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

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.base.flow.PrimeTypeFilter;
import org.anon.smart.base.flow.ClassTypeFilter;
import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.deployment.LicensedDeploymentSuite;

import org.anon.utilities.exception.CtxException;

public class DeploymentShell implements SmartShell, FlowConstants
{
    private ClassLoader _loader;
    private LicensedDeploymentSuite<FlowDeployment> _licensed;

    public DeploymentShell(ClassLoader ldr)
    {
        _licensed = new LicensedDeploymentSuite<FlowDeployment>();
        _loader = ldr;
    }

    public Class deployment(String name, String type)
        throws CtxException
    {
        ArtefactType atype = ArtefactType.artefactTypeFor(type);
        assertion().assertNotNull(atype, "Cannot recognized artefactType: " + type);
        return _licensed.assistant().clazzFor(name, atype, _loader);
    }

    public Class eventClass(String name)
        throws CtxException
    {
        return deployment(name, EVENT);
    }

    public Class dataClass(String name)
        throws CtxException
    {
        Class cls = deployment(name, DATA);
        if (cls == null)
            cls = deployment(name, PRIMEDATA);
        return cls;
    }

    public Class primeClass(String name)
        throws CtxException
    {
        return deployment(name, PRIMEDATA);
    }

    public List<Class> searchDeployment(String wild, String type)
        throws CtxException
    {
        ArtefactType atype = ArtefactType.artefactTypeFor(type);
        assertion().assertNotNull(atype, "Cannot recognized artefactType: " + type);
        return _licensed.assistant().clazzezFor(wild, atype, _loader);
    }

    public FlowDeployment deploymentFor(String flow)
        throws CtxException
    {
        return (FlowDeployment)_licensed.assistant().deploymentFor(flow);
    }

    public FlowDeployment flowForPrimeType(String name)
        throws CtxException
    {
        return (FlowDeployment)_licensed.assistant().deploymentFor(new PrimeTypeFilter(name));
    }

    public FlowDeployment flowForType(String name)
        throws CtxException
    {
        return (FlowDeployment)_licensed.assistant().deploymentFor(new ClassTypeFilter(name));
    }

    public void initializeShell()
        throws CtxException
    {
    }

    public void cleanup()
        throws CtxException
    {
    }
}

