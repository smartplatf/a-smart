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
 * File:                org.anon.smart.d2cache.store.repository.hbase.Constants
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of constants for hbase
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

public interface Constants
{
    public static final String ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
    public static final String ZOOKEEPER_CLIENT_PORT = "hbase.zookeeper.property.clientPort";
    public static final String HBASE_MASTER = "hbase.master";

    public static final String SYNTHETIC_COL_FAMILY = "s";
    public static final String DATA_COL_FAMILY = "d";
    public static final String CLASSNAME = "__smart__class__name__";
    public static final String PART_SEPARATOR = ".";
    public static final String KEY_NAME = "__smart__key__";
    public static final String FIELDTYPE = "__FIELDTYPE__";
    public static final String SIZE = "__SIZE__";
    public static final String SMART_KEY_NAME = "___smart_legend___"+PART_SEPARATOR+"_id";
}

