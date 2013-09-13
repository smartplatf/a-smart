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
 * File:                org.anon.smart.d2cache.store.repository.hbase.BytesConverter
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A converter to bytes of the key object
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import java.util.UUID;
import java.math.BigDecimal;

import org.apache.hadoop.hbase.util.Bytes;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;


public class BytesConverter
{
    private static final byte SEPARATOR = 0x00;

    public BytesConverter()
    {
    }

    public static byte[] convertBytes(Object key)
        throws CtxException
    {
        if (type().checkPrimitive(key.getClass()))
        {
            return convertPrimitive(key);
        }
        else
        {
            //TODO:
            assertion().assertTrue(false, "Currently compound key is not supported.");
        }

        return null;
    }

    private static byte[] convertPrimitive(Object key)
        throws CtxException
    {
        if (key instanceof String)
            return Bytes.toBytes((String)key);
        else if (key instanceof Boolean)
            return Bytes.toBytes(((Boolean)key).booleanValue());
        else if (key instanceof Short)
            return Bytes.toBytes(((Short)key).shortValue());
        else if (key instanceof Integer)
            return Bytes.toBytes(((Integer)key).intValue());
        else if (key instanceof Long)
            return Bytes.toBytes(((Long)key).longValue());
        else if (key instanceof Float)
            return Bytes.toBytes(((Float)key).floatValue());
        else if (key instanceof Double)
            return Bytes.toBytes(((Double)key).doubleValue());
        else if (key instanceof UUID)
            return Bytes.toBytes(key.toString());
        else if (key instanceof BigDecimal)
            return Bytes.toBytes((BigDecimal)key);

        return Bytes.toBytes(key.toString());
    }
}

