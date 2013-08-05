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
 * File:                org.anon.smart.smcore.events.EventLegend
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A legend for the event that contains information about the event
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.events;

import java.util.UUID;

public class EventLegend
{
    private UUID _eventID;
    private UUID _sessionID;
    private long _receivedOn;
    private long _respondedAt;
    private String _origin;

    public EventLegend()
    {
    	_eventID = UUID.randomUUID();
    }

    public EventLegend(UUID sessID)
    {
        _eventID = UUID.randomUUID();
        _sessionID = sessID;
    }

    public EventLegend(UUID sessID, String origin)
    {
        _eventID = UUID.randomUUID();
        _sessionID = sessID;
        _origin = origin;
    }

    public void stampReceived(long recvd)
    {
        _receivedOn = recvd;
    }

    public void stampReceived()
    {
        _receivedOn = System.nanoTime();
    }

    public void stampResponded()
    {
        _respondedAt = System.nanoTime();
    }

    public UUID eventID() { return _eventID; }
    public UUID sessionID() { return _sessionID; }
    public long receivedOn() { return _receivedOn; }
    public long respondedAt() { return _respondedAt; }
    public String origin() { return _origin; }
}

