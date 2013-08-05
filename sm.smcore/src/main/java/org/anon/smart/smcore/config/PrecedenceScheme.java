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
 * File:                org.anon.smart.smcore.config.PrecedenceScheme
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A scheme where a more narrower config takes precedence over a broader config
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.config;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.anon.smart.smcore.config.modes.DataInstanceMode;
import org.anon.smart.smcore.config.modes.DataTypeMode;
import org.anon.smart.smcore.config.modes.FlowMode;
import org.anon.smart.smcore.config.modes.StaticValueMode;
import org.anon.smart.smcore.data.ConfigData;

import org.anon.utilities.exception.CtxException;

public class PrecedenceScheme implements CReadScheme, CWriteScheme
{
    private Map<CMode, CMode> _nextMode;
    private CMode[] _modes;

    public PrecedenceScheme()
    {
        _nextMode = new HashMap<CMode, CMode>();
        _modes = new CMode[] 
                {
                    new DataInstanceMode(),
                    new StaticValueMode(),
                    new DataTypeMode(),
                    new FlowMode()
                };

        for (int i = 1; i < _modes.length; i++)
        {
            _nextMode.put(_modes[i - 1], _modes[i]);
        }
    }

    public CMode nextMode(CMode previous, ConfigData data)
    {
        if (data == null)
        {
            if (previous == null)
                return _modes[0];

            return _nextMode.get(previous);
        }

        return null;
    }

    public CMode nextMode(CMode mode, List<ConfigData> created)
        throws CtxException
    {
        if (mode == null)
            return _modes[0];

        return _nextMode.get(mode);
    }
}

