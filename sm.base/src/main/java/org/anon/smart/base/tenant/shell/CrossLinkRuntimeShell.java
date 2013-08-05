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
 * File:                org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a crosslink for runtimeshell
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import java.util.List;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkRuntimeShell extends CrossLinker
{
    public CrossLinkRuntimeShell(Object obj)
    {
        super(obj);
    }

    public CrossLinkRuntimeShell(ClassLoader ldr)
        throws CtxException
    {
        super(ldr);
        create();
    }

    protected Class[] parmTypes(String method, Object ... params)
    {
        if (method.equals("lookupFor") || method.equals("searchFor"))
            return new Class[] { String.class, String.class, Object.class };
        else if (method.equals("enabledFlowClazzez"))
            return new Class[] { Object.class, Class[].class };
        else if (method.equals("commitInternalObjects"))
            return new Class[] { String.class, Object[].class };

        return super.parmTypes(method, params);
    }

    public void cleanup()
        throws CtxException
    {
        linkMethod("cleanup");
    }

    public Object lookupFor(String spacemodel, String group, Object key)
        throws CtxException
    {
        return linkMethod("lookupFor", spacemodel, group, key);
    }

    public List<Object> searchFor(String spacemodel, String group, Object query)
        throws CtxException
    {
        return (List<Object>)linkMethod("searchFor", spacemodel, group, query);
    }

    public void enabledFlowClazzez(Object model, Class[] clazzez)
        throws CtxException
    {
        linkMethod("enabledFlowClazzez", model, clazzez);
    }

    public void commitInternalObjects(String spacemodel, Object[] objs)
        throws CtxException
    {
        linkMethod("commitInternalObjects", spacemodel, objs);
    }
}

