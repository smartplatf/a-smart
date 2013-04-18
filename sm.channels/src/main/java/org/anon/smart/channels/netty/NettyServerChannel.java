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
 * File:                org.anon.smart.channels.netty.NettyServerChannel
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A server implementation using netty
 *
 * ************************************************************
 * */

package org.anon.smart.channels.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.anon.smart.channels.AbstractServerChannel;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.utilities.exception.CtxException;

public abstract class NettyServerChannel extends AbstractServerChannel
{
    private ServerBootstrap _bootstrap;

    public NettyServerChannel(SCShell shell, ExternalConfig cfg)
        throws CtxException
    {
        super(shell, cfg);
    }

    protected abstract ChannelPipelineFactory pipelineFactory()
        throws CtxException;

    public void start()
        throws CtxException
    {
        ExternalConfig cfg = (ExternalConfig)_config;
        Executor bossthrd = Executors.newCachedThreadPool();
        Executor workthrd = Executors.newCachedThreadPool();
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(bossthrd, workthrd));
        bootstrap.setPipelineFactory(pipelineFactory());
        bootstrap.bind(new InetSocketAddress(cfg.port()));
        _bootstrap = bootstrap;
        System.out.println(">>>>>>Started the server on the port>>> " + cfg.port());
    }

    public void stop()
        throws CtxException
    {
        super.shutdown();
        _bootstrap.releaseExternalResources();
    }
}

