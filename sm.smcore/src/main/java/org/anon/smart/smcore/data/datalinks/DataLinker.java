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
 * File:                org.anon.smart.smcore.data.datalinks.DataLinker
 * Author:              rsankar
 * Revision:            1.0
 * Date:                18-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A linker that manages the data links
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.data.datalinks;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.base.flow.CrossLinkLink;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.transition.TransitionContext;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class DataLinker
{
    CrossLinkSmartTenant _tenant;
    CrossLinkDeploymentShell _dshell;
    RuntimeShell _rshell;

    public DataLinker()
        throws CtxException
    {
        _tenant = CrossLinkSmartTenant.currentTenant();
        _dshell = _tenant.deploymentShell();
        _rshell = (RuntimeShell)_tenant.runtimeShell();
    }

    private Class getRelationClass(Class cls, Type type)
        throws CtxException
    {
        Class ret = cls;
        if (type instanceof ParameterizedType)
        {
            //assumption is only the first level is parameterized. We cannot handle
            //List<List<Object>>. Such situation cannot occur.
            ParameterizedType ptype = (ParameterizedType)type;
            Type[] actuals = ptype.getActualTypeArguments();
            //again we do not handle Map<Somthing, something>. Hence this
            //has to be only one parameter.
            ret = (Class)actuals[0]; //if the parameter is not qualified, we have a problem
        }

        return ret;
    }

    public List<SmartData> getLinked(Class cls, Type type, SmartPrimeData data)
        throws CtxException
    {
        //for now only the first level of relation is retrieved
        Class pcls = getRelationClass(cls, type);
        String tflow = flowFor(pcls);
        String tname = className(pcls);
        String flow = tflow;

        if ((tflow == null) || (tflow.length() <= 0))
            return null; // not a hosted object, hence not handled by us. 

        if ((tname == null) || (tname.length() <= 0))
            return null; //not a hosted object, not handled by us.

        String fflow = flowFor(data.getClass());
        String fname = objectName(data);

        //try to get the links from the flow for the object for which this is being
        //declared. parameters has to flow via the prime data. 
        CrossLinkFlowDeployment dep = _dshell.deploymentFor(tflow);
        List<CrossLinkLink> lnks = dep.linksFor(tflow, tname);
        if (((lnks == null) || (lnks.size() <= 0)) && (!fflow.equals(tflow)))
        {
            //try to get it from the prime object if the first is not 
            //present
            dep = _dshell.deploymentFor(fflow);
            lnks = dep.linksFor(tflow, tname);
            flow = fflow;
        }

        System.out.println("Got for: " + pcls + ":" + lnks + ":" + flow + ":" + tflow + ":" + fflow);

        for (int i = 0; (lnks != null) && (i < lnks.size()); i++)
        {
            //what if there is more than one link for the same object??
            //not sure we shd allow this at all?
            CrossLinkLink l = lnks.get(i);
            CrossLinkLink.CrossLinkLinkObject fobj = l.getFromObject();
            CrossLinkLink.CrossLinkLinkObject tobj = l.getToObject();

            String attr = null;
            if (fobj.getFlow().equals(fflow) && fobj.getObject().equals(fname))
                attr = fobj.getField();
            else if (tobj.getFlow().equals(fflow) && fobj.getObject().equals(tname))
                attr = tobj.getField();
            if (attr != null)
            {
                Object key = reflect().getAnyFieldValue(data.getClass(), data, attr);
                System.out.println("Retrieved: " + attr + ":" + key);
                if (key != null)
                {
                    LinkedData ldata = getLinkBetween(fobj.getFlow(), fobj.getObject(), tobj.getObject(), key);
                    if (ldata != null)
                        return getDataFor(tflow, ldata, pcls);
                }
            }
        }

        return null;
    }

    private List<SmartData> getDataFor(String flow, LinkedData data, Class pcls)
        throws CtxException
    {
        List<UUID> objects = data.getLinks();
        if (objects == null)
            return null;

        List<SmartData> ret = new ArrayList<SmartData>();
        String ltype = data.linkType();
        for (UUID id : objects)
        {
            SmartData d = (SmartData)_rshell.lookupFor(flow, ltype, id);
            System.out.println("Retrieving data for: " + ltype + ":" + id + ":" + d);
            if ((d != null) && (type().isAssignable(d.getClass(), pcls)))
                ret.add(d);
        }

        return ret;
    }

    private LinkedData getLinkBetween(String fromflow, String from, String to, Object key)
        throws CtxException
    {
        String group = from + "__" + to;
        LinkedData ldata = (LinkedData)_rshell.lookupFor(fromflow, group, key);
        return ldata;
    }


    private void createLinks(TransitionContext ctx, List<CrossLinkLink> links, Class dcls, SmartData truthData, SmartData modified)
        throws CtxException
    {
        //TODO: need to lock LinkedData before modifying
        for (CrossLinkLink l : links)
        {
            CrossLinkLink.CrossLinkLinkObject fobj = l.getFromObject();
            CrossLinkLink.CrossLinkLinkObject tobj = l.getToObject();
            String toflow = tobj.getFlow();
            String toobj = tobj.getObject();
            String fflow = fobj.getFlow();
            String fobject = fobj.getObject();
            String attr = fobj.getField();
            Object key = reflect().getAnyFieldValue(dcls, truthData, attr);
            Object modkey = reflect().getAnyFieldValue(dcls, modified, attr);
            UUID addrelated = truthData.smart___id();

            if (l.isInternal())
            {
                //get the data and set it correctly
                SmartEvent evt = ctx.event();
                String evtname = objectName(evt);
                CrossLinkLink.CrossLinkLinkObject via = l.getViaObject();
                if ((via != null) && (evtname != null) && (via.getObject().equals(evtname)))
                {
                    String eattr = via.getField();
                    Object setkey = reflect().getAnyFieldValue(evt.getClass(), evt, eattr);
                    if (setkey != null)
                    {
                        modkey = setkey;
                        reflect().setAnyFieldValue(dcls, modified, attr, setkey);
                    }
                }
            }

            if (modkey != null)
            {
                LinkedData ldata = getLinkBetween(fflow, fobject, toobj, modkey);
                if (ldata == null)
                    ldata = new LinkedData(fobject, toobj, modkey);

                ldata.addLink(addrelated);
                ctx.transaction().addToTransaction(ldata, fflow);
                if ((key != null) && (!modkey.equals(key)))
                {
                    LinkedData pldata = getLinkBetween(fflow, fobject, toobj, key);
                    if (pldata != null)
                        pldata.removeLink(addrelated);
                    ctx.transaction().addToTransaction(pldata, fflow);
                }
            }
        }
    }

    public void createLinks(TransitionContext ctx, SmartDataED edata, SmartData truthData, boolean isNew)
        throws CtxException
    {
        String flow = flowFor(truthData.getClass());
        String name = truthData.smart___name();
        List<CrossLinkFlowDeployment> linked = _dshell.linkedDeploymentsFor(flow, name);
        SmartData modified = edata.empirical();
        Class dcls = truthData.getClass();

        if (linked == null)
            return;

        if (linked.size() <= 0)
            return;

        for (CrossLinkFlowDeployment clf : linked)
        {
            List<CrossLinkLink> links = clf.toLinksFor(flow, name);
            if ((links == null) || (links.size() <= 0))
                continue;
            createLinks(ctx, links, dcls, truthData, modified);
        }
    }
}

