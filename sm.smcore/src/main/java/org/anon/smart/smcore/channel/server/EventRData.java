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
 * File:                org.anon.smart.smcore.channel.server.EventRData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A rectified data for the event
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.server;

import java.util.UUID;

import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.smcore.events.CrossLinkSmartEvent;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.RData;

import org.anon.utilities.exception.CtxException;

public class EventRData extends RData
{
    private Object _event;
    private SmartTenant _tenant;
    private FlowDeployment _deployment;
    private String _origin;
    private UUID _requestID;
    private UUID _eventID;

    public EventRData(PData data, Object event, SmartTenant tenant, FlowDeployment dep)
        throws CtxException
    {
        super(data);
        EventPData epdata = (EventPData)data;
        EventDScope dscope = (EventDScope)epdata.dscope();
        _origin = dscope.origin();
        _requestID = dscope.requestID();
        _deployment = dep;
        _tenant = tenant;
        _event = event;
        CrossLinkSmartEvent cle = new CrossLinkSmartEvent(_event);
        _eventID = cle.eventID();
    }

    public Object event() { return _event; }
}

