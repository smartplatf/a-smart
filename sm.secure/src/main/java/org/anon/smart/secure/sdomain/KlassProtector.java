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
 * File:                org.anon.smart.secure.sdomain.KlassProtector
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A protector for classes in smart
 *
 * ************************************************************
 * */

package org.anon.smart.secure.sdomain;

import java.util.Map;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.security.CodeSource;
import java.security.CodeSigner;
import java.security.ProtectionDomain;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Permission;

import org.anon.smart.secure.access.AccessRequest;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class KlassProtector extends ProtectionDomain
{
    private String _name;
    private Map<Class, Permissions> _protected;

    public KlassProtector(String name, CodeSource source, PermissionCollection perms)
    {
        super(source, perms);
        _protected = new ConcurrentHashMap<Class, Permissions>();
        _name = name;
    }

    public void protectKlass(Class cls)
        throws CtxException
    {
        Permissions perms = new Permissions();
        String pkg = type().getPackage(this.getClass());
        String clspkg = type().getPackage(cls);
        if (pkg.equals(clspkg))
        {
            PassThruPermission p = new PassThruPermission(cls);
            perms.add(p);
        }
        else
        {
            SmartPermission p = new SmartPermission(cls);
            perms.add(p);
        }
        _protected.put(cls, perms);
    }

    public boolean implies(Permission permission)
    {
        if (permission instanceof RequestPermission)
        {
            RequestPermission perm = (RequestPermission)permission;
            AccessRequest request = perm.getRequest();
            Class cls = request.getAccessed().getAccessedClass();
            //only if it is protected
            if (_protected.containsKey(cls))
            {
                Permissions perms = _protected.get(cls);
                boolean ret = false;
                Enumeration<Permission> iter = perms.elements();
                while (iter.hasMoreElements())
                {
                    Permission thisperm = iter.nextElement();
                    if (thisperm instanceof SmartPermission)
                        ret = ret || thisperm.implies(permission);
                }

                return ret;
            }
        }

        return true;
    }

}

