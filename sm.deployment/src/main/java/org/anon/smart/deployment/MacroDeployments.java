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
 * File:                org.anon.smart.deployment.MacroDeployments
 * Author:              rsankar
 * Revision:            1.0
 * Date:                12-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * All deployments in the system
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.Map;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.utilities.exception.CtxException;

public class MacroDeployments<T extends Deployment>
{
    private Map<String, T> _deployments;
    private Class<T> _handleClass;

    MacroDeployments()
    {
        _deployments = new ConcurrentHashMap<String, T>();
    }

    void setHandleDeployment(Class<T> dep)
    {
        _handleClass = dep;
    }

    public T addDeployment(String name, Artefact[] artefacts)
        throws CtxException
    {
        T dep = _deployments.get(name);
        if (dep == null)
            dep = Deployment.deploymentFor(name, artefacts, _handleClass);
        else
            dep.addArtefacts(artefacts);
        _deployments.put(name, dep);
        return dep;
    }

    public T addDeploymentFrom(T dep, String[] features)
        throws CtxException
    {
        T ret = _deployments.get(dep.deployedName());
        if (ret == null)
        {
            ret = Deployment.deploymentFrom(dep, features, _handleClass);
            _deployments.put(ret.deployedName(), ret);
        }

        return ret;
    }

    public T deploymentFor(String dep)
    {
        return _deployments.get(dep);
    }

    public T deploymentFor(DeploymentFilter<T> filter)
        throws CtxException
    {
        T ret = null;
        for (T dep : _deployments.values())
        {
            if (filter.matches(dep))
            {
                ret = dep;
                break;
            }
        }

        return ret;
    }

    public void addDeployment(String file)
        throws CtxException
    {
    }
}

