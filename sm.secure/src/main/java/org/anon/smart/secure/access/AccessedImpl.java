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
 * File:                org.anon.smart.secure.access.AccessedImpl
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An implementation for accessed
 *
 * ************************************************************
 * */

package org.anon.smart.secure.access;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.cthreads.RuntimeContext;
import org.anon.utilities.exception.CtxException;

public class AccessedImpl implements Accessed
{
    private Object _accessed;
    private Class _accessClass;
    private Object[] _parms;
    private RuntimeContext _rContext;

    public AccessedImpl(Object acc, Object[] parms)
        throws CtxException
    {
        _accessed = acc;
        _parms = parms;
        _accessClass = acc.getClass();
        _rContext = threads().runtimeContext();
    }

    public Object getAccessed()
    {
        return _accessed;
    }

    public Class getAccessedClass()
    {
        return _accessClass;
    }

    public RuntimeContext getRuntime()
    {
        return _rContext;
    }

    public Object[] parms()
    {
        return _parms;
    }
}

