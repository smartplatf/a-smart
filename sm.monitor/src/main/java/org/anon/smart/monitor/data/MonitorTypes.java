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
 * File:                org.anon.smart.monitor.data.MonitorTypes
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * The types of monitors supported
 *
 * ************************************************************
 * */

package org.anon.smart.monitor.data;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.exception.CtxException;

public enum MonitorTypes
{
    cnt(new CounterMonitor()),
    tcnt(new TimedCounter());

    private Monitor monitor;

    private MonitorTypes(Monitor m)
    {
        monitor = m;
    }

    public String getMonitorKey(String mevt, String[] parms)
        throws CtxException
    {
        return monitor.getKey(mevt, parms);
    }

    public Monitor getMonitor(String group, String mevt, String[] parms)
        throws CtxException
    {
        MonitorParms vars = new MonitorParms(group, mevt, parms);
        return (Monitor)monitor.repeatMe(vars);
    }

    public String datatype()
    {
        return monitor.getClass().getSimpleName();
    }
}

