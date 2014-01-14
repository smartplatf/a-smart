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
 * File:                org.anon.smart.kernel.config.ChannelConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for channels
 *
 * ************************************************************
 * */

package org.anon.smart.kernel.config;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

public class ChannelConfig implements VerifiableObject, java.io.Serializable
{
    private String name;
    private String ports;
    private String protocol;
    private String type;
    private String translator;

    public boolean _isVerified;

    public ChannelConfig()
    {
    }

    public String getName() { return name; }

    public int[] getPortRange()
        throws CtxException
    {
        return value().rangeAsInt(ports);
    }

    public String getProtocol()
    {
        return protocol;
    }

    public String channelType()
    {
        if (type == null)
            type = "event";
        return type;
    }

    public String getTranslator()
    {
        return translator;
    }

    public boolean isVerified() { return _isVerified; }

    public boolean verify()
        throws CtxException
    {
        assertion().assertNotNull(name, "Channel name cannot be null");
        assertion().assertNotNull(ports, "Channel ports cannot be null");
        assertion().assertNotNull(protocol, "Channel protocol cannot be null");

        //TODO: check other parameters
        _isVerified = true;
        return _isVerified;
    }
}

