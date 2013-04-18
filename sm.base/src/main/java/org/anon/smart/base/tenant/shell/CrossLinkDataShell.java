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
 * File:                org.anon.smart.base.tenant.shell.CrossLinkDataShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A crosslink for datashell
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkDataShell extends CrossLinker
{
    public CrossLinkDataShell(Object obj)
    {
        super(obj);
    }

    public CrossLinkDataShell(int start, ClassLoader ldr)
        throws CtxException
    {
        super(ldr);
        create(start);
    }

    protected Class[] parmTypes(String method, Object ... params)
    {
        if (method.equals("<init>"))
            return new Class[] { Integer.TYPE };

        if (method.equals("addSpace"))
            return new Class[] { Object.class };

        return super.parmTypes(method, params);
    }

    public int addStandardSpaces()
        throws CtxException
    {
        Integer ret = (Integer)linkMethod("addStandardSpaces");
        return ret.intValue();
    }

    public int addWorkingSpaces()
        throws CtxException
    {
        Integer ret = (Integer)linkMethod("addWorkingSpaces");
        return ret.intValue();
    }

    public Object addSpace(Object model)
        throws CtxException
    {
        return linkMethod("addSpace", model);
    }

    public void cleanup()
        throws CtxException
    {
        linkMethod("cleanup");
    }
}

