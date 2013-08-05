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
 * File:                org.anon.smart.smcore.config.modes.AbstractMode
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A base cmode class for all modes
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.config.modes;

import java.util.Map;

import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.utils.AnnotationUtils;
import org.anon.smart.smcore.data.ConfigData;
import org.anon.smart.smcore.config.CConstants;
import org.anon.smart.smcore.config.CMode;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public abstract class AbstractMode implements CMode, CConstants
{
    public AbstractMode()
    {
    }

    protected abstract Object[] myKeys(Class<? extends ConfigData> cls, Object obj)
        throws CtxException;

    public ConfigData modeConfig(Class<? extends ConfigData> cls, Object obj, RuntimeShell rshell)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find null config.");
        String cfgGroup = AnnotationUtils.className(cls);
        assertion().assertNotNull(cfgGroup, "Cannot find config for non-hosted data." + cls.getName());
        ConfigData ret = null;
        Object[] keys = myKeys(cls, obj);
        for (int i = 0; (ret == null) && (keys != null) && (i < keys.length); i++)
        {
            ret = (ConfigData)rshell.lookupConfigFor(cfgGroup, keys[i]);
        }

        return ret;
    }

    public ConfigData[] newConfig(Class<? extends ConfigData> cls, Object forkey, Map<String, Object> values)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot create a null config object type.");
        assertion().assertNotNull(forkey, "Cannot create a config object for null key." + cls.getName());
        assertion().assertNotNull(values, "No values specified to create config object from." + cls.getName() + ":" + forkey);

        Object[] keys = myKeys(cls, forkey);
        ConfigData data = null;
        if (keys != null)
            data = convert().mapToObject(cls, values);
        for (int i = 0; (keys != null) && (data != null) && (i < keys.length); i++)
            data.smart___addKey(keys[i]);

        if (data != null)
            return new ConfigData[] { data };
        else
            return null;
    }

    public int modeKeys(Class<? extends ConfigData> cls, Object obj)
        throws CtxException
    {
        Object[] keys = myKeys(cls, obj);
        if (keys != null)
            return keys.length;
        else
            return 0;
    }
}

