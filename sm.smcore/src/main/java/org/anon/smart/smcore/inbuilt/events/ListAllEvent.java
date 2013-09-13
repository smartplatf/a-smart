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
 * File:                org.anon.smart.smcore.inbuilt.events.ListAllEvent
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 8, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.events;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

public class ListAllEvent implements Serializable {
	private String group;
	private int size = 20;
    private int config = 0;
    private String startTime;
    private String endTime;
	
	public String getGroup() { return group; }
	public int getSize() { return size; }
    public boolean isConfig() { return (config == 1); }

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
