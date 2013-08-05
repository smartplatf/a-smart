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
 * File:                org.anon.smart.smcore.test.channel.TestPData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data with tenant etc
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.channel;

import java.util.UUID;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.CData;
import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.http.HTTPRequestPData;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public class TestPData extends HTTPRequestPData
{
    private String _tenant;
    private String _flowType;
    private String _eventType;
    private UUID _sessionID;
    private String _posted;

    public TestPData(DScope scope, CData data)
        throws CtxException
    {
        super(scope, data);
        _posted = io().readStream(data.data()).toString();
        try
        {
            data.data().reset();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setTenant(String tenant) { _tenant = tenant; }
    public void setFlowType(String flow) { _flowType = flow; }
    public void setEventType(String evt) { _eventType = evt; }
    public void setSessionID(UUID sess) { _sessionID = sess; }

    public String tenant() { return _tenant; }
    public String flowType() { return _flowType; }
    public String eventType() { return _eventType; }
    public UUID session() { return _sessionID; }

    public String getPosted() { return _posted; }

    public String toString()
    {
        return ":Tenant: " + _tenant +
            ":Flow:" + _flowType +
            ":EventType:" + _eventType +
            ":Session:" + _sessionID + 
            ":data:" + _posted;
    }
}

