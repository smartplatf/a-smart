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
 * File:                org.anon.smart.kernel.SmartModuleConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for a module in smart
 *
 * ************************************************************
 * */

package org.anon.smart.kernel;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.smcore.anatomy.SMCoreConfig;
import org.anon.smart.kernel.config.SmartConfig;
import org.anon.smart.kernel.config.ChannelConfig;
import org.anon.smart.smcore.channel.server.EventServerConfig;
import org.anon.smart.smcore.channel.server.UploadServerConfig;
import org.anon.smart.secure.channel.server.SecureEventServerConfig;

import org.anon.utilities.exception.CtxException;

public class SmartModuleConfig implements SMCoreConfig
{
    private ExternalConfig[] _channels;
    private String _configDir;
    private boolean _master;

    public SmartModuleConfig(SmartConfig cfg, boolean master)
        throws CtxException
    {
        _configDir = cfg.getInstall().getConfigDir();
        _master = master;
        List<ExternalConfig> lst = new ArrayList<ExternalConfig>();
        List<ChannelConfig>  channels = cfg.getChannels();
        for (ChannelConfig channel : channels)
        {
            ExternalConfig ccfg = createChannel(channel);
            if (ccfg != null)
                lst.add(ccfg);
        }
        _channels = lst.toArray(new ExternalConfig[0]);
    }

    private ExternalConfig createChannel(ChannelConfig cfg)
        throws CtxException
    {
        String type = cfg.channelType();
        ExternalConfig ccfg = null;
        boolean https = (cfg.getProtocol().equals("https"));
        int[] ports = cfg.getPortRange();
        int port = SCShell.getNextAvailablePort(ports);
        
        if (type.equals("event"))
            ccfg = new EventServerConfig(port, https);
        //if the channel type is upload create a httpuploadConfig
        else if (type.equals("upload"))
        	ccfg = new UploadServerConfig(port, https);
        else if (type.equals("secureevent"))
            ccfg = new SecureEventServerConfig(port, https);
        return ccfg;
    }

    public ExternalConfig[] startChannels()
        throws CtxException
    {
        return _channels;
    }

    public boolean firstJVM()
    {
        return _master;
    }

    public String configDir()
        throws CtxException
    {
        return _configDir;
    }
}

