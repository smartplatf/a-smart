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
 * File:                org.anon.smart.secure.inbuilt.transition.ManageRoles
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A transition to manage roles
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.transition;

import java.util.Map;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.inbuilt.data.SmartRole;
import org.anon.smart.secure.inbuilt.events.CreateRole;
import org.anon.smart.secure.inbuilt.responses.SecurityResponse;
import org.anon.smart.base.tenant.shell.RuntimeShell;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class ManageRoles
{
    private static String DEFAULT = "DefaultRole";

    public ManageRoles()
    {
    }

    public void createRole(CreateRole role)
        throws CtxException
    {
        SecurityResponse resp = null;
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String group = className(SmartRole.class);
        String flow = flowFor(SmartRole.class);
        Object exist = rshell.lookupFor(flow, group, role.getRoleName());
        if (exist != null)
        {
            resp = new SecurityResponse("A role already exists for: " + role.getRoleName());
            return;
        }

        SmartRole srole = new SmartRole(role.getRoleName());
        Map<String, String> permits = role.getPermits();
        if (permits != null)
        {
            for (String uri : permits.keySet())
            {
                String access = permits.get(uri);
                Access a = Access.valueOf(access);
                assertion().assertNotNull(a, "Cannot find access " + access + " for uri: " + uri);
                srole.allowAccess(uri, a);
            }
        }

        String allaccess = role.getAllAccess();
        Access a = Access.valueOf(allaccess);
        assertion().assertNotNull(a, "Cannot find access " + allaccess);
        srole.allowAll(a);
        boolean issmartadmin = role.isSmartAdmin();
        //TODO:
        resp = new SecurityResponse("Created a role for: " + role.getRoleName());
    }


    public static SmartRole createDefaultRole()
        throws CtxException
    {
        SmartRole srole = new SmartRole(DEFAULT);
        srole.allowAll(Access.execute);
        return srole;
    }

    public static SmartRole getDefaultRole()
        throws CtxException
    {
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String group = className(SmartRole.class);
        String flow = flowFor(SmartRole.class);
        SmartRole exist = (SmartRole)rshell.lookupFor(flow, group, DEFAULT);
        //TODO: remove when default roles are defined.
        if (exist == null)
            exist = createDefaultRole();

        return exist;
    }

    public static String getDefaultRoleName() { return DEFAULT; }

    public Object clCreateDefaultRole(ClassLoader ldr)
        throws CtxException
    {
        CrossLinkAny any = new CrossLinkAny(this.getClass().getName(), ldr);
        any.create();
        return any.invoke("createDefaultRole");
    }
}

