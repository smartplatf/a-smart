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
 * File:                org.anon.smart.channels.http.HTTPServerFactory
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An implementation to create http server
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http;

import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.shell.SCFactory;
import org.anon.smart.channels.http.netty.HTTPServer;

import org.anon.utilities.exception.CtxException;

public class HTTPServerFactory implements SCFactory
{
    public HTTPServerFactory()
    {
    }

    public SmartChannel createSC(SCShell shell, SCConfig cfg)
        throws CtxException
    {
        //default implementation is netty. There is no other currently.
        return new HTTPServer(shell, (HTTPConfig)cfg);
    }
}

