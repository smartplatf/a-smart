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
 * File:                org.anon.smart.deployment.SuiteAssistant
 * Author:              rsankar
 * Revision:            1.0
 * Date:                19-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An assistant that works with deployment suites
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.List;

import org.anon.utilities.exception.CtxException;

public class SuiteAssistant<T extends Deployment>
{
    private DSuite<T> _suite;

    public SuiteAssistant(DSuite<T> suite)
    {
        _suite = suite;
    }

    public T addDeployment(T dep)
        throws CtxException
    {
        return _suite.deployments().addDeployment(dep);
    }

    public Artefact[] deployClazz(Class cls)
        throws CtxException
    {
        Artefact[] artefacts = _suite.artefacts().deployClazz(cls);
        return artefacts;
    }

    public Deployment deployClazz(Class cls, String dep)
        throws CtxException
    {
        Artefact[] artefacts = _suite.artefacts().deployClazz(cls);
        return _suite.deployments().addDeployment(dep, artefacts);
    }

    public T deploymentFor(String dep)
        throws CtxException
    {
        return _suite.deployments().deploymentFor(dep);
    }

    public T deploymentFor(DeploymentFilter<T> filter)
        throws CtxException
    {
        return _suite.deployments().deploymentFor(filter);
    }

    public List<Class> clazzezFor(String wild, ArtefactType type, ClassLoader ldr)
        throws CtxException
    {
        return _suite.artefacts().clazzezFor(wild, type, ldr);
    }

    public Class clazzFor(String key, ArtefactType type, ClassLoader ldr)
        throws CtxException
    {
        return _suite.artefacts().clazzFor(key, type, ldr);
    }

    public Class clazzFor(String key, String type, ClassLoader ldr)
        throws CtxException
    {
        ArtefactType atype = ArtefactType.artefactTypeFor(type);
        return clazzFor(key, atype, ldr);
    }

    public void enableFor(LicensedDeploymentSuite<T> ldeploy, String dep, String[] features)
        throws CtxException
    {
        _suite.enableFor(ldeploy, dep, features);
    }
}

