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
 * File:                org.anon.smart.smcore.inbuilt.transition.SmartTenantManager
 * Author:              rsankar
 * Revision:            1.0
 * Date:                27-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A manager for smart tenants
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import java.util.List;

import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.base.tenant.TenantsHosted;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.inbuilt.events.NewTenant;
import org.anon.smart.smcore.inbuilt.events.EnableFlow;
import org.anon.smart.smcore.inbuilt.responses.SuccessCreated;
import org.anon.smart.smcore.transition.TransitionContext;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SmartTenantManager
{
    public void newTenant(TenantAdmin dest, NewTenant tenant)
        throws CtxException
    {
        assertion().assertTrue(dest.isPlatformOwner(), "Cannot create a tenant on any other tenant other than the owner.");
        assertion().assertNotNull(tenant.tenantName(), "Cannot create a null tenant");
        System.out.println("Creating a tenant: " + tenant.tenantName());
        SmartTenant stenant = new SmartTenant(tenant.tenantName());
        stenant.deploymentShell().enableForMe("AdminSmartFlow", new String[] { "all" });
        stenant.deploymentShell().enableForMe("AllFlows", new String[] { "all" });
        TenantAdmin admin = new TenantAdmin(tenant.tenantName(), stenant);
        String flow = tenant.getEnableFlow();
        List<String> features = tenant.getEnableFeatures();
        if ((flow != null) && (flow.length() > 0) && (features != null))
        {
            System.out.println("Typing to enable flow for: " + flow + ":" + features);
            stenant.deploymentShell().enableForMe(flow, features.toArray(new String[0]));
        }
        SuccessCreated created = new SuccessCreated(tenant.tenantName());
    }

    public void enableFlow(TenantAdmin dest, EnableFlow enable)
        throws CtxException
    {
        assertion().assertNotNull(enable.getTenant(), "Cannot enable flow for a null tenant");
        assertion().assertNotNull(enable.getEnableFlow(), "Cannot enable a null flow.");

        SmartTenant stenant = TenantsHosted.tenantFor(enable.getTenant());
        //CrossLinkSmartTenant stenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkSmartTenant ptenant = TenantsHosted.crosslinkedPlatformOwner();
        //TODO: a hack currently to get this working.
        CrossLinkRuntimeShell shell = new CrossLinkRuntimeShell(ptenant.runtimeShell());
        Object tentxn = shell.lookupFor("AdminSmartFlow", "TenantAdmin", enable.getTenant());
        TenantAdmin tadmin = (TenantAdmin)tentxn;
        tadmin.setupTenant(stenant); //set this up, else it will try to commit the SmartOwner
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if (ctx != null)
            ctx.atomicity().includeData((SmartData)tentxn);
        //TODO: a hack, will have to be removed when the related data is implemented.

        String flow = enable.getEnableFlow();
        List<String> features = enable.getEnableFeatures();
        if ((flow != null) && (flow.length() > 0) && (features != null))
        {
            System.out.println("Typing to enable flow for: " + flow + ":" + features);
            stenant.deploymentShell().enableForMe(flow, features.toArray(new String[0]));
        }
        SuccessCreated created = new SuccessCreated(enable.getEnableFlow());
    }
}

