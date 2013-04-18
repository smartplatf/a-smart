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
 * File:                org.anon.smart.smcore.channel.server.EventResponses
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of responses for an event
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.server;

import java.util.UUID;

import org.anon.smart.channels.data.Responses;
import org.anon.smart.smcore.events.SmartEventResponse;

public class EventResponses extends Responses implements SmartEventResponse, java.io.Serializable
{
    private UUID event;

    public EventResponses(UUID id)
    {
        super();
        event = id;
    }

    public UUID smart___eventID()
    {
        return event;
    }
}

