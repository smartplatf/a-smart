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
 * File:                org.anon.smart.d2cache.store.repository.hbase.InternalKey
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An internal key implementation specific to SMART
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import static org.anon.utilities.services.ServiceLocator.*;

public class InternalKey implements KeyColumn, Constants
{
    private String _group;
    private String _datatype;
    private String _keyTypeCol;

    public InternalKey(String group, String datatype)
    {
        if ((datatype == null) || (datatype.length() <= 0))
            datatype = group;
        _keyTypeCol = datatype+PART_SEPARATOR+SMART_KEY_NAME+FIELDTYPE;
        _group = group;
        _datatype = datatype;
    }

    public void addToScan(Scan s)
        throws Exception
    {
        s.addColumn(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes(_keyTypeCol));
    }

    public Object read(Result result)
        throws Exception
    {
        Class keyFieldType = null;
        byte[] keyType = result.getValue(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes(_keyTypeCol));
        if(keyType != null)
            keyFieldType =  Class.forName(new String(keyType));
        else
            keyFieldType = String.class;
        Object keyObj = type().convertToPrimitive(keyFieldType, result.getRow());
        return keyObj;
    }
}

