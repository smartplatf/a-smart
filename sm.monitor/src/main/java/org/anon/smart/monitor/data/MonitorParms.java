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
 * File:                org.anon.smart.monitor.data.MonitorParms
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of parameters to create monitor
 *
 * ************************************************************
 * */

package org.anon.smart.monitor.data;

import org.anon.utilities.utils.RepeaterVariants;

public class MonitorParms implements RepeaterVariants
{
    private String[] _parms;
    private String _group;
    private String _mevent;

    public MonitorParms(String g, String evt, String[] parms)
    {
        _group = g;
        _mevent = evt;
        _parms = parms;
    }

    public String[] getParams()
    {
        return _parms;
    }

    public String getGroup() { return _group; }
    public String getMonitorEvent() { return _mevent; }
}

