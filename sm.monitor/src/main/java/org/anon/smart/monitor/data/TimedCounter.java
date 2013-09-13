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
 * File:                org.anon.smart.monitor.data.TimedCounter
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A counter that is time grouped
 *
 * ************************************************************
 * */

package org.anon.smart.monitor.data;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class TimedCounter extends CounterMonitor
{
    private enum groupby { hour, day, week, month };

    TimedCounter()
    {
        super();
    }

    public TimedCounter(String g, String evt, String time)
        throws CtxException
    {
        super(g, evt);
        key = constructKey(evt, getBucket(time));
    }

    public String getBucket(String time)
        throws CtxException
    {
        groupby t = groupby.valueOf(time);
        assertion().assertNotNull(t, "The specified grouping is not correct. Please fix it." + time);
        Date dt = new Date();
        DateFormat fmt;
        switch(t)
        {
        case hour:
            fmt = new SimpleDateFormat("yyyyMMMdHH");
            break;
        case week:
            fmt = new SimpleDateFormat("yyyyw");
            break;
        case month:
            fmt = new SimpleDateFormat("yyyyMMM");
            break;
        case day:
        default:
            fmt = new SimpleDateFormat("yyyyMMMd");
            break;
        }

        return fmt.format(dt);
    }

    public String getKey(String evt, String[] parms)
        throws CtxException
    {
        assertion().assertTrue(parms.length >= 3, "For a timed counter, the time parameter has to be specified.");
        String key = getBucket(parms[2]);
        return constructKey(evt, key);
    }

    @Override
    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        MonitorParms parms = (MonitorParms)vars;
        String[] p = parms.getParams();
        assertion().assertTrue(p.length >= 3, "Need to specify time parameter.");
        return new TimedCounter(parms.getGroup(), parms.getMonitorEvent(), p[2]);
    }
}

