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

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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

    public Artefact[] deployClazz(String dep, Class cls)
        throws CtxException
    {
        MicroArtefacts marts = _suite.artefacts(dep);
        if (marts == null)
            marts = _suite.artefactsCreate(dep);
        Artefact[] artefacts = marts.deployClazz(cls);
        return artefacts;
    }

    public Deployment deployClazz(Class cls, String dep)
        throws CtxException
    {
        MicroArtefacts marts = _suite.artefacts(dep);
        if (marts == null)
            marts = _suite.artefactsCreate(dep);
        Artefact[] artefacts = marts.deployClazz(cls);
        return _suite.deployments().addDeployment(dep, artefacts);
    }

    public T deploymentFor(String dep)
        throws CtxException
    {
        return _suite.deployments().deploymentFor(dep);
    }

    public List<T> deploymentFor(DeploymentFilter<T> filter)
        throws CtxException
    {
        return _suite.deployments().deploymentFor(filter);
    }

    public List<Class> clazzezFor(String dep, String wild, ArtefactType type, ClassLoader ldr)
        throws CtxException
    {
        Deployment d = deploymentFor(dep);
        MicroArtefacts marts = _suite.artefacts(dep);
        if (marts != null)
            return marts.clazzezFor(wild, type, ldr, d);
        return new ArrayList<Class>();
    }

    public Class clazzFor(String dep, String key, ArtefactType type, ClassLoader ldr)
        throws CtxException
    {
        Deployment d = deploymentFor(dep);
        MicroArtefacts marts = _suite.artefacts(dep);
        if (marts != null)
            return marts.clazzFor(key, type, ldr, d);
        return null;
    }

    public Class clazzFor(String dep, String key, String type, ClassLoader ldr)
        throws CtxException
    {
        ArtefactType atype = ArtefactType.artefactTypeFor(type);
        return clazzFor(dep, key, atype, ldr);
    }

    public Class clazzFor(String dep, String clsname)
        throws CtxException
    {
        Deployment d = deploymentFor(dep);
        MicroArtefacts marts = _suite.artefacts(dep);
        if (marts != null)
        {
            Artefact[] artefacts = marts.artefactsForClazz(clsname);
            if ((artefacts != null) && (artefacts.length > 0))
                return artefacts[0].getClazz();
        }
        return null;

    }

    public String[] linkFor(String dep, Class cls, String part)
        throws CtxException
    {
        Deployment d = deploymentFor(dep);
        MicroArtefacts marts = _suite.artefacts(dep);
        if (marts != null)
        {
            String clsname = cls.getName();
            Artefact[] artefacts = marts.artefactsForClazz(clsname);
            if ((artefacts != null) && (artefacts.length > 0))
                return artefacts[0].expandLinks(part);
        }
        return null;
    }

    public Artefact[] enableFor(LicensedDeploymentSuite<T> ldeploy, String dep, String[] features, Map<String, List<String>> links)
        throws CtxException
    {
        return _suite.enableFor(ldeploy, dep, features, links);
    }

    public List<String> allDeployments()
        throws CtxException
    {
        return _suite.deployments().allDeployments();
    }
}

