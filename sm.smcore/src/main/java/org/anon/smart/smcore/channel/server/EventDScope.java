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
 * File:                org.anon.smart.smcore.channel.server.EventDScope
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A datascope for smart events
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.server;

import java.util.UUID;

import org.anon.smart.channels.Route;
import org.anon.smart.channels.http.HTTPMessageReader;
import org.anon.smart.channels.http.HTTPDataFactory;
import org.anon.smart.channels.http.HTTPMessageDScope;

import org.anon.smart.smcore.events.CrossLinkEventLegend;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class EventDScope extends HTTPMessageDScope
{
    private String _tenant;
    private String _flow;
    private String _eventName;
    private UUID _sessionId;

    public EventDScope(Route r, Object msg, HTTPMessageReader rdr, HTTPDataFactory fact)
        throws CtxException
    {
        super(r, msg, rdr, fact);
    }

    protected void handlePath(String path)
        throws CtxException
    {
        if ((path == null) || (path.length() <= 0) || (path.equals("/")))
            return; //let the default handle this.

        String[] tokens = path.split("/");
        int count = 0;
        while ((count < tokens.length) && (tokens[count].trim().length() <= 0))
            count++;

        assertion().assertTrue((count < tokens.length), "The URL is not correct. Please use a URL of format: /tenant/flowname/event");
        _tenant = tokens[count++];
        assertion().assertTrue((count < tokens.length), "The URL is not correct. Please use a URL of format: /tenant/flowname/event");
        _flow = tokens[count++];
        if (count < tokens.length)
            _eventName = tokens[count++];
        else
            _eventName = "SearchEvent";

    }

    protected void handleHeader(String key, String value)
    {
        super.handleHeader(key, value);
        if (key.equalsIgnoreCase("Session-Id"))
            _sessionId = UUID.fromString(value);
    }

    public String tenant() { return _tenant; }
    public String flow() { return _flow; }
    String eventName() { return _eventName; }
    UUID sessionId() { return _sessionId; }

    public Object eventLegend(ClassLoader ldr)
        throws CtxException
    {
        CrossLinkEventLegend legend = new CrossLinkEventLegend(_sessionId, _origin, ldr);
        legend.stampReceived(primary().receivedTime());
        return legend.link();
    }
}

