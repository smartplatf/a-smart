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
 * File:                org.anon.smart.secure.inbuilt.data.SmartRole
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data for defining role to be associated with a user
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.smart.secure.access.Access;

import org.anon.utilities.exception.CtxException;

public class SmartRole implements java.io.Serializable
{
    private String _roleName;
    private Map<String, Access> _permitted;
    private boolean _allowAll;
    private Access _accessForAll;
    private boolean _smartAdmin;

    public SmartRole(String name)
    {
        _roleName = name;
        _permitted = new ConcurrentHashMap<String, Access>();
        _allowAll = false;
        _smartAdmin = false;
    }

    public SmartRole(String name, boolean smartadmin)
    {
        this(name);
        _smartAdmin = smartadmin;
    }

    public void allowAccess(String deployedURI, Access access)
    {
        _permitted.put(deployedURI, access);
    }

    public void allowAll(Access access)
    {
        _allowAll = true;
        _accessForAll = access;
    }

    public Access allowedAccess(String uri)
        throws CtxException
    {
        Access ret = _permitted.get(uri);
        if ((ret == null) && (_allowAll))
            ret = _accessForAll;

        return ret;
    }

    public String getName() { return _roleName; }
    public boolean allAllowed() { return _allowAll; }
    public Access defaultAccess() { return _accessForAll; }
    public boolean isSmartAdmin() { return _smartAdmin; }
}

