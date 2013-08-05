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
 * File:                org.anon.smart.channels.netty.NettyInstinctHandler
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A handler that works with data instincts
 *
 * ************************************************************
 * */

package org.anon.smart.channels.netty;

import java.util.UUID;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import org.anon.smart.channels.MessageReader;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.shell.DataInstincts;

public class NettyInstinctHandler extends SimpleChannelUpstreamHandler
{
    private UUID _channelID;
    private SCShell _shell;
    private DataInstincts _instinct;
    private MessageReader _reader;

    public NettyInstinctHandler(SCShell shell, SCConfig cfg, MessageReader rdr, UUID channelID)
    {
        _shell = shell;
        _instinct = cfg.instinct();
        _reader = rdr;
        _channelID = channelID;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception
    {
        NettyRoute route = new NettyRoute(ctx.getChannel(), _channelID);
        _instinct.whenConnect(route);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
        throws Exception
    {
        NettyRoute route = new NettyRoute(e.getChannel(), _channelID);
        boolean transmitdefault = _instinct.whenMessage(route, e.getMessage(), _reader);
        if (transmitdefault)
        {
            Object send = _reader.transmitDefault();
            route.send(send);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
    {
        System.out.println("Exception: "+ e.getCause());
        e.getCause().printStackTrace();
        try
        {
            Object send = _reader.transmitException(e.getCause());
            NettyRoute route = new NettyRoute(ctx.getChannel(), _channelID);
            //route.send(send);
        }
        catch (Exception e1)
        {
            //can't help
            e1.printStackTrace();
        }
    }
}

