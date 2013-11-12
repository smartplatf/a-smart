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
 * File:                org.anon.smart.smcore.timer.TimerPlugin
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A plugin for timer related calls
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.timer;

import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.transition.plugin.BasicPlugin;
import org.anon.smart.base.tenant.shell.RuntimeShell;

import static org.anon.smart.base.utils.AnnotationUtils.*;

import org.anon.utilities.scheduler.ScheduleTask;
import org.anon.utilities.exception.CtxException;

public class TimerPlugin extends BasicPlugin
{
    public TimerPlugin()
    {
    }

    public void objectCreated(SmartData obj)
        throws CtxException
    {
        System.out.println("Object created. Checking for timer: " + obj);
        String state = obj.utilities___currentState().stateName();
        setupTimer(obj, state);
    }

    public void primeObjectCreated(SmartPrimeData obj)
        throws CtxException
    {
        objectCreated(obj);
    }

    private void setupTimer(SmartData obj, String to)
        throws CtxException
    {
        Class cls = obj.getClass();
        int timeout = timeoutFor(cls, to);
        System.out.println("State Changed. Checking for timer: " + obj + ":" + to + ":" + timeout);
        if (timeout > 0)
        {
            System.out.println("Setting Timer for data : "  + obj + ": state: " + to + ":" + timeout);
            StateExpiryTask runnable = new StateExpiryTask(flowFor(cls), className(cls), obj.smart___id());
            ScheduleTask task = new ScheduleTask(runnable, timeout);
            //TODO: need to remove the previous one?

            RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
            rshell.scheduleTask(task);
        }
    }

    public void stateTransitioned(SmartData obj, String from, String to)
        throws CtxException
    {
        setupTimer(obj, to);
    }
}

