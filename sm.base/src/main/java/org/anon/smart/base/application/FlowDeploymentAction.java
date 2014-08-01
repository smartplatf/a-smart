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
 * File:                org.anon.smart.base.application.FlowDeploymentAction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An application action to deploy flows
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.util.Map;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.deployment.MacroDeployer;
import org.anon.smart.base.flow.FlowConstants;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class FlowDeploymentAction implements ApplicationAction, FlowConstants
{
    public FlowDeploymentAction()
    {
    }

    public void doAction(ApplicationAction.appactions action, ApplicationDefinition adef, Map parms, boolean readback)
        throws CtxException
    {
        //assumption is that the persisted flow deployments will pick up and deploy 
        //these files and hence we do not have to deploy.
        if (!readback && action.equals(ApplicationAction.appactions.deploy))
        {
            List<FlowDefinition> flows = adef.getFlows();
            for (int i = 0; (flows != null) && (i < flows.size()); i++)
            {
                FlowDefinition fdef = flows.get(i);
                String soaName = fdef.getName();
                String soaFile = fdef.getSoa();
                File f = new File(fdef.getFile());
                assertion().assertTrue(f.exists(), "Cannot deploy a file not present on the server." + fdef.getFile());
                List<String> lib = fdef.getLibraries();
                if (lib == null)
                    lib = new ArrayList<String>();
                String[] jars = new String[lib.size() + 1];
                for (int j = 0; j < lib.size(); j++)
                {
                    f = new File(lib.get(j));
                    assertion().assertTrue(f.exists(), "Cannot deploy a file not present on the server." + lib.get(j));
                    jars[j] = lib.get(j);
                }
                jars[lib.size()] = fdef.getFile();
                for (int k = 0; k < jars.length; k++)
                    System.out.println("Deploying: " + jars[k]);
                MacroDeployer.deployFile(FLOW, soaFile, jars);
            }
        }
    }
}

