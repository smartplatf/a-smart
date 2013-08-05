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
 * File:                org.anon.smart.smcore.config.modes.DataTypeMode
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A mode of configuration where the Object type is configured for a config
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.config.modes;

import org.anon.smart.base.utils.AnnotationUtils;
import org.anon.smart.smcore.data.ConfigData;

import org.anon.utilities.exception.CtxException;

public class DataTypeMode extends AbstractMode
{
    public DataTypeMode()
    {
        super();
    }

    protected Object[] myKeys(Class<? extends ConfigData> cls, Object obj)
        throws CtxException
    {
        Class forcls = null;
        if ((obj != null) && (obj instanceof Class))
            forcls = (Class)obj;
        else if (obj != null)
            forcls = obj.getClass();

        if (forcls != null)
        {
            String name = AnnotationUtils.className(forcls);
            if (name != null)
                return new Object[] { name };
        }

        return null;
    }
}

