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
 * File:                org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                16-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A crosslink for deployment shell
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import java.util.List;

import org.anon.smart.base.flow.CrossLinkFlowDeployment;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkDeploymentShell extends CrossLinker
{
    public CrossLinkDeploymentShell(Object obj)
    {
        super(obj);
    }

    public void cleanup()
        throws CtxException
    {
        linkMethod("cleanup");
    }

    public Class deployment(String name, String type)
        throws CtxException
    {
        return (Class)linkMethod("deployment", name, type);
    }

    public List<Class> searchDeployment(String wild, String type)
        throws CtxException
    {
        return (List<Class>)linkMethod("searchDeployment", wild, type);
    }

    public CrossLinkFlowDeployment deploymentFor(String flow)
        throws CtxException
    {
        Object dep = linkMethod("deploymentFor", flow);
        return new CrossLinkFlowDeployment(dep);
    }

    public CrossLinkFlowDeployment flowForPrimeType(String name)
        throws CtxException
    {
        Object obj = linkMethod("flowForPrimeType", name);
        return new CrossLinkFlowDeployment(obj);
    }

    public CrossLinkFlowDeployment flowForType(String name)
        throws CtxException
    {
        Object obj = linkMethod("flowForType", name);
        return new CrossLinkFlowDeployment(obj);
    }

    public List<Class> transitionsFor(String prime, String event)
        throws CtxException
    {
        return (List<Class>)linkMethod("transitionsFor", prime, event);
    }
}

