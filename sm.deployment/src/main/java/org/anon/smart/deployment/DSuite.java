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
 * File:                org.anon.smart.deployment.DSuite
 * Author:              rsankar
 * Revision:            1.0
 * Date:                19-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A deployment suite implementation
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.Map;

import org.anon.utilities.exception.CtxException;

public interface DSuite<T extends Deployment>
{
    public MicroArtefacts artefacts(String dep);
    public MicroArtefacts artefactsCreate(String dep);
    public MacroDeployments<T> deployments();
    public Artefact[] enableFor(LicensedDeploymentSuite<T> ldeploy, String dep, String[] features, Map<String, String> links)
        throws CtxException;
}

