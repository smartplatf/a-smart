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
 * File:                org.anon.smart.base.application.EnablePackageAction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An enable action for a tenant
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.base.tenant.shell.DeploymentShell;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class EnablePackageAction implements ApplicationAction
{
    public static final String TENANT = "TENANT";
    public static final String PACKAGE = "PACKAGE";
    public static final String DEPLOYMENTSHELL = "DEPSHELL";
    public static final String TENANTOBJ = "TENANTOBJ";
    public static final String TENANTADMIN = "TENANTADMIN";

    private class enableDet
    {
        String flow;
        List<String> features;
        Map<String, List<String>> linked;

        enableDet(PackageDefinition.EnabledDefinition edef)
        {
            flow = edef.getEnableFlow();
            features = new ArrayList<String>();
            List<String> f = edef.getEnabledFeatures();
            if (f != null) features.addAll(f);
            linked = new HashMap<String, List<String>>();
            addLinks(edef);
        }

        private void addLinks(PackageDefinition.EnabledDefinition edef)
        {
            List<PackageDefinition.LinkDefinition> lnks = edef.getLinks();
            for (int i = 0; (lnks != null) && (i < lnks.size()); i++)
                linked.put(lnks.get(i).getLinkName(), lnks.get(i).getArray());
        }

        void extendFlow(PackageDefinition.EnabledDefinition edef)
        {
            List<String> a = edef.getAddFeatures();
            if (a != null) features.addAll(a);
            addLinks(edef);
        }
    }

    public EnablePackageAction()
    {
    }

    public static void getPackageDefinitions(String epkg, List<PackageDefinition> pkgs, List<PackageDefinition> epkgs)
        throws CtxException
    {
        PackageDefinition pdef = null;
        for (int i = 0; (pkgs != null) && (i < pkgs.size()); i++)
        {
            if (pkgs.get(i).getName().equals(epkg))
            {
                pdef = pkgs.get(i);
                break;
            }
        }

        if (pdef != null)
        {
            epkgs.add(pdef);
            if (pdef.getExtend() != null)
                getPackageDefinitions(pdef.getExtend(), pkgs, epkgs);
        }
    }

    private List<enableDet> unravel(List<PackageDefinition> epkgs, String tenant)
        throws CtxException
    {
        Map<String, enableDet> defs = new HashMap<String, enableDet>();
        List<String> enableOrder = new ArrayList<String>();

        for (int i = (epkgs.size() - 1); i >= 0; i--)
        {
            PackageDefinition def = epkgs.get(i);
            List<PackageDefinition.EnabledDefinition> edefs = def.getEnableFlows();
            for (int j = 0; (edefs != null) && j < edefs.size(); j++)
            {
                PackageDefinition.EnabledDefinition edef = edefs.get(j);
                if ((edef.getTenant() == null) || (edef.getTenant().equals(tenant)))
                {
                    enableOrder.add(edef.getEnableFlow());
                    enableDet d = defs.get(edef.getEnableFlow());
                    if (d == null)
                        d = new enableDet(edef);
                    else
                        d.extendFlow(edef);

                    defs.put(edef.getEnableFlow(), d);
                }
            }
        }

        List<enableDet> ret = new ArrayList<enableDet>();
        for (int i = 0; i < enableOrder.size(); i++)
            ret.add(defs.get(enableOrder.get(i)));

        return ret;
    }

    public void doAction(ApplicationAction.appactions action, ApplicationDefinition adef, Map parms, boolean readback)
        throws CtxException
    {
        if ((!readback) && (action == ApplicationAction.appactions.enable))
        {
            //only for enabling
            assertion().assertNotNull(parms, "Need to specify the tenant and package to enable");
            assertion().assertTrue(parms.containsKey(PACKAGE), "Need to specify the tenant and package to enable");
            assertion().assertTrue(parms.containsKey(TENANT), "Need to specify the tenant and package to enable");
            assertion().assertTrue(parms.containsKey(DEPLOYMENTSHELL), "Need to specify the tenant and package to enable");

            String enablepkg = (String)parms.get(PACKAGE);
            List<PackageDefinition> pkgs = adef.getPackages();
            List<PackageDefinition> epkgs = new ArrayList<PackageDefinition>();

            getPackageDefinitions(enablepkg, pkgs, epkgs);
            assertion().assertTrue(epkgs.size() > 0, "Cannot find the package to enable.");

            String tenant = (String)parms.get(TENANT);
            List<enableDet> enable = unravel(epkgs, tenant);
            DeploymentShell dshell = (DeploymentShell)parms.get(DEPLOYMENTSHELL);
            for (int i = 0; (dshell != null) && (i < enable.size()); i++)
            {
                enableDet d = enable.get(i);
                System.out.println("Enabling: " + d.flow + ":" + d.features + ":" + d.linked);
                dshell.enableForMe(d.flow, d.features.toArray(new String[0]), d.linked);
            }
        }
    }
}

