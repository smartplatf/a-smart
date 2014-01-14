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

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class LicensedDeploymentSuite<T extends Deployment> implements DSuite<T>
{
    private Map<String, MicroArtefacts> _licensedClazzez;
    private MacroDeployments<T> _licensedDeployments;
    private SuiteAssistant<T> _assistant;

    public LicensedDeploymentSuite()
    {
        _licensedClazzez = new ConcurrentHashMap<String, MicroArtefacts>();
        _licensedDeployments = new MacroDeployments<T>();
        _assistant = new SuiteAssistant<T>(this);
    }

    public void setHandleDeployment(Class<T> cls)
        throws CtxException
    {
        _licensedDeployments.setHandleDeployment(cls);
    }

    public MicroArtefacts artefacts(String dep)
    {
        return _licensedClazzez.get(dep);
    }

    public MicroArtefacts artefactsCreate(String dep)
    {
        MicroArtefacts marts = _licensedClazzez.get(dep);
        if (marts == null)
            marts = new MicroArtefacts();
        _licensedClazzez.put(dep, marts);
        return marts;
    }

    public MacroDeployments<T> deployments()
    {
        return _licensedDeployments;
    }

    public SuiteAssistant<T> assistant()
    {
        return _assistant;
    }

    public void deployArtefacts(String dep, Artefact[] artefacts, Map<String, List<String>> links)
        throws CtxException
    {
        MicroArtefacts m = _licensedClazzez.get(dep);
        if (m == null)
            m = new MicroArtefacts();
        m.deployArtefacts(artefacts, links);
        _licensedClazzez.put(dep, m);
    }

    public Artefact[] enableFor(LicensedDeploymentSuite<T> ldeploy, String dep, String[] features, Map<String, List<String>> links)
        throws CtxException
    {
        except().te(this, "Enabling from an already licensed suite is not supported");
        return null;
    }
}

