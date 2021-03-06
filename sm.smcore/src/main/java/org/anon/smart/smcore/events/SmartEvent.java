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
 * File:                org.anon.smart.smcore.events.SmartEvent
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * an event that is used in smart
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.events;

import java.util.UUID;

import org.anon.smart.base.flow.FlowObject;
import org.anon.smart.smcore.data.SmartPrimeData;

import org.anon.utilities.exception.CtxException;

public interface SmartEvent
{
    public void smarteventstt____init()
        throws CtxException;
    public String smart___name();
    public UUID smart___eventID();
    public FlowObject smart___forFlow();
    public EventLegend smart___legend();
    public SmartPrimeData smart___primeData();
    public String smart___flowname();
    public String smart___extratransitionfilter()
        throws CtxException;
    //public Session session();
}

