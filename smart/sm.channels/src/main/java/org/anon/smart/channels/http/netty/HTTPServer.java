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
 * File:                org.anon.smart.channels.http.netty.HTTPServer
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A http server that is used to recognize http requests
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http.netty;

import org.jboss.netty.channel.ChannelPipelineFactory;

import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.ServerSSLContext;
import org.anon.smart.channels.netty.NettyServerChannel;

import org.anon.utilities.exception.CtxException;

public class HTTPServer extends NettyServerChannel
{
    private ServerSSLContext _sslContext;

    public HTTPServer(SCShell shell, HTTPConfig cfg)
        throws CtxException
    {
        super(shell, cfg);
    }

    @Override
    protected void initialize(SCShell shell, SCConfig cfg)
        throws CtxException
    {
        HTTPConfig httpcfg = (HTTPConfig)cfg;
        if (httpcfg.makeSecure())
            _sslContext = new ServerSSLContext(httpcfg);
    }

    @Override
    protected ChannelPipelineFactory pipelineFactory()
        throws CtxException
    {
        return new HTTPPipelineFactory(_id, _shell, _config, _sslContext);
    }
}

