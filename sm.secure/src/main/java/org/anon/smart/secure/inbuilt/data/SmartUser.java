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
 * File:                org.anon.smart.secure.inbuilt.data.SmartUser
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A user registered in smart to have access
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.tenant.shell.RuntimeShell;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SmartUser implements java.io.Serializable
{
    private String _id;
    private String _name;
    private List<String> _roles;

    private transient List<SmartRole> _roleObjects;

    public SmartUser(String id, String name)
    {
        _id = id;
        _name = name;
        _roles = new ArrayList<String>();
    }

    public String getID() { return _id; }
    public String getName() { return _name; }

    public void addRole(String rolename) 
    { 
        if (!_roles.contains(rolename))
            _roles.add(rolename); 
    }

    public List<String> getRoles()
    {
        return _roles;
    }

    public boolean playsRole(String name)
    {
        return _roles.contains(name);
    }

    public void removeRole(String rolename)
    {
        _roles.remove(rolename);
    }

    public static SmartUser getUser(String id)
        throws CtxException
    {
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String usrname = className(SmartUser.class);
        String flowname = flowFor(SmartUser.class);
        SmartUser ret = (SmartUser)rshell.lookupFor(flowname, usrname, id);
        assertion().assertNotNull(ret, "Cannot find user " + id);
        return ret;
    }

    public List<SmartRole> lookupRoles()
        throws CtxException
    {
        //cache for this instance, if loaded from db, then
        //populate it the first time it is accessed
        if (_roleObjects == null)
        {
            RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
            String rolename = className(SmartRole.class);
            String flowname = flowFor(SmartRole.class);
            _roleObjects = new ArrayList<SmartRole>();
            for (String rname : _roles)
            {
                SmartRole role = (SmartRole)rshell.lookupFor(flowname, rolename, rname);
                assertion().assertNotNull(role, "Cannot find role " + rname + " played by " + _id);
                _roleObjects.add(role);
            }
        }

        return _roleObjects;
    }
}

