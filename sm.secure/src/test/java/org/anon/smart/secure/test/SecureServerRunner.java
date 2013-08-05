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
 * File:                org.anon.smart.secure.test.SecureServerRunner
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A runner for the secure server
 *
 * ************************************************************
 * */

package org.anon.smart.secure.test;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.smcore.test.StartCoreServerRunner;
import org.anon.smart.base.test.testanatomy.BaseStartConfig;

public class SecureServerRunner extends StartCoreServerRunner
{
    public SecureServerRunner(boolean start, int port)
    {
        super(start, port);
    }

    @Override
    protected String[] tenants()
    {
        return new String[0];
    }

    protected void addTenantConfigs(String[] tenants, BaseStartConfig cfg)
    {
    }

    @Override
    protected String[] getStartOrder()
    {
        String[] comps = new String[] { 
            "org.anon.smart.smcore.anatomy.SMCoreModule", 
            "org.anon.smart.secure.anatomy.SecureModule", 
            "org.anon.smart.base.test.testanatomy.TestModule" 
        };

        return comps;
    }


    protected BaseStartConfig getConfig()
    {
        String[] tens = tenants();
        Map<String, String[]> enable = new HashMap<String, String[]>();
        TestSecureConfig cfg = new TestSecureConfig(new String[0], tens, enable, _port);
        addTenantConfigs(tens, cfg);
        return cfg;
    }

}

