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
 * File:                org.anon.smart.channels.http.netty.HTTPClient
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A client for http server
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http.netty;

import org.jboss.netty.channel.ChannelPipelineFactory;

import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.ServerSSLContext;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.netty.NettyClientChannel;

import org.anon.utilities.exception.CtxException;

public class HTTPClient extends NettyClientChannel implements HTTPClientChannel
{
    private ServerSSLContext _sslContext;

    public HTTPClient(SCShell shell, HTTPConfig cfg)
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
        super.initialize(shell, cfg);
    }

    @Override
    protected ChannelPipelineFactory pipelineFactory()
        throws CtxException
    {
        return new HTTPClientPipelineFactory(_id, _shell, _config, _sslContext, this);
    }

    public void post(String uri, PData[] post)
        throws CtxException
    {
        NettyResponseReader rrdr = (NettyResponseReader)_reader;
        rrdr.resetGet();
        rrdr.setURI(uri);
        rrdr.setHost(((HTTPConfig)_config).server());
        super.post(post);
    }

    public void get(String uri)
        throws CtxException
    {
        NettyResponseReader rrdr = (NettyResponseReader)_reader;
        rrdr.setGet();
        rrdr.setURI(uri);
        rrdr.setHost(((HTTPConfig)_config).server());
        super.post(null);
    }
}


