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
 * File:                org.anon.smart.monitor.inbuilt.transitions.MonitorManager
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A manager to retrieve monitor data
 *
 * ************************************************************
 * */

package org.anon.smart.monitor.inbuilt.transitions;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.d2cache.ListParams;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.monitor.data.MonitorTypes;
import org.anon.smart.monitor.plugin.MonitorHookTypes;
import org.anon.smart.monitor.util.MonitorUtils;
import org.anon.smart.monitor.inbuilt.events.ReadMonitorData;
import org.anon.smart.monitor.inbuilt.responses.MonitorResponse;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class MonitorManager
{
    public void getMonitorData(ReadMonitorData data, FlowAdmin admin)
        throws CtxException
    {
		assertion().assertNotNull(data, " MonitorManager: getMonitorData is NULL");
		assertion().assertNotNull(data.getGroup(), "MonitorData: Group cannot be NULL for ReadMonitorData");
        assertion().assertNotNull(data.getHookType(), "MonitorData: Need to provide the type of monitor hook.");
		
        String space = TenantConstants.MONITOR_SPACE;

        String flow = data.getFlow();
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkDeploymentShell shell = tenant.deploymentShell();
        CrossLinkFlowDeployment dep = shell.deploymentFor(flow);
        assertion().assertNotNull(dep, "Cannot find the deployment for " + flow);
        String clsname = dep.classFor(data.getGroup());
        assertion().assertNotNull(clsname, "Cannot find the deployment class for: " + data.getGroup() + " in " + flow);
        Class cls = null;
        switch (data.getHookType())
        {
        case evt:
            cls = shell.eventClass(flow, data.getGroup());
            break;
        case pobj:
            cls = shell.primeClass(flow, data.getGroup());
            break;
        default:
            cls = shell.dataClass(flow, data.getGroup());
            break;
        }
        assertion().assertNotNull(cls, "Cannot find the deployment class for: " + clsname + ":" + data.getGroup() + ":" +  flow);

        RuntimeShell rshell = (RuntimeShell)tenant.runtimeShell();

        String[] monitors = MonitorUtils.getMonitors(cls);
        if ((monitors == null) || (monitors.length <= 0))
        {
            new MonitorResponse(new ArrayList());
            return;
        }

        String datatype = null;
        MonitorTypes monitortype = null;

        for (int i = 0; i < monitors.length; i++)
        {
            String[] parms = monitors[i].split("\\|");
            assertion().assertTrue(parms.length >= 2, "Wrong configuration for monitor");
            MonitorHookTypes type = MonitorHookTypes.valueOf(parms[0]);
            MonitorTypes mtype = MonitorTypes.valueOf(parms[1]);
            if (type.equals(data.getHookType()))
            {
                if ((data.getMonitorType() == null)|| (data.getMonitorType().equals(mtype)))
                {
                    monitortype = mtype;
                    break;
                }
            }
        }

        if (monitortype == null)
        {
            //did not find the monitor, so forget it
            new MonitorResponse(new ArrayList());
            return;
        }

        datatype = monitortype.datatype();
        long start = data.getStartTime();
        List res = new ArrayList();
        if (start <= 0)
        {
            //read all.
            res = rshell.listAll(space, data.getGroup(), -1, datatype);
        }
        else
        {
            ListParams parms = new ListParams(data.getGroup(), datatype, start, data.getEndTime());
            res = rshell.listAll(space, parms);
        }

		System.out.println("Monitor returned " + res.size() + " results");

		MonitorResponse resp = new MonitorResponse(res);
	}
}

