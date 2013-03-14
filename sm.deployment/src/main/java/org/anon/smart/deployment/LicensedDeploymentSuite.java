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
 * File:                org.anon.smart.deployment.LicensedDeploymentSuite
 * Author:              rsankar
 * Revision:            1.0
 * Date:                19-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A suite of licensed deployments
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import org.anon.utilities.exception.CtxException;

public class LicensedDeploymentSuite<T extends Deployment> implements DSuite<T>
{
    private MicroArtefacts _licensedClazzez;
    private MacroDeployments<T> _licensedDeployments;
    private SuiteAssistant<T> _assistant;

    public LicensedDeploymentSuite()
    {
        _licensedClazzez = new MicroArtefacts();
        _licensedDeployments = new MacroDeployments<T>();
        _assistant = new SuiteAssistant<T>(this);
    }

    protected void setHandleDeployment(Class<T> cls)
        throws CtxException
    {
        _licensedDeployments.setHandleDeployment(cls);
    }

    public MicroArtefacts artefacts()
    {
        return _licensedClazzez;
    }

    public MacroDeployments<T> deployments()
    {
        return _licensedDeployments;
    }

    public SuiteAssistant<T> assistant()
    {
        return _assistant;
    }

    public void deployArtefacts(Artefact[] artefacts)
        throws CtxException
    {
        _licensedClazzez.deployArtefacts(artefacts);
    }
}

