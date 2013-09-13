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
 * File:                org.anon.smart.smcore.flow.jitq.FlowEventListener
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A listener for flow events
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.flow.jitq;

import java.util.UUID;
import java.util.Map;

import org.anon.smart.base.flow.FlowObject;
import org.anon.smart.smcore.transition.TransitionService;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.inbuilt.responses.ErrorResponse;
import org.anon.smart.smcore.channel.server.CrossLinkEventRData;
import org.anon.smart.smcore.transition.MessageSource;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.jitq.JITProcessQueue;
import org.anon.utilities.jitq.DataListener;
import org.anon.utilities.cthreads.RuntimeContext;
import org.anon.utilities.exception.CtxException;

public class FlowEventListener implements DataListener, MessageSource
{
    private JITProcessQueue _queue;

    public FlowEventListener()
    {
    }

    public void setQueue(JITProcessQueue q)
    {
        _queue = q;
    }

    public void doneMessage()
        throws CtxException
    {
        jitq().doneProcessingMessage(_queue);
    }

    public void onMessage(Object data)
        throws CtxException
    {
        try
        {
            System.out.println("Data is: " + data + _queue);
            TransitionContext ctx = TransitionService.createContext(data, this);
            ctx.executeGraph();
        }
        catch (Exception e)
        {
            //TODO: log this error
            e.printStackTrace();
            //send out an error message if an exception is caught here
            try
            {
                CrossLinkEventRData rdata = new CrossLinkEventRData(data);
                rdata.clearResponses();
                ErrorResponse resp = new ErrorResponse(ErrorResponse.servererrors.exception, e);
                rdata.addResponse(resp);
                rdata.commitResponses();
            }
            catch (Exception e1)
            {
                //TODO: log error
                e1.printStackTrace();
            }
            finally
            {
                try
                {
                    doneMessage(); //so we can continue
                }
                catch (Exception e2)
                {
                    e2.printStackTrace();
                }
            }
        }
    }

    public RuntimeContext startRuntimeContext(String action, JITProcessQueue queue)
        throws CtxException
    {
        return null;
    }

    public UUID getID(Object associated)
    {
        if (associated instanceof FlowObject)
        {
            FlowObject obj = (FlowObject)associated;
            return obj.flowID();
        }

        return null;
    }

    public String getName(Object associated)
    {
        if (associated instanceof FlowObject)
        {
            FlowObject obj = (FlowObject)associated;
            return obj.flowName();
        }

        return null;
    }

    public Map<String, Object> locals(Object associated, Object data)
        throws CtxException
    {
        return null;
    }
}

