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
 * File:                org.anon.smart.smcore.inbuilt.transition.DeploymentManager
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a manager that deploys
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.base.flow.FlowDeploymentSuite;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.deployment.MacroDeployer;
import org.anon.smart.smcore.inbuilt.events.DeployEvent;
import org.anon.smart.smcore.inbuilt.events.ListDeployments;
import org.anon.smart.smcore.inbuilt.responses.SuccessCreated;
import org.anon.smart.smcore.inbuilt.responses.DeploymentList;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.loader.RelatedLoader;
import org.anon.utilities.exception.CtxException;

public class DeploymentManager implements FlowConstants
{
    public DeploymentManager()
    {
    }

    public void deployJar(TenantAdmin owner, DeployEvent deploy)
        throws CtxException
    {
        assertion().assertTrue(owner.isPlatformOwner(), "Cannot deploy on a tenant that is not the owner of the platform");
        assertion().assertNotNull(deploy.getJar(), "Cannot deploy a null jar.");
        File f = new File(deploy.getJar());
        assertion().assertTrue(f.exists(), "Cannot deploy a file not present on the server.");
        assertion().assertNotNull(deploy.getFlowSoa(), "Cannot deploy a null flow.");
        //RelatedLoader ldr = (RelatedLoader)this.getClass().getClassLoader();
        //ldr.addJar(deploy.getJar());
        MacroDeployer.deployFile(FLOW, deploy.getFlowSoa(), new String[] { deploy.getJar() });
        //TODO: if there is an error, this does not do anything
        SuccessCreated resp = new SuccessCreated(deploy.getJar());
    }

    public DeploymentList retrieveDeployments(ListDeployments lst)
        throws CtxException
    {
        List<String> deps = new ArrayList<String>();
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        if ((!tenant.isPlatformOwner()) || (lst.getFlow() != null))
        {
            assertion().assertNotNull(lst.getFlow(), "Need to provide a flow for which to list deployments.");
            assertion().assertNotNull(lst.getDType(), "Need to provide the deployment type to list. ");

            CrossLinkDeploymentShell dshell = tenant.deploymentShell();
            CrossLinkFlowDeployment dep = dshell.deploymentFor(lst.getFlow());
            deps = dep.getDeploymentFor(lst.getDType());
        }
        else
        {
            deps = FlowDeploymentSuite.getAllDeployments();
        }

        return new DeploymentList(lst.getDType(), deps);
    }
}

