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
 * File:                org.anon.smart.monitor.plugin.MonitorPlugin
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A plugin for adding monitor functions to the transition
 *
 * ************************************************************
 * */

package org.anon.smart.monitor.plugin;

import org.anon.smart.base.dspace.DSpaceService;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.transition.plugin.BasicPlugin;
import org.anon.smart.monitor.plugin.MonitorableObject;
import org.anon.smart.monitor.plugin.MonitorHookTypes;
import org.anon.smart.monitor.data.Monitor;
import org.anon.smart.monitor.data.MonitorTypes;
import org.anon.smart.monitor.stt.Constants;
import org.anon.smart.d2cache.D2CacheTransaction;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class MonitorPlugin extends BasicPlugin implements Constants
{
    public MonitorPlugin()
    {
    }

    private void createOrModifyMonitor(String[] parms, String group, String qualified, D2CacheTransaction txn, RuntimeShell rshell)
        throws CtxException
    {
        MonitorTypes mtype = MonitorTypes.valueOf(parms[1]);
        String evtkey = parms[0];
        if ((qualified != null) && (qualified.length() > 0))
            evtkey = evtkey + SEP + qualified;
        String key = mtype.getMonitorKey(evtkey, parms);
        Monitor mon = (Monitor)rshell.lookupMonitorFor(group, key);
        System.out.println("Looking up : " + group + ":" + key + ":" + mon);
        if (mon == null)
            mon = mtype.getMonitor(group, evtkey, parms);

        mon.monitorAction();
        DSpaceService.addObject(txn, mon);
    }

    private void addMonitors(Object obj, String grp, MonitorHookTypes mevt, String qualified)
        throws CtxException
    {
        if (obj instanceof MonitorableObject)
        {
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            assertion().assertNotNull(ctx, "Cannot call this outside the transition context");
            D2CacheTransaction txn = ctx.transaction().getMonitorTransaction();
            assertion().assertNotNull(txn, "Cannot start a transaction for monitoring.");
            RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
            MonitorableObject mobj = (MonitorableObject)obj;
            String[] monitors = mobj.getMonitors();
            for (int i = 0; (monitors != null) && (i < monitors.length); i++)
            {
                String[] parms = monitors[i].split("\\|");
                assertion().assertTrue(parms.length >= 2, "Wrong configuration for monitor");
                MonitorHookTypes type = MonitorHookTypes.valueOf(parms[0]);
                if (type.equals(mevt))
                    createOrModifyMonitor(parms, grp, qualified, txn, rshell);
            }
        }
    }

    @Override
    public void eventProcessed(SmartEvent evt)
        throws CtxException
    {
        System.out.println("eventProcessed: " + evt + ":");
        addMonitors(evt, evt.smart___name(), MonitorHookTypes.evt, null);
    }

    @Override
    public void objectCreated(SmartData data)
        throws CtxException
    {
        System.out.println("SmartData: " + data + ":");
        addMonitors(data, data.smart___objectGroup(), MonitorHookTypes.obj, null);
    }

    @Override
    public void objectModified(SmartData data)
        throws CtxException
    {
        System.out.println("ObjectModified: " + data + ":");
        addMonitors(data, data.smart___objectGroup(), MonitorHookTypes.mod, null);
    }

    @Override
    public void primeObjectCreated(SmartPrimeData pdata)
        throws CtxException
    {
        System.out.println("PrimeCreated: " + pdata + ":");
        addMonitors(pdata, pdata.smart___objectGroup(), MonitorHookTypes.pobj, null);
    }

    @Override
    public void stateTransitioned(SmartData data, String from, String to)
        throws CtxException
    {
        System.out.println("Object transitioned: " + data + ":" + from + ":" + to);
        addMonitors(data, data.smart___objectGroup(), MonitorHookTypes.state, to);
    }
}

