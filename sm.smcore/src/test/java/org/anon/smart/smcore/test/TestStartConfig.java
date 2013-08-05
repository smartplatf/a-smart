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
 * File:                org.anon.smart.smcore.test.TestStartConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for start up of server
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.smart.smcore.channel.server.EventServerConfig;
import org.anon.smart.smcore.anatomy.SMCoreConfig;

import org.anon.smart.base.test.testanatomy.BaseStartConfig;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class TestStartConfig extends BaseStartConfig implements SMCoreConfig
{
    protected ExternalConfig[] _channels;

    public TestStartConfig(String[] deploy, String[] tenants, Map<String, String[]> enable, int port)
    {
        super(deploy, tenants, enable);
        _channels = new ExternalConfig[1];
        _channels[0] = new EventServerConfig(port, false);
    }

    public ExternalConfig[] startChannels()
        throws CtxException
    {
        return _channels;
    }

    public boolean firstJVM()
    {
        return true;
    }

    public boolean initTenants()
    {
        return false; //will be initialized by smcore
    }
}

