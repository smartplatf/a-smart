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
 * File:                org.anon.smart.channels.netty.NettyClientChannel
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A client channel implementation in netty
 *
 * ************************************************************
 * */

package org.anon.smart.channels.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import org.anon.smart.channels.Route;
import org.anon.smart.channels.MessageReader;
import org.anon.smart.channels.AbstractClientChannel;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.smart.channels.data.PData;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public abstract class NettyClientChannel extends AbstractClientChannel
{
    private boolean _connected;
    private ClientBootstrap _bootstrap;
    private Channel _connectedChannel;
    protected MessageReader _reader;
    private Route _route;

    public NettyClientChannel(SCShell shell, ExternalConfig cfg)
        throws CtxException
    {
        super(shell, cfg);
    }

    protected abstract ChannelPipelineFactory pipelineFactory()
        throws CtxException;

    @Override
    protected void initialize(SCShell shell, SCConfig cfg)
        throws CtxException
    {
        _bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                                                    Executors.newCachedThreadPool(),
                                                    Executors.newCachedThreadPool()));

        _bootstrap.setPipelineFactory(pipelineFactory());
        _connected = false;
    }

    public boolean connect()
        throws CtxException
    {
        ExternalConfig cfg = (ExternalConfig)_config;
        ChannelFuture future = _bootstrap.connect(new InetSocketAddress(cfg.server(), cfg.port()));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess())
        {
            //TODO:log in logger
            future.getCause().printStackTrace();
            _bootstrap.releaseExternalResources();
            except().te(this, "Problem connecting to the server: " + cfg.server() + ":" + cfg.port());
            _connected = false;
        }
        else
        {
            _connected = true;
            _connectedChannel = channel;
            _route = new NettyRoute(channel, _id);
        }

        return _connected;
    }

    @Override
    public boolean disconnect()
        throws CtxException
    {
        if (_connected && (_connectedChannel != null))
        {
            ChannelFuture future = _connectedChannel.close();
            future.awaitUninterruptibly();
        }
        super.disconnect();
        return true;
    }

    public void setReader(MessageReader rdr) { _reader = rdr; }

    public void post(PData[] post)
        throws CtxException
    {
        Object obj = _reader.transmitObject(post , _route);
        System.out.println("posting: " + obj);
        _route.send(obj);
    }
}

