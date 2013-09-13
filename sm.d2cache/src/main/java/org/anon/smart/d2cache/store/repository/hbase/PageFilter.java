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
 * File:                org.anon.smart.d2cache.store.repository.hbase.PageFilter
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Aug 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.filter.FilterBase;

public class PageFilter extends FilterBase
{
    private long _pageSize = Long.MAX_VALUE;
    private int _rowsAccepted = 0;

    public PageFilter()
    {
        
    }
    
    public PageFilter(long size)
    {
       _pageSize = size;
    }
    
    public long getPageSize()
    {
        return _pageSize;
    }
    
    public int getRowsAccepted() 
    {
        return _rowsAccepted;
    }

    public boolean filterAllRemaining()
    {
        return this._rowsAccepted >= this._pageSize;
    }

    public boolean filterRow()
    {
        this._rowsAccepted++;
        return this._rowsAccepted > this._pageSize;
    }
    
    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeLong(_pageSize);

    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
       _pageSize = in.readLong();

    }

}
