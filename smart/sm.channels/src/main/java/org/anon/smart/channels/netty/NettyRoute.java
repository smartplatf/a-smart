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
 * File:                org.anon.smart.channels.netty.NettyRoute
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A single route that connects two machines using netty
 *
 * ************************************************************
 * */

package org.anon.smart.channels.netty;

import java.util.UUID;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import org.anon.smart.channels.Route;

import org.anon.utilities.exception.CtxException;

public class NettyRoute implements Route
{
    private Channel _channel;
    private UUID _channelID;

    public NettyRoute(Channel chnl, UUID id)
    {
        _channel = chnl;
        _channelID = id;
    }

    public Channel getChannel() { return _channel; }

    public void send(Object resp)
        throws CtxException
    {
        if (_channel.isOpen())
        {
            ChannelFuture future = _channel.write(resp);
            future.awaitUninterruptibly();
        }
    }

    public void close()
        throws CtxException
    {
        if (_channel.isOpen())
        {
            ChannelFuture future = _channel.close();
            future.awaitUninterruptibly();
        }
    }

    public UUID channelID() { return _channelID; }
}

