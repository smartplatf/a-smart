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
 * File:                org.anon.smart.kernel.config.ModuleConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for module configuration
 *
 * ************************************************************
 * */

package org.anon.smart.kernel.config;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

public class ModuleConfig implements VerifiableObject, java.io.Serializable
{
    private String name;
    private String module;
    private int loadorder;
    private int startorder;
    private String classloader;
    private String defaultmod;
    private String jars;
    private String dependantjars;
    private String enableflag;

    private boolean _isVerified = false;

    public ModuleConfig()
    {
    }

    public String getName() { return name; }
    public String getModule() { return module; }
    public boolean isVerified() { return _isVerified; }
    public int getLoadOrder() { return loadorder; }
    public int getStartOrder() { return startorder; }
    public String getClassLoader() { return classloader; }
    public String[] getSmartJars()
        throws CtxException
    {
        return value().listAsString(jars);
    }

    public String[] getDependantJars()
        throws CtxException
    {
        if (dependantjars != null)
            return value().listAsString(dependantjars);

        return new String[0];
    }

    public boolean isDefaultMod()
    {
        return convert().stringToBoolean(defaultmod);
    }

    public String getEnableFlag()
    {
        return enableflag;
    }

    public boolean verify()
        throws CtxException
    {
        //TODO:
        _isVerified = true;
        return _isVerified;
    }
}

