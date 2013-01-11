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
 * File:                org.anon.smart.channels.http.netty.HTTPPipelineFactory
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A factory for http using netty
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http.netty;

import java.util.UUID;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;

import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.http.ServerSSLContext;
import org.anon.smart.channels.netty.NettyInstinctHandler;

public class HTTPPipelineFactory implements ChannelPipelineFactory
{
    private SCShell _shell;
    private SCConfig _config;
    private UUID _channelID;
    private ServerSSLContext _sslContext;

    public HTTPPipelineFactory(UUID channelID, SCShell shell, SCConfig cfg, ServerSSLContext ctx)
    {
        _shell = shell;
        _config = cfg;
        _sslContext = ctx;
        _channelID = channelID;
    }

    public ChannelPipeline getPipeline()
        throws Exception
    {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();

        if (_sslContext != null)
            pipeline.addLast("ssl", new SslHandler(_sslContext.createEngine(false)));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("deflater", new HttpContentCompressor());
        pipeline.addLast("handler", new NettyInstinctHandler(_shell, _config, new NettyRequestReader(), _channelID));
        return pipeline;
    }

}

