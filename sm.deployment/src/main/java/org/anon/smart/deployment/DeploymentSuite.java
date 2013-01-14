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

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.utils.ApplicationSingleton;
import org.anon.utilities.exception.CtxException;

public class DeploymentSuite<T extends Deployment> extends ApplicationSingleton
{
    private MicroArtefacts _microArtefacts;
    private MacroDeployments<T> _macroDeployments;

    protected DeploymentSuite()
    {
        _microArtefacts = new MicroArtefacts();
        _macroDeployments = new MacroDeployments<T>();
    }

    protected void setHandleDeployment(Class<T> cls)
        throws CtxException
    {
        _macroDeployments.setHandleDeployment(cls);
    }

    protected Artefact[] deployClazz(Class cls)
        throws CtxException
    {
        Artefact[] artefacts = _microArtefacts.deployClazz(cls);
        return artefacts;
    }

    protected Deployment deployClazz(Class cls, String dep)
        throws CtxException
    {
        Artefact[] artefacts = _microArtefacts.deployClazz(cls);
        return _macroDeployments.addDeployment(dep, artefacts);
    }

    protected Map<String, String> deployFile(String file)
        throws CtxException
    {
        Deployer deployer = Deployer.deployers.deployerFor(file);
        return deployer.deploy(file, this);
    }

    protected List<Class> clazzezFor(String wild, ArtefactType type, ClassLoader ldr)
        throws CtxException
    {
        return _microArtefacts.clazzezFor(wild, type, ldr);
    }

    protected Class clazzFor(String key, ArtefactType type, ClassLoader ldr)
        throws CtxException
    {
        return _microArtefacts.clazzFor(key, type, ldr);
    }

    protected void enableFor(MicroArtefacts micro, String dep)
        throws CtxException
    {
        Deployment deployment = _macroDeployments.deploymentFor(dep);
        assertion().assertNotNull(deployment, "No Deployment " + dep + " to enable");

        List<String> arts = deployment.artefacts();
        for (int i = 0; (arts != null) && (i < arts.size()); i++)
        {
            Artefact[] artefacts = _microArtefacts.artefactsForClazz(arts.get(i));
            assertion().assertNotNull(artefacts, "Wrong deployment details. No class deployed for: " + arts.get(i));
            //TODO: micro.enable(artefacts);
        }
    }
}

