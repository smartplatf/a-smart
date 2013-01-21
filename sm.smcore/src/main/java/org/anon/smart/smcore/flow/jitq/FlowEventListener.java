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

import org.anon.utilities.jitq.JITProcessQueue;
import org.anon.utilities.jitq.DataListener;
import org.anon.utilities.cthreads.RuntimeContext;
import org.anon.utilities.exception.CtxException;

public class FlowEventListener implements DataListener
{
    private JITProcessQueue _queue;

    public FlowEventListener()
    {
    }

    public void setQueue(JITProcessQueue q)
    {
        _queue = q;
    }

    public void onMessage(Object data)
        throws CtxException
    {
        System.out.println("Data is: " + data);
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

