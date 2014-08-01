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
 * File:                org.anon.smart.secure.application.RoleCreateAction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An action to create roles pre-defined in the application
 *
 * ************************************************************
 * */

package org.anon.smart.secure.application;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.base.application.ApplicationAction;
import org.anon.smart.base.application.ApplicationDefinition;
import org.anon.smart.base.application.EnablePackageAction;
import org.anon.smart.base.application.RoleDefinition;
import org.anon.smart.base.application.PackageDefinition;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.secure.inbuilt.transition.ManageRoles;
import org.anon.smart.secure.inbuilt.data.SmartRole;

import org.anon.utilities.exception.CtxException;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

public class RoleCreateAction implements ApplicationAction
{
    public RoleCreateAction()
    {
    }

    public void doAction(ApplicationAction.appactions action, ApplicationDefinition adef, Map parms, boolean readback)
        throws CtxException
    {
        if ((!readback) && (action == ApplicationAction.appactions.enable))
        {
            assertion().assertNotNull(parms, "Need to specify the tenant and package to enable");
            assertion().assertTrue(parms.containsKey(EnablePackageAction.PACKAGE), "Need to specify the tenant and package to enable");
            assertion().assertTrue(parms.containsKey(EnablePackageAction.TENANTOBJ), "Need to specify the tenant and package to enable");
            assertion().assertTrue(parms.containsKey(EnablePackageAction.TENANTADMIN), "Need to specify the tenant and package to enable");

            String enablepkg = (String)parms.get(EnablePackageAction.PACKAGE);
            List<PackageDefinition> pkgs = adef.getPackages();
            List<PackageDefinition> epkgs = new ArrayList<PackageDefinition>();

            EnablePackageAction.getPackageDefinitions(enablepkg, pkgs, epkgs);
            assertion().assertTrue(epkgs.size() > 0, "Cannot find the package to enable.");

            SmartTenant tenant = (SmartTenant)parms.get(EnablePackageAction.TENANTOBJ);
            TenantAdmin admin = (TenantAdmin)parms.get(EnablePackageAction.TENANTADMIN);
            String flow = flowFor(SmartRole.class);

            assertion().assertNotNull(tenant, "Need the tenant to enable roles.");
            ManageRoles mr = new ManageRoles();
            List<String> roles = new ArrayList<String>();

            for (PackageDefinition pdef : epkgs)
            {
                List<String> r = pdef.getRoles();
                roles.addAll(r);
            }

            List<RoleDefinition> rldefs = adef.getRoles();
            Map<String, RoleDefinition> rdefs = new HashMap<String, RoleDefinition>();

            for (RoleDefinition r : rldefs)
            {
                if (roles.contains(r.getName()))
                    rdefs.put(r.getName(), r);
            }

            List roleobjs = new ArrayList();

            for (String rname : rdefs.keySet())
            {
                List<RoleDefinition.PermitDef> pdefs = rdefs.get(rname).getPermits();
                Map<String, String> defs = new HashMap<String, String>();
                for (RoleDefinition.PermitDef pd : pdefs)
                {
                    defs.put(pd.getURL(), pd.getPermission());
                }
                System.out.println("Creating role for: " + rname + ":" + defs);
                Object role = mr.clCreateNewRole(tenant.getRelatedLoader(), rname, defs);
                roleobjs.add(role);
            }

            admin.addTenantObjects(flow, roleobjs);
        }
    }
}

