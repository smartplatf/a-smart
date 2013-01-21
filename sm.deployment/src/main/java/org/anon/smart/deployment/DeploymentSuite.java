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
 * File:                org.anon.smart.deployment.DeploymentSuite
 * Author:              rsankar
 * Revision:            1.0
 * Date:                13-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A suite of deployments
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.utils.ApplicationSingleton;
import org.anon.utilities.exception.CtxException;

public abstract class DeploymentSuite<T extends Deployment> extends ApplicationSingleton implements DSuite<T>
{
    private MicroArtefacts _microArtefacts;
    private MacroDeployments<T> _macroDeployments;
    private SuiteAssistant<T> _assistant;

    protected DeploymentSuite()
    {
        _microArtefacts = new MicroArtefacts();
        _macroDeployments = new MacroDeployments<T>();
        _assistant = new SuiteAssistant<T>(this);
    }

    protected void setHandleDeployment(Class<T> cls)
        throws CtxException
    {
        _macroDeployments.setHandleDeployment(cls);
    }

    public MicroArtefacts artefacts()
    {
        return _microArtefacts;
    }

    public MacroDeployments<T> deployments()
    {
        return _macroDeployments;
    }

    public Map<String, String> deployFile(String file)
        throws CtxException
    {
        Deployer deployer = Deployer.deployers.deployerFor(file);
        return deployer.deploy(file, this);
    }

    public SuiteAssistant<T> assistant()
    {
        return _assistant;
    }

    public void enableFor(LicensedDeploymentSuite<T> ldeploy, String dep, String[] features)
        throws CtxException
    {
        T deployment = _macroDeployments.deploymentFor(dep);
        assertion().assertNotNull(deployment, "No Deployment " + dep + " to enable");

        Deployment ldeployment = ldeploy.assistant().deploymentFor(dep);
        List<String> enable = new ArrayList<String>();
        if (ldeployment == null)
        {
            ldeployment = ldeploy.deployments().addDeploymentFrom(deployment, features);
            enable.addAll(ldeployment.featureArtefacts());
        }
        else
        {
            for (int i = 0; (features != null) && (i < features.length); i++)
            {
                ldeployment.addFeatureFrom(deployment, features[i]);
                enable.addAll(deployment.featureArtefacts(features[i]));
            }
        }

        for (int i = 0; (enable != null) && (i < enable.size()); i++)
        {
            Artefact[] artefacts = _microArtefacts.artefactsForClazz(enable.get(i));
            assertion().assertNotNull(artefacts, "Wrong deployment details. No class deployed for: " + enable.get(i));
            ldeployment.addArtefacts(artefacts);
            ldeploy.deployArtefacts(artefacts);
        }
    }
}

