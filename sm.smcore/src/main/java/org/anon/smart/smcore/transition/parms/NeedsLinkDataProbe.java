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
 * File:                org.anon.smart.smcore.transition.parms.NeedsLinkDataProbe
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-11-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data probe that links from the deployment
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.parms;

import java.util.List;
import java.lang.reflect.Type;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;

import org.anon.utilities.gconcurrent.execute.ParamType;
import org.anon.utilities.gconcurrent.execute.PDescriptor;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.PProbe;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

public class NeedsLinkDataProbe implements AtomicityConstants, PProbe
{
    public NeedsLinkDataProbe()
    {
    }

    public Object valueFor(Class cls, Type t, ProbeParms parms, PDescriptor desc)
        throws CtxException
    {
        String linkname = desc.attribute();
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if (ctx != null)
        {
            String flow = ctx.flow();
            CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
            assertion().assertNotNull(tenant, "Not running in tenant context. Error.");
            CrossLinkDeploymentShell shell = tenant.deploymentShell();
            assertion().assertNotNull(shell, "Not a Valid deployment shell.");
            CrossLinkFlowDeployment dep = shell.deploymentFor(flow);
            String link = dep.getServiceMash(linkname);
            System.out.println("Got Link in NeedsLink: " + link + ":" + linkname);
            if (link != null)
            {
                String[] vals = link.split("\\.");
                ParamType ptype = ParamType.valueOf(vals[0]);
                assertion().assertNotNull(ptype, "Cannot find the related link." + vals[0] + ":" + link);
                String find = "";
                String add = "";
                //the last one is an attribute?
                for (int i = 0; i < (vals.length - 1); i++)
                {
                    find += add + vals[i];
                    add = ".";
                }
                String pstr = "(" + find + ")";
                List<PDescriptor> descs = PDescriptor.parseParamDesc(pstr);
                PProbe p = ptype.myProbe();
                Object val = p.valueFor(Object.class, t, parms, descs.get(0));
                System.out.println("Got Value for: " + pstr + ":" + val + ":" + cls);
                //now get the attribute?
                if (val != null)
                {
                    Object ret = reflect().getAnyFieldValue(val.getClass(), val, vals[vals.length - 1]);
                    System.out.println("Getting the value for: " + pstr + ":" + p + ":" + val + ":" + descs + ":" + ret);
                    return ret;
                }
            }
        }

        return null;
    }

    public Object valueFor(Class cls, Type type, ProbeParms parms)
        throws CtxException
    {
        //if descriptor is not given then we do not know what to search.
        return null;
    }

    public Object valueFor(ProbeParms parms, Type type, PDescriptor desc)
        throws CtxException
    {
        return null;
    }

    public void releaseValues(Object[] val)
        throws CtxException
    {
    }
}

