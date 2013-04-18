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
 * File:                org.anon.smart.d2cache.store.index.solr.Constants
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of constants according to the schema
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

public interface Constants
{
    public final String ID_COLUMN = "id";
    public final String PART_SEPARATOR = ".";

    public final String CORE_CONFIG = "solr.xml";
    public final String SOLR_CONFIG = "solrconfig.xml";
    public final String SCHEMA_CONFIG = "schema.xml";
    public final String DEFAULT_CORE = "core0";
}

