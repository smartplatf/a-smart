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
 * File:                org.anon.smart.secure.inbuilt.transition.TransitionService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of services for transitions
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.transition;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.secure.guards.Constants;
import org.anon.smart.secure.inbuilt.data.SmartRole;
import org.anon.smart.secure.inbuilt.data.SmartUser;
import org.anon.smart.secure.inbuilt.data.iden.Identity;
import org.anon.smart.smcore.inbuilt.transition.DefaultObjectsManager;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class TransitionService implements Constants, DefaultObjectsManager.TenantDefaultCreator, DefaultObjectsManager.InternalServices
{
    private TransitionService()
    {
    }

    public static void setupSystemContext()
        throws CtxException
    {
        threads().addToContextLocals(SYSTEM_RUNTIME, STARTUP_DEFAULTS);
    }

    public void addTenantObjects(TenantAdmin admin, SmartTenant tenant, ClassLoader tenantldr)
        throws CtxException
    {
        CrossLinkAny any = new CrossLinkAny(this.getClass().getName(), tenantldr);
        any.invoke("setupSystemContext");
        ManageRoles roles = new ManageRoles();
        Object role = roles.clCreateDefaultRole(tenantldr);
        ManageUsers users = new ManageUsers();
        Object usr = users.clCreateDefaultUser(tenantldr, tenant.getName());
        String flow = flowFor(SmartUser.class);

        List<Object> commit = new ArrayList<Object>();
        //assumption here is that the three objects belong to the same flow. If this is
        //changed, then change here.
        if (role != null)
            commit.add(role);

        if (usr != null)
            commit.add(usr);

        Object ident = users.clAddIdentity(tenantldr, usr, tenant.getName());
        if (ident != null)
            commit.add(ident);
        admin.addTenantObjects(flow, commit);
    }

    public static void initialize()
        throws CtxException
    {
        DefaultObjectsManager.addCreator(new TransitionService());
        DefaultObjectsManager.addInternalServices(new TransitionService());
    }

    public static void setupServiceContext(String svc)
        throws CtxException
    {
        threads().addToContextLocals(SYSTEM_RUNTIME, svc);
    }

    public void setupContext(String svc, ClassLoader ldr)
        throws CtxException
    {
        System.out.println("Setting up internal context for: " + svc);
        CrossLinkAny any = new CrossLinkAny(this.getClass().getName(), ldr);
        any.invoke("setupServiceContext", svc);
        setupServiceContext(svc);
    }


    public static void setupDefaultRolesAndUsers(SmartTenant powner)
        throws CtxException
    {
        threads().addToContextLocals(SYSTEM_RUNTIME, STARTUP_DEFAULTS);
        //TODO: revisit after design of default roles and users
        RuntimeShell rshell = (RuntimeShell)powner.runtimeShell();
        String flow = "";
        SmartRole role = ManageRoles.createDefaultRole();
        if (role != null)
        {
            flow = flowFor(role.getClass());
            rshell.commitToSpace(flow, new DSpaceObject[] { (DSpaceObject)role });
        }

        ManageUsers musers = new ManageUsers();

        SmartUser usr = musers.createDefaultUser();
        if (usr != null)
        {
            flow = flowFor(usr.getClass());
            rshell.commitToSpace(flow, new DSpaceObject[] { (DSpaceObject)usr });
            System.out.println("Created default User");

            Identity iden = musers.addDefaultIdentity(usr);
            if (iden != null)
            {
                flow = flowFor(iden.getClass());
                rshell.commitToSpace(flow, new DSpaceObject[] { (DSpaceObject)iden });
                System.out.println("Created default Identity");
            }
        }
        threads().addToContextLocals(SYSTEM_RUNTIME, "");
    }
}

