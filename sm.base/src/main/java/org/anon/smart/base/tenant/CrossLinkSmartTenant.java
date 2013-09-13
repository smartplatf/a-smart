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
 * File:                org.anon.smart.base.tenant.CrossLinkSmartTenant
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A crosslink for smart tenant
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import java.util.Set;

import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;

import org.anon.utilities.loader.RelatedUtils;
import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkSmartTenant extends CrossLinker
{
    public CrossLinkSmartTenant(Object obj)
    {
        super(obj);
    }

    public void cleanup()
        throws CtxException
    {
        linkMethod("cleanup");
    }

    public ClassLoader getRelatedLoader()
        throws CtxException
    {
        return (ClassLoader)linkMethod("getRelatedLoader");
    }

    public String getName()
        throws CtxException
    {
        return (String)linkMethod("getName");
    }

    public Object dataShellFor(String spacemodel)
        throws CtxException
    {
        return linkMethod("dataShellFor", spacemodel);
    }

    public Object runtimeShell()
        throws CtxException
    {
        return linkMethod("runtimeShell");
    }

    public CrossLinkDeploymentShell deploymentShell()
        throws CtxException
    {
        Object shell = linkMethod("deploymentShell");
        return new CrossLinkDeploymentShell(shell);
    }

    public static CrossLinkSmartTenant currentTenant()
        throws CtxException
    {
        Object tenant = RelatedUtils.getRelatedObjectForClass(CrossLinkSmartTenant.class);
        return new CrossLinkSmartTenant(tenant);
    }

    public boolean isPlatformOwner()
        throws CtxException
    {
        Boolean b = (Boolean)linkMethod("isPlatformOwner");
        return ((b != null) && b.booleanValue());
    }

    public boolean controlsAdmin()
        throws CtxException
    {
        Boolean b = (Boolean)linkMethod("controlsAdmin");
        return ((b != null) && b.booleanValue());
    }

	public Set<String> listEnableFlows()
	    throws CtxException
	{
        return (Set<String>)linkMethod("listEnableFlows");
    }
}

