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
 * File:                org.anon.smart.smcore.config.ConfigService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A service for config related tasks
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.config;

import java.util.Map;

import org.anon.smart.smcore.data.ConfigData;

import org.anon.utilities.exception.CtxException;

public class ConfigService
{
    private static CReadScheme CURRENT_READ_SCHEME = new PrecedenceScheme();
    private static CWriteScheme CURRENT_WRITE_SCHEME = new PrecedenceScheme();

    private ConfigService()
    {
    }

    public static ConfigData configFor(Object obj, Class<? extends ConfigData> cls)
        throws CtxException
    {
        CModel model = new CModel(CURRENT_READ_SCHEME, CURRENT_WRITE_SCHEME);
        return model.configFor(cls, obj);
    }

    public static ConfigData[] saveConfig(Class<? extends ConfigData> cls, Object forkey, Map<String, Object> values)
        throws CtxException
    {
        CModel model = new CModel(CURRENT_READ_SCHEME, CURRENT_WRITE_SCHEME);
        return model.createConfig(cls, forkey, values);
    }
}

