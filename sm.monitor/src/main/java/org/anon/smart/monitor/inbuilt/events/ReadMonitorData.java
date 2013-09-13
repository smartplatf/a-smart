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
 * File:                org.anon.smart.monitor.inbuilt.events.ReadMonitorData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An event to read monitor data
 *
 * ************************************************************
 * */

package org.anon.smart.monitor.inbuilt.events;

import java.util.Date;

import org.anon.smart.monitor.data.MonitorTypes;
import org.anon.smart.monitor.plugin.MonitorHookTypes;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class ReadMonitorData implements java.io.Serializable
{
    private String flow;
    private String group;
    private String monitortype;
    private String hooktype;
    private String startTime;
    private String endTime;
    private int duration; //duration is in hours??

    public ReadMonitorData()
    {
    }

    public String getGroup() { return group; }
    public MonitorTypes getMonitorType() 
    { 
        if (monitortype != null)
            return MonitorTypes.valueOf(monitortype); 

        return null;
    }
    public MonitorHookTypes getHookType() 
    { 
        if (hooktype != null)
            return MonitorHookTypes.valueOf(hooktype); 

        return null;
    }

    public int getDuration() { return duration; }
    public String getFlow() { return flow; }

    private long convertTime(String time)
        throws CtxException
    {
        if ((time == null) || (time.length() <= 0))
            return -1;

        Date dt = convert().stringToDate(time, null); //default format is MM/d/yyyy HH:mm
        return dt.getTime();
    }


    public long getStartTime()
        throws CtxException
    {
        return convertTime(startTime);
    }

    public long getEndTime()
        throws CtxException
    {
        return convertTime(endTime);
    }
}

