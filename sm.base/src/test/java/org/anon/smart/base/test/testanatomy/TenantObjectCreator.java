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
 * File:                org.anon.smart.base.test.testanatomy.TenantObjectCreator
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A runnable that creates objects in a given tenant
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.testanatomy;

import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;

import org.anon.utilities.exception.CtxException;

public class TenantObjectCreator implements Runnable
{
    private CrossLinkSmartTenant _tenant;
    private ClassLoader _loader;
    private TestTenantConfig _config;
    private String[] _deploys;
    private RuntimeShell _runtime;

    public TenantObjectCreator(Object tenant, Object config, String[] deps)
        throws CtxException
    {
        _tenant = new CrossLinkSmartTenant(tenant);
        _loader = _tenant.getRelatedLoader();
        _config = (TestTenantConfig)config;
        _deploys = deps;
        _runtime = (RuntimeShell)_tenant.runtimeShell();
    }

    public void run()
    {
        try
        {
            for (int i = 0; i < _deploys.length; i++)
            {
                DSpaceObject[] spaceobjs = _config.objectsFor(_deploys[i]);
                for (int k = 0; (spaceobjs != null) && (k < spaceobjs.length); k++)
                    System.out.println("Creating objects for: " + spaceobjs[k] + " in " + _deploys[i]);
                _runtime.commitToSpace(_deploys[i], spaceobjs);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

