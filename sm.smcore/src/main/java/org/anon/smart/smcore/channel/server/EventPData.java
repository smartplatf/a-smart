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
 * File:                org.anon.smart.smcore.channel.server.EventPData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * The primal data for a smart event
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.server;

import java.util.UUID;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.CData;
import org.anon.smart.channels.data.DScope;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class EventPData extends PData
{
    private String _tenant;
    private String _flow;
    private String _eventName;
    private UUID _sessionId;

    public EventPData(DScope scope, CData data)
    {
        super(scope, data);
        EventDScope edscope = (EventDScope)scope;
        _tenant = edscope.tenant();
        _flow = edscope.flow();
        _eventName = edscope.eventName();
        _sessionId = edscope.sessionId();
    }

    public String tenant() { return _tenant; }
    public String flow() { return _flow; }
    public String eventName() { return _eventName; }
    public UUID sessionId() { return _sessionId; }
}

