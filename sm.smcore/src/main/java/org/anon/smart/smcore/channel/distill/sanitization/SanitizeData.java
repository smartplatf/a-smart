/**
 * SMART - State Machine ARchiTecture
 *
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of SMART.
 *
 * SMART is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SMART is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * */

/**
 * ************************************************************
 * HEADERS
 * ************************************************************
 * File:                org.anon.smart.smcore.channel.distill.sanitization.SanitizeData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                19-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A sanitizer for data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.sanitization;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.anon.smart.d2cache.QueryObject;
import org.anon.smart.channels.data.PData;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantsHosted;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell;
import org.anon.smart.base.utils.AnnotationUtils;

import org.anon.smart.smcore.data.CrossLinkSmartPrimeData;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.channel.server.EventPData;
import org.anon.smart.smcore.channel.server.EventDScope;
import org.anon.smart.smcore.channel.distill.ChannelConstants;
import org.anon.smart.smcore.channel.distill.translation.MapData;
import org.anon.smart.smcore.channel.distill.translation.ObjectData;
import org.anon.smart.smcore.channel.internal.MessagePData;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SanitizeData implements ChannelConstants
{
    public SanitizeData()
    {
    }

    public void sanitizePData(PData data, SearchedData populate)
        throws CtxException
    {
        System.out.println("data is: " + (data instanceof MessagePData) + ":" + data);
    	if(data instanceof MessagePData)
    	{
    		sanitizePDataForMessage(data, populate);
    		return;
    	}
        EventPData epdata = (EventPData)data;
        EventDScope epscope = (EventDScope)data.dscope();

        String tenant = epdata.tenant();
        SmartTenant stenant = TenantsHosted.tenantFor(tenant, false);
        assertion().assertNotNull(stenant, "Tenant " + tenant + " does not exist");

        if ((stenant.getDomain() != null) && (stenant.getDomain().length > 0))
        {
            String[] domain = stenant.getDomain();
            String checkagainst = epscope.origin();
            if (checkagainst == null)
            {
                checkagainst = epscope.referer();
                if (checkagainst != null)
                {
                    int index = checkagainst.indexOf("/", 7);
                    checkagainst = checkagainst.substring(0, index);
                }
            }


            assertion().assertNotNull(checkagainst, "Cannot be called from null origin. Has to be called from: " + stenant.getDomain());

            String origin = checkagainst;
            String compare1 = origin;
            if (origin.startsWith("http://"))
                compare1 = origin.substring(7);

            String compare2 = compare1;
            if (compare1.startsWith("www"))
                compare2 = compare1.substring(4);
            else
                compare2 = "www." + compare1;

            String[] diffvers = new String[3]; //add more if present here.
            diffvers[0] = origin;
            diffvers[1] = compare1;
            diffvers[2] = compare2;

            boolean found = false;

            for (int i = 0; i < domain.length; i++)
            {
                for (int j = 0; j < diffvers.length; j++)
                {
                    if (diffvers[j].equals(domain[i]))
                    {
                        found = true;
                        break;
                    }
                }
            }

            String domains = "";
            for (int i = 0; i < domain.length; i++)
                domains += " " + domain[i];
            assertion().assertTrue(found, "Cannot be called from origin: " + origin + ". Has to be called from one of: " + domains);
        }

        populate.setupTenant(new CrossLinkSmartTenant(stenant));

        String flow = epdata.flow();
        FlowDeployment dep = stenant.deploymentShell().deploymentFor(flow);
        assertion().assertNotNull(dep, "No deployment found for flow: " + flow);
        populate.setupFlowDeployment(dep);
        populate.setupFlow(flow);

        String eventName = epdata.eventName();
        Class evt = stenant.deploymentShell().eventClass(flow, eventName);
        if (evt == null)
            evt = stenant.deploymentShell().eventClass("AllFlows", eventName); //try getting from AllFlows
        assertion().assertNotNull(evt, "No event class found for event: " + eventName);
        populate.setupEventClass(evt);
        populate.setupEventLegend(epscope.eventLegend(stenant.getRelatedLoader()));
    }

    private void sanitizePDataForMessage(PData data, SearchedData populate)
    	throws CtxException
    {
    	try
    	{
		MessagePData mpData = (MessagePData)data;

		String eventName = mpData.eventName();

		//CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        //DO NOT TAKE FOR GRANTED IT IS THE CURRENT TENANT.  I WANT TO BE ABLE TO POST CROSS_TENANT
        CrossLinkSmartTenant tenant = TenantsHosted.crosslinkedTenantFor(mpData.tenant());
        populate.setupTenant(tenant);
		//CrossLinkFlowDeployment dep = tenant.deploymentShell().deploymentFor(flow);
        String nm = AnnotationUtils.objectName(mpData.event());
        java.lang.reflect.Field dest = AnnotationUtils.destinations(mpData.event().getClass());
        if (dest != null)
        {
            dest.setAccessible(true);
            Object val = reflect().getAnyFieldValue(mpData.event().getClass(), mpData.event(), dest.getName());
            assertion().assertNotNull(val, "Destination value for " + dest.getName() + " is NULL in INTERNAL event.");
            nm = AnnotationUtils.objectName(val);
            System.out.println("Got destination field as: " + dest + ":" + nm + ":" + val.getClass() + ":");
        }
		List<CrossLinkFlowDeployment> deps =  tenant.deploymentShell().flowForType(nm);
        assertion().assertTrue(deps.size() > 0, "Cannot find deployment for: " + nm);
        CrossLinkFlowDeployment dep = deps.get(0);
		String flow = dep.deployedName();

		populate.setupFlow(flow);
		populate.setupFlowDeployment(dep);

        Class evtcls = (mpData.event().getClass());
        Class evt = tenant.getRelatedLoader().loadClass(evtcls.getName());
        System.out.println("Event Class for InternalEvent:"+evt.getName());
        assertion().assertNotNull(evt, "No event class found for event: " + eventName);
        populate.setupEventClass(evt);
        populate.setupEventLegend(mpData.dscope().eventLegend(tenant.getRelatedLoader()));

        reflect().setAnyFieldValue(evtcls, mpData.event(), "___smart_flow_name___", flow);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}

	}


	public void sanitizeMap(MapData map, SearchedData populate)
        throws CtxException
    {
        String flow = null;
        Map<String, Object> data = map.translated();
        populate.setupMappedData(data);
        List<String> prime = null;
        if(populate.flowDeployment() != null)
        	prime = populate.flowDeployment().getPrimeData();
        else
        	prime = populate.crossLinkFlowDeployment().getPrimeData();
        CrossLinkSmartTenant thisTenant;
        thisTenant  = populate.tenant();

        CrossLinkFlowDeployment fd = thisTenant.deploymentShell().deploymentFor("AllFlows");

        List<String> primesAllFlows =  fd.getPrimeData();
        for(String p : primesAllFlows)
        {
            if(!p.equals("org.anon.smart.base.flow.FlowAdmin"))
            {
                if (!prime.contains(p))
                    prime.add(p);
            }
        }
        assertion().assertTrue((prime.size() > 0), "The flow does not have even one prime enabled.");
        List<String> sanitizedKeys = new ArrayList<String>();
        for (String p : prime)
        {
            String lookp = null;
            if(populate.flowDeployment() != null)
            {
            	lookp = populate.flowDeployment().nameFor(p);
                flow = populate.getFlow();
            }
            else
            {
            	lookp = populate.crossLinkFlowDeployment().nameFor(p);
                flow = populate.getFlow();
            }

            if(lookp == null)
            {
            	lookp =	fd.nameFor(p);
                flow = fd.deployedName();
            }
            if (data.containsKey(lookp))
            {
                //means we need to post to these flows
                Object search = data.get(lookp);
                sanitizePrime(flow, lookp, search, populate);
                sanitizedKeys.add(lookp);
            }
        }

        //check FlowAdmin
        if ((populate.getPrimes().size() <= 0) && (data.containsKey("FlowAdmin")))
        {
            //check if FlowAdmin is present
            Object search = data.get("FlowAdmin");
            sanitizePrime(populate.getFlow(), "FlowAdmin", search, populate);
            sanitizedKeys.add("FlowAdmin");
        }

        //check in linked flows
        if ((populate.getPrimes().size() <= 0) && (populate.flowDeployment() != null))
        {
            String[] lflows = populate.flowDeployment().getLinkedFlows();
            System.out.println("Got linked flows as: " + lflows.length);
            for (int i = 0; (lflows != null) && (i < lflows.length); i++)
            {
                String check = lflows[i];
                CrossLinkFlowDeployment lfd = thisTenant.deploymentShell().deploymentFor(check);
                List<String> lprimes =  lfd.getPrimeData();
                System.out.println("Checking for linked flow : " + check + ":" + lprimes);
                for (int j = 0; j < lprimes.size(); j++)
                {
                    String lobj = lprimes.get(j);
                    String llook =	lfd.nameFor(lobj);
                    System.out.println("Checking for linked flow : " + check + ":" + llook + ":" + data.get(llook));
                    if (data.containsKey(llook))
                    {
                        Object srch = data.get(llook);
                        sanitizePrime(check, llook, srch, populate);
                        sanitizedKeys.add(llook);
                        break;
                    }
                }
            }
        }

        assertion().assertTrue((populate.getPrimes().size() > 0), "There are no flows defined to which to post the event.");

        for (String key : data.keySet())
        {
            if (!sanitizedKeys.contains(key)) //already done in prime sanitization
                sanitize(key, data.get(key), populate);
        }
    }


    private void sanitizePrime(String flow, String type, Object val, SearchedData populate)
        throws CtxException
    {
    	CrossLinkSmartTenant t = null;
        t = populate.tenant();

        Class typecls = t.deploymentShell().primeClass(flow, type);
        if (typecls == null)    {
            typecls = t.deploymentShell().primeClass("AllFlows", type);
        }
        assertion().assertNotNull(typecls, "Cannot find the class for: <<<<" + type + ">>>>");
        List<CrossLinkFlowDeployment> deps = new ArrayList<CrossLinkFlowDeployment>();
        CrossLinkFlowDeployment dep = null;
        if(populate.flowDeployment() != null)
        	dep = new CrossLinkFlowDeployment(populate.flowDeployment());
        else
        	dep = populate.crossLinkFlowDeployment();
        deps.add(dep);
        if (!flow.equals(dep.deployedName()))
        {
            CrossLinkFlowDeployment fdep = t.deploymentShell().deploymentFor(flow);
            deps.add(fdep);
        }
        Object searched = searchData(flow, type, typecls, val, deps, t);

        assertion().assertNotNull(searched, "Cannot find object of type: " + type + ":" + val);

        Collection primes = null;

        if (searched instanceof Collection)
        {
            primes = (Collection)searched;
        }
        else
        {
            primes = new ArrayList();
            primes.add(searched);
        }

        for (Object prime : primes)
        {
            CrossLinkSmartPrimeData d = new CrossLinkSmartPrimeData(prime);
            Object thisflow = d.smart___flow();
            populate.addPrime(prime, thisflow);
        }
    }

    private Class typeForKey(String key, SearchedData populate)
        throws CtxException
    {
        Class evtcls = populate.eventClass();
        Field fld = reflect().getAnyField(evtcls, key);
        if (fld == null)
            return null;

        Class cls = fld.getType();
        Type fldtype = fld.getGenericType();
        if (fldtype instanceof ParameterizedType)
        {
            Type[] types = ((ParameterizedType)fldtype).getActualTypeArguments();
            cls = (Class)types[types.length - 1]; //assumption is what here?
        }

        return cls;
    }

    private void sanitize(String key, Object value, SearchedData populate)
        throws CtxException
    {
        if (!(value instanceof Map) && !(value instanceof Collection))
            return;

        CrossLinkSmartTenant tenant = populate.tenant();
        Class cls = typeForKey(key, populate);
        System.out.println("For key: " + key + ":" + cls);
        if (cls == null)
            return; //no field present, hence is an internal field value

        String type = AnnotationUtils.crossClassName(cls);
        if (type == null)
            return; //not a hosted type

        System.out.println("For key: " + key + ":" + cls + ":" + type);
        //TODO: have to take care of the same class being deployed into multiple flows.
        List deps = tenant.deploymentShell().flowForType(type);
        assertion().assertTrue((deps.size() > 0), "Cannnot find the deployment for: " + type);
        Object val = searchData(populate.getFlow(), type, cls, value, deps, tenant);
        if (val instanceof List)
            populate.addSearch(key, (List<Object>)val);
        else
            populate.addSearch(key, val);
    }

    protected Object searchOne(String name, Object search, String spacemodel, String action, Object tenant)
        throws CtxException
    {
        System.out.println("Searching in: " + spacemodel + ":" + name + ":" + search);
        if (search == null)
            return null;

        Object ret = null;
        CrossLinkRuntimeShell shell = null;
        shell = new CrossLinkRuntimeShell(((CrossLinkSmartTenant)tenant).runtimeShell());
        System.out.println("Searching in: " + spacemodel + ":" + name + ":" + search + ":" + ((CrossLinkSmartTenant)tenant).getName());
        if ((action == null) || action.equals(LOOKUP_ACTION))
        {
            ret = shell.lookupFor(spacemodel, name, search);
        }
        else if (action.equals(SEARCH_ACTION))
        {
            ret = shell.searchFor(spacemodel, name, search);
        }

        return ret;
    }
    private Object searchData(String thisflow, String name, Class cls, Object search, List deps, Object t)
            throws CtxException
        {
    		CrossLinkSmartTenant tenant = null;
    		tenant = new CrossLinkSmartTenant(t);

            Object ret = null;
            //has to be a collection or a map, else it is nothing
            if (search instanceof Collection)
            {
                Collection coll = (Collection)search;
                List vals = new ArrayList<Object>();
                for (Object one : coll)
                {
                    Object val = searchData(thisflow, name, cls, one, deps, tenant);
                    vals.add(val);
                }
                ret = vals;
            }
            else if (search instanceof Map)
            {
                Map smap = (Map)search;
                Class type = null;
                String action = (String)smap.get(ACTION);
                if (action == null) action = "";
                String flow = (String)smap.get(SEARCH_FLOW);
                if (flow == null)
                    flow = thisflow;
                CrossLinkFlowDeployment dep = null;
                for (int i = 0; i < deps.size(); i++)
                {
                	CrossLinkFlowDeployment d = null;
                	if(deps.get(i) instanceof FlowDeployment)
                		d = new CrossLinkFlowDeployment(deps.get(i));
                	else
                		d = (CrossLinkFlowDeployment)deps.get(i);
                    if (d.deployedName().equals(flow))
                    {
                        dep = d;
                        break;
                    }
                }

                assertion().assertNotNull(dep, "Cannot find the deployment for: " + name + ":" + flow);
                try
                {
                    Object val = null;
                    if ((action.equals(LOOKUP_ACTION)) && (smap.containsKey(KEY_TYPE)))
                    {
                        type = tenant.getRelatedLoader().loadClass(smap.get(KEY_TYPE).toString());
                    }
                    else if (action.equals(LOOKUP_ACTION))
                    {
                        Class[] keyTypes = AnnotationUtils.keyTypes(cls);
                        //blindly take the first, if more than one, it has to be
                        //specified, else there will anyways be an error
                        type = keyTypes[0];
                    }
                    else if (action.equals(SEARCH_ACTION))
                    {
                        //load search class and populate it
                        type = tenant.getRelatedLoader().loadClass(QueryObject.class.getName());
                    }


                    if ((type != null) && (type().checkPrimitive(type)))
                    {
                        val = smap.get(VALUE);
                        if ((!type().isAssignable(type, val.getClass())) && convert().canConvertFromString(type))
                            val = convert().stringToClass((String)smap.get(VALUE), type);
                    }
                    else
                    {
                        val = convert().mapToObject(type, smap);
                    }

                    System.out.println("Got type as: " + type + ":" + convert().canConvertFromString(type) + ":" + val.getClass() + ":" + val);
                   	assertion().assertTrue(((val != null) && type().isAssignable(type, val.getClass())),
                            "Cannot lookup value: " + val + " not correct type: " + type.getName() + ":" + val);

                    if (action != null) //only if there is an action do we search, else we do not
                        ret = searchOne(name, val, dep.deployedName(), action, tenant);
                }
                catch (Exception e)
                {
                    except().rt(e, new CtxException.Context("Exception in finding key: ", "Exception"));
                }
            }

            return ret;
        }
}
