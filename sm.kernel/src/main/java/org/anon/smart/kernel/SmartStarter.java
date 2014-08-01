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
 * File:                org.anon.smart.kernel.SmartStarter
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A started for smart
 *
 * ************************************************************
 * */

package org.anon.smart.kernel;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.smart.kernel.config.SmartConfig;
import org.anon.smart.deployment.MacroDeployer;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantsHosted;
import org.anon.smart.base.application.ApplicationSuite;

import org.anon.utilities.exception.CtxException;

public class SmartStarter implements Runnable
{
    private SmartConfig _config;
    private boolean _master;
    private SmartModuleConfig _moduleConfig;
    private String[] _startOrder;
    private boolean _stop;

    public SmartStarter(SmartConfig cfg, boolean master, String[] start, boolean stop)
        throws CtxException
    {
        _config = cfg;
        _master = master;
        _moduleConfig = new SmartModuleConfig(cfg, master);
        _startOrder = start;
        _stop = stop;
    }

    public void run()
    {
        try
        {
            if (!_stop)
            {
                anatomy().startup(_moduleConfig, _startOrder);
                //trying this, why is the other one having problems..? no idea??
                MacroDeployer.setConfigDir(_moduleConfig.configDir());
                MacroDeployer.deployPersistedJars();
                ApplicationSuite.deployPersistedApplications();
            }
            else
            {
                anatomy().shutDown();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

