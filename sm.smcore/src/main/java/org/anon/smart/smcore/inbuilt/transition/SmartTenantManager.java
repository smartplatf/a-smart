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

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.base.tenant.TenantsHosted;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell;
import org.anon.smart.base.tenant.shell.DeploymentShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.anatomy.SmartModuleContext;
import org.anon.smart.base.application.ApplicationSuite;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.inbuilt.events.LinkFor;
import org.anon.smart.smcore.inbuilt.events.ListEnabledFlows;
import org.anon.smart.smcore.inbuilt.events.NewTenant;
import org.anon.smart.smcore.inbuilt.events.UpdateTenant;
import org.anon.smart.smcore.inbuilt.events.NewInternalTenant;
import org.anon.smart.smcore.inbuilt.events.EnableFlow;
import org.anon.smart.smcore.inbuilt.events.InternalEnableFlow;
import org.anon.smart.smcore.inbuilt.events.AddFlowLinks;
import org.anon.smart.smcore.inbuilt.responses.ListEnabledFlowsResponse;
import org.anon.smart.smcore.inbuilt.responses.SuccessCreated;
import org.anon.smart.smcore.transition.TransitionContext;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.anatomy.AModule;
import org.anon.utilities.exception.CtxException;

public class SmartTenantManager
{
    public void newTenant(TenantAdmin dest, NewTenant tenant)
        throws CtxException
    {
        assertion().assertTrue(dest.isPlatformOwner(), "Cannot create a tenant on any other tenant other than the owner.");
        assertion().assertNotNull(tenant.tenantName(), "Cannot create a null tenant");
        CrossLinkSmartTenant ptenant = CrossLinkSmartTenant.currentTenant();
        System.out.println("Creating a tenant: " + tenant.tenantName() + ":" + this.getClass().getClassLoader() + ":" + ptenant.getName());
        SmartTenant stenant = new SmartTenant(tenant.tenantName());
        stenant.setDomain(tenant.getDomain());
        stenant.setClientOf(tenant.getClientOf());
        stenant.setControlsAdmin(tenant.getControlsAdmin());

        //get from all modules and enable not just these.
        AModule[] mods = anatomy().allModules();
        List<String> stdenable = new ArrayList<String>();
        for (int i = 0; i < mods.length; i++)
        {
            String[] std = ((SmartModuleContext)(mods[i].context())).getEnableFlows();
            for (int j = 0; (std != null) && (j < std.length); j++)
            {
                if (!stdenable.contains(std[j]))
                    stdenable.add(std[j]);
            }
        }
        //stenant.deploymentShell().enableForMe("AdminSmartFlow", new String[] { "all" }, new HashMap<String, String>());
        //stenant.deploymentShell().enableForMe("AllFlows", new String[] { "all" }, new HashMap<String, String>());
        for (int i = 0; i < stdenable.size(); i++)
        {
            String en = stdenable.get(i);
            String[] vals = en.split(":");
            String ef = en;
            List<String> ff = new ArrayList<String>();
            if ((vals != null) && (vals.length > 0))
                ef = vals[0];
            for (int j = 1; (vals != null) && (j < vals.length); j++)
                ff.add(vals[j]);

            if (ff.size() <= 0)
                ff.add("all");
            stenant.deploymentShell().enableForMe(ef, ff.toArray(new String[0]), new HashMap<String, List<String>>());
        }
        TenantAdmin admin = new TenantAdmin(tenant.tenantName(), stenant);

        //If application/package is provided, then enable that not the flow??
        if ((tenant.getEnableApplication() != null) && (tenant.getEnableApplication().length() > 0))
        {
            //assumption is that the appln is always enabled when creating, after this it shd be upgrade
            //need to add that event. Cannot separate flows.
            assertion().assertNotNull(tenant.getEnablePackage(), "Cannot enable a null package.");
            assertion().assertTrue(tenant.getEnablePackage().length() > 0, "Cannot enable a null package.");
            System.out.println("Typing to enable application for: " + tenant.getEnableApplication() + ":" + tenant.getEnablePackage());
            ApplicationSuite.enableApplication(tenant.getEnableApplication(), tenant.getEnablePackage(), stenant, admin, false);
        }
        else
        {
            String flow = tenant.getEnableFlow();
            List<String> features = tenant.getEnableFeatures();
            if ((flow != null) && (flow.length() > 0) && (features != null))
            {
                System.out.println("Typing to enable flow for: " + flow + ":" + features);
                stenant.deploymentShell().enableForMe(flow, features.toArray(new String[0]), new HashMap<String, List<String>>());
            }
        }

        ClassLoader ldr = stenant.getRelatedLoader();
        DefaultObjectsManager.createDefaultObjects(admin, stenant, ldr);

        SuccessCreated created = new SuccessCreated(tenant.tenantName());
    }

