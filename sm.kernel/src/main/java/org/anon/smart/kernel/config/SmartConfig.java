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
 * File:                org.anon.smart.kernel.config.SmartConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration to startup the kernel
 *
 * ************************************************************
 * */

package org.anon.smart.kernel.config;

import java.util.List;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

public class SmartConfig implements VerifiableObject, java.io.Serializable
{
    private InstallConfig install;
    private EnableFlags enable;
    private List<ChannelConfig> channels;
    private List<ModuleConfig> modules;
    private boolean _verified;

    public SmartConfig()
    {
        _verified = false;
    }

    public boolean isVerified()
    {
        return _verified;
    }

    public boolean verify()
        throws CtxException
    {
        _verified = install.verify();
        _verified = _verified && enable.verify();
        for (ChannelConfig channel : channels)
            _verified = _verified && channel.verify();

        for (ModuleConfig module : modules)
            _verified = _verified && module.verify();

        return _verified;
    }

    public InstallConfig getInstall() { return install; }
    public EnableFlags getFlags() { return enable; }
    public List<ChannelConfig> getChannels() { return channels; }
    public List<ModuleConfig> getModules() { return modules; }
}

