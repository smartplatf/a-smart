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
 * File:                org.anon.smart.kernel.config.SmartConfigReader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A reader for smartconfig
 *
 * ************************************************************
 * */

package org.anon.smart.kernel.config;

import java.util.Map;
import java.io.InputStream;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.config.Format;
import org.anon.utilities.exception.CtxException;

public class SmartConfigReader
{
    private SmartConfigReader()
    {
    }

    public static SmartConfig readConfigForServer(String svr)
        throws CtxException
    {
        String mypkg = type().getPackage(SmartConfigReader.class);
        mypkg = mypkg.replaceAll("\\.", "/");
        String file = mypkg + "/SmartConfig" + svr + ".cfg";
        InputStream str = SmartConfigReader.class.getClassLoader().getResourceAsStream(file);
        assertion().assertNotNull(str, "Cannot find the configuration for: " + svr + ":" + file);
        Format fmt = config().readYMLConfig(str);
        Map values = fmt.allValues();
        SmartConfig config = convert().mapToVerifiedObject(SmartConfig.class, values);
        return config;
    }
}

