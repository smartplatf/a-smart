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
 * File:                org.anon.smart.kernel.config.InstallConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for install related configurations
 *
 * ************************************************************
 * */

package org.anon.smart.kernel.config;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

public class InstallConfig implements VerifiableObject, java.io.Serializable
{
    private String path;
    private String version;
    private String user;
    private String identity;
    private String password;
    private String configdir;

    private boolean _isVerified;

    public InstallConfig()
    {
    }

    public String getPath()
        throws CtxException
    {
        return value().envOrValue(path);
    }

    public String getVersion()
        throws CtxException
    {
        return value().envOrValue(version);
    }

    public String getUser()
    {
        return user;
    }

    public String getIdentity()
    {
        if (identity == null)
            identity = user;

        return identity;
    }

    public String getPassword()
    {
        return password;
    }

    public String getConfigDir()
        throws CtxException
    {
        if ((configdir == null) || (configdir.length() <= 0))
            configdir = getPath() + "/config";

        return configdir;
    }

    public boolean isVerified() { return _isVerified; }
    public boolean verify()
        throws CtxException
    {
        assertion().assertNotNull(path, "The install path has to be provided for config");
        assertion().assertNotNull(version, "The install version has to be provided for config");
        assertion().assertNotNull(user, "The admin user has to be provided for config");
        _isVerified = true;
        return _isVerified;
    }
}

