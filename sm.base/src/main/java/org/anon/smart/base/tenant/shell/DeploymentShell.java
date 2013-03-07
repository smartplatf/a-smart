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
 * File:                org.anon.smart.base.tenant.shell.DeploymentShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                13-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A shell where active deployments for this tenant is stored
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import java.util.List;

import org.anon.smart.deployment.MicroArtefacts;
import org.anon.smart.deployment.ArtefactType;

import org.anon.utilities.exception.CtxException;

public class DeploymentShell
{
    private ClassLoader _loader;
    private MicroArtefacts _enabled;

    public DeploymentShell(ClassLoader ldr)
    {
        _enabled = new MicroArtefacts();
        _loader = ldr;
    }

    public Class deployment(String name, ArtefactType type)
        throws CtxException
    {
        return _enabled.clazzFor(name, type, _loader);
    }

    public List<Class> searchDeployment(String wild, ArtefactType type)
        throws CtxException
    {
        return _enabled.clazzezFor(wild, type, _loader);
    }
}

