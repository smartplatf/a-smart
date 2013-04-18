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

import static org.anon.utilities.services.ServiceLocator.*;

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

    public List<Class> searchDeployment(String dep, String wild, String type)
        throws CtxException
    {
        return (List<Class>)linkMethod("searchDeployment", dep, wild, type);
    }

    public CrossLinkFlowDeployment deploymentFor(String flow)
        throws CtxException
    {
        Object dep = linkMethod("deploymentFor", flow);
        assertion().assertNotNull(dep, "There is no deployment found for: " + dep);
        return new CrossLinkFlowDeployment(dep);
    }

    public CrossLinkFlowDeployment flowForPrimeType(String flow, String name)
        throws CtxException
    {
        Object obj = linkMethod("flowForPrimeType", flow, name);
        assertion().assertNotNull(obj, "There is no deployment found for: " + flow + ":" + name);
        return new CrossLinkFlowDeployment(obj);
    }

    public CrossLinkFlowDeployment flowForType(String name)
        throws CtxException
    {
        Object obj = linkMethod("flowForType", name);
        assertion().assertNotNull(obj, "There is no deployment found for: " + name);
        return new CrossLinkFlowDeployment(obj);
    }

    public List<Class> transitionsFor(String dep, String prime, String event, String extra)
        throws CtxException
    {
        if (extra == null)
            extra = "";
        return (List<Class>)linkMethod("transitionsFor", dep, prime, event, extra);
    }

    public Class primeClass(String dep, String type)
        throws CtxException
    {
        return (Class)linkMethod("primeClass", dep, type);
    }

    public void enableForMe(String name, String[] features)
        throws CtxException
    {
        linkMethod("enableForMe", name, features);
    }
}

