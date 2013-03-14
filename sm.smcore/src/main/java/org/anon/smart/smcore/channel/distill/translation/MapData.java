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
 * File:                org.anon.smart.smcore.channel.distill.translation.MapData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A output of the translation stage
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.translation;

import java.util.Map;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Isotope;

public class MapData extends Isotope
{
    private Map<String, Object> _data;

    public MapData(Isotope pdata, Map<String, Object> translated)
    {
        super(pdata);
        _data = translated;
    }

    public Map<String, Object> translated() { return _data; }
    public PData pdata() { return (PData)isotope(); }
}

