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
 * File:                org.anon.smart.smcore.transition.TConstants
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of constants used in transitions
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition;

public interface TConstants
{
    public static final String EVENT = "event";
    public static final String DATA = "data";
    public static final String RELATED = "related";
    public static final String CONFIG = "config";
    public static final String TXN = "txn";
    public static final String LINK = "link";
    public static final String SEARCH = "srch";
    public static final String SRCHCFG = "srchcfg";
    public static final String ALL = "all";
    public static final String SESSION = "session"; //this is implemented in secure package

    public static final String NEEDSSERVICE = "needsService";
    public static final String NOTMAPPED = "notMapped";
    public static final String NEEDSLINK = "needslink";
}

