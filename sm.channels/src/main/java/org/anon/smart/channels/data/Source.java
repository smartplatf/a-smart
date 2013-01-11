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
 * File:                org.anon.smart.channels.data.Source
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A source for data
 *
 * ************************************************************
 * */

package org.anon.smart.channels.data;

public class Source
{
    public enum src { ex, in };

    private src _source;
    private Object _origin;

    public Source(src which, Object what)
    {
        _source = which;
        _origin = what;
    }

    public Object origin() { return _origin; }
    public boolean isExt() { return _source.equals(src.ex); }
    public boolean isInt() { return _source.equals(src.in); }

    public String toString()
    {
        return _source.name() + ":" + _origin;
    }
}

