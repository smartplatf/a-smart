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
 * File:                org.anon.smart.smcore.timer.StateExpiryTask
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A task to be executed when state of an object expires
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.timer;

import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.inbuilt.events.TimedEvent;
import org.anon.smart.smcore.channel.internal.MessageConfig;
import org.anon.smart.smcore.anatomy.CoreContext;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

public class StateExpiryTask implements Runnable
{
    private static final String SYSTEM_RUNTIME = "systemContext";

    private Object objectkey;
    private String objectgroup;
    private String objectflow;

    public StateExpiryTask(String flow, String grp, Object key)
    {
        objectkey = key;
        objectgroup = grp;
        objectflow = flow;
    }

    public void run()
    {
        try
        {
            RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
            threads().addToContextLocals(SYSTEM_RUNTIME, "TimedEvent");
            SmartData data = (SmartData)rshell.lookupFor(objectflow, objectgroup, objectkey);
            assertion().assertNotNull(data, "Cannot find data to post timed event to. " + objectflow + ":" + objectgroup + ":" + objectkey + ":" + this.getClass().getClassLoader());
            Object evt = new TimedEvent("stateexpired", data);
            SmartEvent event = (SmartEvent)evt;
            //MessageConfig mc = new MessageConfig(event);
            CoreContext ctx = (CoreContext)anatomy().overriddenContext(this.getClass());
            MessageConfig mc = (MessageConfig)ctx.getMessageConfig(event);
            mc.postMessage();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

