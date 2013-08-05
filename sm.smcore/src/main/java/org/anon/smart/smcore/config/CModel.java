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
 * File:                org.anon.smart.smcore.config.CModel
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A system that defines the different types of configuration
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.config;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.data.ConfigData;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class CModel
{
    private CReadScheme _readScheme;
    private CWriteScheme _writeScheme;

    public CModel(CReadScheme rscheme, CWriteScheme wscheme)
    {
        _readScheme = rscheme;
        _writeScheme = wscheme;
    }

    public ConfigData configFor(Class<? extends ConfigData> cls, Object obj)
        throws CtxException
    {
        ConfigData data = null;
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        assertion().assertNotNull(tenant, "The tenant cannot be Null. There is some internal error.");
        RuntimeShell rshell = (RuntimeShell)tenant.runtimeShell();
        CMode mode = _readScheme.nextMode(null, data);
        while (mode != null)
        {
            data = mode.modeConfig(cls, obj, rshell);
            if ((data != null) && (!type().isAssignable(data.getClass(), cls)))
                data = null;
            mode = _readScheme.nextMode(mode, data); //if the scheme is a First Found Return mode, it shd return null when data is not null.
        }

        return data;
    }

    public ConfigData[] createConfig(Class<? extends ConfigData> cls, Object forkey, Map<String, Object> values)
        throws CtxException
    {
        List<ConfigData> ret = new ArrayList<ConfigData>();
        CMode mode = _writeScheme.nextMode(null, ret);
        while (mode != null)
        {
            ConfigData[] data = mode.newConfig(cls, forkey, values);
            for (int i = 0; (data != null) && (i < data.length); i++)
                ret.add(data[i]);

            mode = _writeScheme.nextMode(mode, ret);
        }

        return ret.toArray(new ConfigData[0]);
    }
}

