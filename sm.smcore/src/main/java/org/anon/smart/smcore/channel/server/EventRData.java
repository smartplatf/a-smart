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

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.smcore.channel.internal.MessagePData;
import org.anon.smart.smcore.events.CrossLinkSmartEvent;

import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.RData;
import org.anon.smart.channels.data.Responses;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.channels.SmartServerChannel;
import org.anon.smart.smcore.anatomy.SMCoreContext;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class EventRData extends RData
{
    private Object _event;
    private CrossLinkSmartTenant _tenant;
    private FlowDeployment _deployment;
    private String _origin;
    private String _flow;
    private UUID _requestID;
    private UUID _eventID;
    private Rectifier _myRectifier;

    public EventRData(Rectifier rectifier, PData data, Object event, CrossLinkSmartTenant tenant, FlowDeployment dep)
        throws CtxException
    {
        super(data);
        _myRectifier = rectifier;
        
        //EventPData epdata = (EventPData)data;
        //EventDScope dscope = (EventDScope)epdata.dscope();
        DScope dscope = data.dscope();
        _origin = dscope.origin();
        _requestID = dscope.requestID();
        _flow = dscope.flow();
        _deployment = dep;
        _tenant = tenant;
        _event = event;
        CrossLinkSmartEvent cle = new CrossLinkSmartEvent(_event);
        _eventID = cle.smart___eventID();
        
        
    }

    public Object event() { return _event; }
    public String flow() { return _flow; }

    @Override
    protected Responses aggregatedResponse()
    {
        return new EventResponses(_eventID);
    }

    @Override
    protected PData createPDataFrom(Object response)
        throws CtxException
    {
        System.out.println("Sending response: " + response);
        EventRData rrdata = new EventRData(_myRectifier, (PData)isotope(), response, _tenant, _deployment);
        Distillate respstart = new Distillate(rrdata);
        Distillate dist = _myRectifier.condense(respstart);
        return (PData)dist.current();
    }

    @Override
    protected SmartServerChannel getChannel(UUID channelID)
        throws CtxException
    {
        SMCoreContext ctx = (SMCoreContext)anatomy().context(this.getClass());
        return (SmartServerChannel)ctx.smartChannelShell().channelFor(channelID);
    }
}