    public void updateTenant(TenantAdmin dest, UpdateTenant tenant)
        throws CtxException
    {
        assertion().assertTrue(dest.isPlatformOwner(), "Cannot update a tenant on any other tenant other than the owner.");
        assertion().assertNotNull(tenant.getTenant(), "Cannot update a null tenant");
        CrossLinkSmartTenant ptenant = CrossLinkSmartTenant.currentTenant();
        System.out.println("Updating a tenant: " + tenant.getTenant() + ":" + this.getClass().getClassLoader() + ":" + ptenant.getName());
        SmartTenant stenant = TenantsHosted.tenantFor(tenant.getTenant());
        stenant.setDomain(tenant.getDomains());

        CrossLinkRuntimeShell shell = new CrossLinkRuntimeShell(ptenant.runtimeShell());
        TenantAdmin tentxn = (TenantAdmin)shell.lookupFor("AdminSmartFlow", "TenantAdmin", stenant.getName());
        tentxn.setupTenant(stenant);
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if (ctx != null)
            ctx.atomicity().includeData((SmartData)tentxn);
        
        SuccessCreated created = new SuccessCreated(tenant.getDomains());
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
            System.out.println("Trying to enable flow for: " + flow + ":" + features);
            Map<String, List<String>> linked = new HashMap<String, List<String>>();
            if (enable.getLinks() != null)
            {
                for (LinkFor l : enable.getLinks())
                {
                    List<String> to = linked.get(l.getName());
                    if (to == null)
                        to = new ArrayList<String>();
                    to.add(l.getTo());
                    linked.put(l.getName(), to);
                }
            }
            stenant.deploymentShell().enableForMe(flow, features.toArray(new String[0]), linked);
        }
        SuccessCreated created = new SuccessCreated(enable.getEnableFlow());
    }

    public void addLinksToFlow(TenantAdmin adm, AddFlowLinks lnk)
        throws CtxException
    {
        assertion().assertNotNull(lnk.getTenant(), "Cannot add links for a null tenant");
        assertion().assertNotNull(lnk.getEnableFlow(), "Cannot add links for  null flow.");
        assertion().assertNotNull(lnk.getLinks(), "Cannot add null links.");
        assertion().assertTrue((lnk.getLinks().size() > 0), "Cannot add 0 links.");

        SmartTenant stenant = TenantsHosted.tenantFor(lnk.getTenant());
        //CrossLinkSmartTenant stenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkSmartTenant ptenant = TenantsHosted.crosslinkedPlatformOwner();
        //TODO: a hack currently to get this working.
        CrossLinkRuntimeShell shell = new CrossLinkRuntimeShell(ptenant.runtimeShell());
        Object tentxn = shell.lookupFor("AdminSmartFlow", "TenantAdmin", lnk.getTenant());
        TenantAdmin tadmin = (TenantAdmin)tentxn;
        tadmin.setupTenant(stenant); //set this up, else it will try to commit the SmartOwner
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if (ctx != null)
            ctx.atomicity().includeData((SmartData)tentxn);
        //TODO: a hack, will have to be removed when the related data is implemented.

        String flow = lnk.getEnableFlow();
        if ((flow != null) && (flow.length() > 0))
        {
            Map<String, List<String>> linked = new HashMap<String, List<String>>();
            if (lnk.getLinks() != null)
            {
                for (LinkFor l : lnk.getLinks())
                {
                    List<String> to = linked.get(l.getName());
                    if (to == null)
                        to = new ArrayList<String>();

                    to.add(l.getTo());
                    linked.put(l.getName(), to);
                }
            }
            System.out.println("Trying to add links flow for: " + flow + ":" + linked);
            stenant.deploymentShell().addLinksFor(flow, linked);
        }
        SuccessCreated created = new SuccessCreated(" Links for " + lnk.getEnableFlow());
    }
    
    public void listEnabledFlows(TenantAdmin dest, ListEnabledFlows event)
        throws CtxException
    {
        assertion().assertNotNull(dest, "Cannot List enabled flows: Tenant is NULL");
        SmartTenant stenant = TenantsHosted.tenantFor(dest.tenantName());
        Collection<String> flows = stenant.listEnableFlows();
        ListEnabledFlowsResponse resp = new ListEnabledFlowsResponse(flows);
        System.out.println("Enabled Flows For Tenant:"+dest.tenantName()+":"+flows);
    }

    //The below two methods are exposed to all tenants, we have to control it via security when we
    //include security.
    //links in the format flow.name=object.attribute
    public void enableFlowService(String tenant, String domain, String clientOf, String flow, List<String> features, Map<String, String> links)
        throws CtxException
    {
        System.out.println(">>>>>> Called enableFlowService: "  + tenant + ":" + flow + ":" + features + ":" + links);
        CrossLinkSmartTenant ten = CrossLinkSmartTenant.currentTenant();
        assertion().assertTrue(ten.controlsAdmin(), "Cannot enable a flow for a tenant without having admin permissions");
        CrossLinkSmartTenant ptenant = TenantsHosted.crosslinkedPlatformOwner();
        DefaultObjectsManager.setupInternalServiceContext("enableFlowService", ptenant.getRelatedLoader());
        CrossLinkRuntimeShell shell = new CrossLinkRuntimeShell(ptenant.runtimeShell());
        Object tentxn = shell.lookupFor("AdminSmartFlow", "TenantAdmin", ptenant.getName());
        CrossLinkSmartTenant stenant = TenantsHosted.crosslinkedTenantFor(tenant);
        if (stenant == null)
        {
            //post a NewInternalTenant event, else post the enableflow event
            // assumption here is that if this is the first tenant, no links can be present, since there is no flow to linkto.
            newTenantService(tenant, domain, clientOf, flow, features);
        }
        else
        {
            InternalEnableFlow evt = new InternalEnableFlow(tenant, flow, features, links, tentxn);
            SuccessCreated created = new SuccessCreated("Posted a new Enable Flow message. Please check after sometime.");
        }
    }


    public void newTenantService(String name, String domain, String clientOf, String enableflow, List<String> features)
        throws CtxException
    {
        System.out.println(">>>>>> Called newTenantService: " + name + ":" + domain + ":" + clientOf + ":" + enableflow + ":" + features);
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        assertion().assertTrue(tenant.controlsAdmin(), "Cannot create a tenant without having admin permissions");
        CrossLinkSmartTenant ptenant = TenantsHosted.crosslinkedPlatformOwner();
        DefaultObjectsManager.setupInternalServiceContext("newTenantService", ptenant.getRelatedLoader());
        CrossLinkRuntimeShell shell = new CrossLinkRuntimeShell(ptenant.runtimeShell());
        Object tentxn = shell.lookupFor("AdminSmartFlow", "TenantAdmin", ptenant.getName());
        NewInternalTenant evt = new NewInternalTenant(name, enableflow, domain, clientOf, features, tentxn);
        SuccessCreated created = new SuccessCreated("Posted a new tenant creation message. Please check after sometime.");
    }

}

