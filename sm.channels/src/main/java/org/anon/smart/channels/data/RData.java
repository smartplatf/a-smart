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
 * File:                org.anon.smart.channels.data.RData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A refined data created from the primary data based on various ways of refinement
 *
 * ************************************************************
 * */

package org.anon.smart.channels.data;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.SmartServerChannel;

import org.anon.utilities.exception.CtxException;

public class RData extends Isotope
{
    private long _convertedTime;
    private DScope _dscope;
    private boolean _committedResponses;

    public RData(PData data)
    {
        super(data);
        _convertedTime = System.nanoTime();
        _dscope = data.dscope();
        _committedResponses = false;
    }

    public long convertedTime() { return _convertedTime; }

    protected Responses aggregatedResponse()
    {
        return new Responses();
    }

    public void addResponse(Object response)
        throws CtxException
    {
        _dscope.responder().addResponse(response);
    }

    public void clearResponses()
        throws CtxException
    {
        _dscope.responder().clearResponses();
    }

    protected PData createPDataFrom(Object response)
        throws CtxException
    {
        return null; //has to be implemented by derived classes.
    }

    protected SmartServerChannel getChannel(UUID channelID)
        throws CtxException
    {
        return null;
    }

    public void sendResponses()
        throws CtxException
    {
        Responder resp = _dscope.responder();
        Object[] responses = resp.responses();
        List<PData> send = new ArrayList<PData>();
        Responses sresp = aggregatedResponse();
        for (int i = 0; i < responses.length; i++)
            sresp.addResponse(responses[i]);

        PData pdata = createPDataFrom(sresp);
        if (pdata != null)
            send.add(pdata);

        if (send.size() > 0)
            _dscope.transmit(send.toArray(new PData[0]));

        _dscope.close();
    }

    public void commitResponses()
        throws CtxException
    {
        if (!_committedResponses)
        {
            UUID channelID = _dscope.channelID();
            if(channelID != null)
            {
            SmartServerChannel chnl = getChannel(channelID);
            if (chnl != null)
                chnl.sendResponses(this);
            }
            _committedResponses = true;
        }
    }
    
    public DScope dScope(){
    	return _dscope;
    }
}

