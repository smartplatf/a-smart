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
 * File:                org.anon.smart.base.test.BaseStartServer
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A runner for smart server in test cases
 *
 * ************************************************************
 * */

package org.anon.smart.base.test;

import java.util.List;
import java.util.ArrayList;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantsHosted;

import org.anon.smart.base.test.testanatomy.BaseStartConfig;

public abstract class BaseStartServer implements Runnable
{
    private boolean _start;
    protected int _port;
    protected List<String> _tenants;

    public BaseStartServer(boolean start, int port)
    {
        _start = start;
        _port = port;
        _tenants = new ArrayList<String>();
    }

    protected abstract BaseStartConfig getConfig();

    public void run()
    {
        try
        {
            if (_start)
            {
                BaseStartConfig cfg = getConfig();
                anatomy().startup(cfg);
            }
            else
            {
                for (String tenant : _tenants)
                {
                    SmartTenant ten = TenantsHosted.tenantFor(tenant);
                    if (ten != null)
                        ten.cleanup();
                }

                anatomy().shutDown();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

