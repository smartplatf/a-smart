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
 * File:                org.anon.smart.deployment.DeploymentService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                14-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A service that works with deployment suites
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

public class DeploymentService
{
    private DeploymentService()
    {
    }

    /*
    public static Artefact[] deployClazz(Class cls)
        throws CtxException
    {
        DeploymentSuite suite = getAppInstance();
        Artefact[] artefacts = _microArtefacts.deployClazz(cls);
        return artefacts;
    }

    Deployment deployClazz(Class cls, String dep)
        throws CtxException
    {
        Artefact[] artefacts = _microArtefacts.deployClazz(cls);
        return _macroDeployments.addDeployment(dep, artefacts);
    }

    Map<String, String> deployFile(String file)
        throws CtxException
    {
        Deployer deployer = Deployer.deployers.deployerFor(file);
        return deployer.deploy(file, this);
    }
    */
}

