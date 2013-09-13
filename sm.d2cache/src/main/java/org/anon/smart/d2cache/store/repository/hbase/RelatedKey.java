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
 * File:                org.anon.smart.d2cache.store.repository.hbase.RelatedKey
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A key for the related object
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Result;

import static org.anon.utilities.services.ServiceLocator.*;

public class RelatedKey implements KeyColumn, Constants
{
    private String _retrieveColumn;

    public RelatedKey()
    {
        _retrieveColumn = RELATED_OBJECT + PART_SEPARATOR + RELATED_COLUMN;
    }

    public void addToScan(Scan s)
        throws Exception
    {
        s.addColumn(Bytes.toBytes(DATA_COL_FAMILY), Bytes.toBytes(_retrieveColumn));
    }

    public Object read(Result result)
        throws Exception
    {
        Class keyFieldType = String.class;
        byte[] value = result.getValue(Bytes.toBytes(DATA_COL_FAMILY), Bytes.toBytes(_retrieveColumn));
        Object keyObj = type().convertToPrimitive(keyFieldType, result.getRow());
        return keyObj;
    }
}

