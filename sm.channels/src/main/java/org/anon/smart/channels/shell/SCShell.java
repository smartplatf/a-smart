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
 * File:                org.anon.smart.channels.shell.SCShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A shell of all channels in smart
 *
 * ************************************************************
 * */

package org.anon.smart.channels.shell;

import java.util.UUID;
import java.util.Map;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.SmartServerChannel;

import org.anon.utilities.exception.CtxException;

public class SCShell
{
    private Map<UUID, SmartChannel> _channels;

    public SCShell()
    {
        _channels = new ConcurrentHashMap<UUID, SmartChannel>();
    }

    public void registerChannel(SmartChannel chnl)
    {
        _channels.put(chnl.id(), chnl);
    }

    public void deregisterChannel(SmartChannel chnl)
    {
        _channels.remove(chnl.id());
    }

    public SmartChannel addChannel(SCConfig cfg)
        throws CtxException
    {
        return cfg.creator().createSC(this, cfg);
    }

    public void startAllChannels()
        throws CtxException
    {
        for (SmartChannel channel : _channels.values())
        {
            if (channel instanceof SmartServerChannel)
                ((SmartServerChannel)channel).start();
        }
    }

    public void stopAllChannels()
        throws CtxException
    {
        for (SmartChannel channel : _channels.values())
        {
            if (channel instanceof SmartServerChannel)
                ((SmartServerChannel)channel).stop();
        }
    }

    public static int isUsed(int port)
    {
        try
        {
            String host = "localhost";
            Socket sock = new Socket(host, port);
            sock.close();
        }
        catch (Exception e)
        {
            return port;
        }

        return -1;
    }

    public static int getNextAvailablePort(int[] ports)
    {
        if (ports != null)
        {
            for (int i = 1; i < ports.length; i++)
            {
                int port = isUsed(ports[i]);
                if (port > 0) return port;
            }
        }

        return -1;
    }

    public SmartChannel channelFor(UUID id)
    {
        return _channels.get(id);
    }
}

