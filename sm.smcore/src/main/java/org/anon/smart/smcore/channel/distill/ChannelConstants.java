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
 * File:                org.anon.smart.smcore.channel.distill.ChannelConstants
 * Author:              rsankar
 * Revision:            1.0
 * Date:                20-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of constants for commn
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill;

public interface ChannelConstants
{
    public static final String ACTION = "___smart_action___";
    public static final String KEY_TYPE = "___key_type___";

    public static final String LOOKUP_ACTION = "lookup";
    public static final String SEARCH_ACTION = "search";

    //standard fields in event
    public static final String EVENT_LEGEND_FLD = "___smart_legend___";
    public static final String FLOW_FLD = "___smart_flow___";
    public static final String PRIMEDATA_FLD = "___smart_primeData___";
    public static final String SESSION_FLD = "___smart_session___";
}

