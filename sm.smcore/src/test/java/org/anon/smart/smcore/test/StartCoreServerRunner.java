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
 * File:                org.anon.smart.smcore.test.StartCoreServerRunner
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A runner for core server started
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.base.test.BaseStartServer;
import org.anon.smart.base.test.testanatomy.BaseStartConfig;

public abstract class StartCoreServerRunner extends BaseStartServer
{
    public StartCoreServerRunner(boolean start, int port)
    {
        super(start, port);
    }

    protected String[] tenants()
    {
        _tenants.add("coreanon");
        return new String[] { "coreanon" };
    }

    protected abstract void addTenantConfigs(String[] tenants, BaseStartConfig cfg);

    protected BaseStartConfig getConfig()
    {
        String[] tens = tenants();
        Map<String, String[]> enable = new HashMap<String, String[]>();
        enable.put("ReviewFlow.soa", tens); //by default enable for all. If any custom override and change
        TestStartConfig cfg = new TestStartConfig(new String[] { "ReviewFlow.soa" }, tens, enable, _port);
        addTenantConfigs(tens, cfg);
        return cfg;
    }
}

