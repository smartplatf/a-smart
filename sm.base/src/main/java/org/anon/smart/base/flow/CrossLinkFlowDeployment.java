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
 * File:                org.anon.smart.base.flow.CrossLinkFlowDeployment
 * Author:              rsankar
 * Revision:            1.0
 * Date:                20-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A crosslink for flowdeployment object
 *
 * ************************************************************
 * */

package org.anon.smart.base.flow;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkFlowDeployment extends CrossLinker
{
    public CrossLinkFlowDeployment(Object obj)
    {
        super(obj);
    }

    public List<String> getPrimeData()
        throws CtxException
    {
        return (List<String>)linkMethod("getPrimeData");
    }

    public Object model(ClassLoader ldr)
        throws CtxException
    {
        return linkMethod("model", ldr);
    }

    protected Class[] parmTypes(String method, Object ... params)
    {
        if (method.equals("model"))
        {
            return new Class[] { ClassLoader.class };
        }

        return super.parmTypes(method, params);
    }
    
    public String deployedName() 
        throws CtxException
    {
    	return (String)linkMethod("deployedName");
    }

    public String classFor(String name)
        throws CtxException
    {
        return (String)linkMethod("classFor", name);
    }

    public List<String> getDeploymentFor(String dtype)
        throws CtxException
    {
        return (List<String>)linkMethod("getDeploymentFor", dtype);
    }
    
    public String nameFor(String p)
    	throws CtxException
    {
    	return (String)linkMethod("nameFor", p);
    }

    private List<CrossLinkLink> linkGetMethod(List lst)
        throws CtxException
    {
        List<CrossLinkLink> ret = null;
        if (lst != null)
        {
            ret = new ArrayList<CrossLinkLink>();
            for (Object o : lst)
                ret.add(new CrossLinkLink(o));
        }
        return ret;
    }

    public List<CrossLinkLink> getLinks()
        throws CtxException
    {
        List lst = (List)linkMethod("getLinks");
        return linkGetMethod(lst);
    }

    public List<CrossLinkLink> linksFor(String flow, String name)
        throws CtxException
    {
        List lst = (List)linkMethod("linksFor", flow, name);
        return linkGetMethod(lst);
    }

    public List<CrossLinkLink> toLinksFor(String flow, String name)
        throws CtxException
    {
        List lst = (List)linkMethod("toLinksFor", flow, name);
        return linkGetMethod(lst);
    }

    public String[] deployedURI(String nm)
        throws CtxException
    {
        return (String[])linkMethod("deployedURI", nm);
    }

    public String getServiceMash(String name)
        throws CtxException
    {
        return (String)linkMethod("getServiceMash", name);
    }

    public Set<String> getNeedLinkNames()
        throws CtxException
    {
        return (Set<String>)linkMethod("getNeedLinkNames");
    }

    public String[] getLinkedFlows()
        throws CtxException
    {
        return (String[])linkMethod("getLinkedFlows");
    }
}

