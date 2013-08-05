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
 * File:                org.anon.smart.secure.stt.tl.SecurityTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of configuration for security related config
 *
 * ************************************************************
 * */

package org.anon.smart.secure.stt.tl;

import java.util.List;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.secure.stt.Constants;
import org.anon.smart.secure.annot.SecurityAnnotate;

import org.anon.utilities.exception.CtxException;

public class SecurityTL extends BaseTL implements Constants
{
    private List<GuardTL> security;

    public SecurityTL()
    {
    }

    public List<GuardTL> getSecurity() { return security; }

    public String[] getTypes()
        throws CtxException
    {
        return new String[] { SECUREDATA };
    }


    @Override
    public Class[] getAnnotations(String name)
        throws CtxException
    {
        //assumption is that this is an extra with others, hence base class annotates is
        //already present.
        return new Class[] { SecurityAnnotate.class };
    }

    public static SecurityTL defaultFor(String clsname, String type, String flow, String[] parms)
    {
        SecurityTL ret = new SecurityTL();
        return ret;
    }

    @Override
    public String[] getExtras()
        throws CtxException
    {
        return new String[0];
    }

}

