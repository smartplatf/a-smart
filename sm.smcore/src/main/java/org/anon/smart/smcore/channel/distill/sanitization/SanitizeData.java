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
        populate.setupTenant(stenant);

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

		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		//CrossLinkFlowDeployment dep = tenant.deploymentShell().deploymentFor(flow);
        String nm = AnnotationUtils.objectName(mpData.event());
		List<CrossLinkFlowDeployment> deps =  tenant.deploymentShell().flowForType(nm);
        assertion().assertTrue(deps.size() > 0, "Cannot find deployment for: " + nm);
        CrossLinkFlowDeployment dep = deps.get(0);
		String flow = dep.deployedName();

		populate.setupFlow(flow);
		populate.setupFlowDeployment(dep);

        Class evt = mpData.event().getClass();
        System.out.println("Event Class for InternalEvent:"+evt.getName());
        assertion().assertNotNull(evt, "No event class found for event: " + eventName);
        populate.setupEventClass(evt);
        populate.setupEventLegend(mpData.dscope().eventLegend(tenant.getRelatedLoader()));

        reflect().setAnyFieldValue(evt, mpData.event(), "___smart_flow_name___", flow);
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
        if(populate.tenant() != null)
            thisTenant  = new CrossLinkSmartTenant(populate.tenant());
        else
            thisTenant =  CrossLinkSmartTenant.currentTenant();

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
    	if(populate.tenant() != null)
    		t = new CrossLinkSmartTenant(populate.tenant());
    	else
    		t = CrossLinkSmartTenant.currentTenant();

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

        SmartTenant tenant = populate.tenant();
        Class cls = typeForKey(key, populate);
        if (cls == null)
            return; //no field present, hence is an internal field value

        String type = AnnotationUtils.className(cls);
        if (type == null)
            return; //not a hosted type

        //TODO: have to take care of the same class being deployed into multiple flows.
        List<FlowDeployment> deps = tenant.deploymentShell().flowForType(type);
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
        if(tenant instanceof SmartTenant)
        	shell = new CrossLinkRuntimeShell(((SmartTenant)tenant).runtimeShell());
        else
        	shell = new CrossLinkRuntimeShell(((CrossLinkSmartTenant)tenant).runtimeShell());
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
    		if(t instanceof SmartTenant)
    			tenant = new CrossLinkSmartTenant(t);
    		else tenant = (CrossLinkSmartTenant)t;

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
                        val = smap.get(VALUE);
                    else
                        val = convert().mapToObject(type, smap);
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
