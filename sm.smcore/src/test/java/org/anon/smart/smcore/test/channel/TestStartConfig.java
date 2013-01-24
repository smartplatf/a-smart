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
 * File:                org.anon.smart.smcore.test.channel.TestStartConfig
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

package org.anon.smart.smcore.test.channel;

import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.smart.smcore.channel.server.EventServerConfig;
import org.anon.smart.smcore.anatomy.SMCoreConfig;

import org.anon.smart.smcore.test.testanatomy.TestModuleConfig;

import org.anon.utilities.exception.CtxException;

public class TestStartConfig implements SMCoreConfig, TestModuleConfig
{
    private ExternalConfig[] _channels;
    private String[] _deploy;

    public TestStartConfig(String[] deploy)
    {
        _channels = new ExternalConfig[1];
        _channels[0] = new EventServerConfig(9080, false);
        _deploy = deploy;
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

    public String configDir()
        throws CtxException
    {
        return "config";
    }

    public String[] deploymentFiles()
    {
        return _deploy;
    }
}

