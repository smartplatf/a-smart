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
 * File:                org.anon.smart.secure.test.TestSecureConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for the server
 *
 * ************************************************************
 * */

package org.anon.smart.secure.test;

import java.util.Map;

import org.anon.smart.secure.channel.server.SecureEventServerConfig;

import org.anon.smart.secure.anatomy.SecureConfig;
import org.anon.smart.smcore.test.TestStartConfig;

public class TestSecureConfig extends TestStartConfig implements SecureConfig
{
    public TestSecureConfig(String[] deploy, String[] tenants, Map<String, String[]> enable, int port)
    {
        super(deploy, tenants, enable, port);
        _channels[0] = new SecureEventServerConfig(port, false);
    }
}

