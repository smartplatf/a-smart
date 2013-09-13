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
 * File:                org.anon.smart.d2cache.ListParams
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of params for list functions
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

public class ListParams
{
    private String _group;
    private String _datatype;
    private long _startTime = -1;
    private long _endTime = -1;
    private Object _startKey;
    private Object _endKey;
    private long _size = -1;

    public ListParams(String group)
    {
        this(group, null);
    }

    public ListParams(String group, long size)
    {
        this(group, null, size);
    }

    public ListParams(String group, String datatype)
    {
        _group = group;
        _datatype = datatype;
    }

    public ListParams(String group, String datatype, long size)
    {
        this(group, datatype);
        _size = size;
    }

    public ListParams(String group, String datatype, Object start, Object end)
    {
        this(group, datatype);
        _startKey = start;
        _endKey = end;
    }

    public ListParams(String group, String datatype, long startTime, long endTime)
    {
        this(group, datatype);
        _startTime = startTime;
        _endTime = endTime;
    }

    public ListParams(String group, String datatype, long size, long startTime, long endTime)
    {
        this(group, datatype);
        _startTime = startTime;
        _endTime = endTime;
        _size = size;
    }

    public String getGroup() { return _group; }
    public String getDataType() { return _datatype; }
    public long getStartTime() { return _startTime; }
    public long getEndTime() { return _endTime; }
    public Object getStartKey() { return _startKey; }
    public Object getEndKey() { return _endKey; }
    public long getSize() { return _size; }
}

