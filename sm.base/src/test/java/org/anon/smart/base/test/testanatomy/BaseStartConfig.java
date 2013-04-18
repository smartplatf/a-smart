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
 * File:                org.anon.smart.base.test.testanatomy.BaseStartConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A config which is implemented for testing only
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.testanatomy;

import java.util.Map;
import java.util.HashMap;

import org.anon.utilities.test.PathHelper;
import org.anon.smart.base.test.ModConstants;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class BaseStartConfig implements TestModuleConfig, ModConstants
{
    private String[] _deploy;
    private String[] _tenants;
    private Map<String, String[]> _enable;
    private Map<String, String> _config;

    public BaseStartConfig(String[] deploy, String[] tenants, Map<String, String[]> enable)
    {
        _deploy = deploy;
        _tenants = tenants;
        _enable = enable;
        _config = new HashMap<String, String>();
    }

    public void addConfig(String tenant, String cfgname)
    {
        _config.put(tenant, cfgname);
    }


    public String[] deploymentFiles()
    {
        return _deploy;
    }

    public String[] tenantsToCreate()
    {
        return _tenants;
    }

    public String[] tenantsToEnableFor(String deployment)
    {
        return _enable.get(deployment);
    }

    public Object configFor(String tenant, ClassLoader ldr)
        throws CtxException
    {
        Object cfg = null;
        String clsnm = _config.get(tenant);
        if (clsnm != null)
        {
            try
            {
                Class cls = ldr.loadClass(clsnm);
                cfg = cls.newInstance();
            }
            catch (Exception e)
            {
                except().rt(e, new CtxException.Context("Exception", "Exception"));
            }
        }

        return cfg;
    }

    public String configDir()
        throws CtxException
    {
        return "config";
    }

    public boolean initTenants()
    {
        return true;
    }

    public String[] jarFiles()
    {
        String jar = PathHelper.getJar(false, SAMPLEAPP);
        return new String[] { jar };
    }
}

