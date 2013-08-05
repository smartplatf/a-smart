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
 * File:                org.anon.smart.smcore.inbuilt.transition.MetricAccessManager
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 17, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import static org.anon.utilities.services.ServiceLocator.assertion;

import org.anon.smart.base.monitor.MetricCounter;
import org.anon.smart.base.monitor.MonitorConstants;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.inbuilt.events.MetricAccess;
import org.anon.smart.smcore.inbuilt.responses.MetricResponse;
import org.anon.utilities.exception.CtxException;

public class MetricAccessManager {

	public void getMetric(MetricAccess event) throws CtxException {
		String k = event.getKey();
		assertion().assertNotNull(k, "Object Type is NULL");

		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		RuntimeShell shell = (RuntimeShell) (tenant.runtimeShell());
		assertion()
				.assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
		CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(
				tenant.deploymentShell());
		Object o = event;
		SmartEvent sevt = (SmartEvent) o;
		String flow = sevt.smart___flowname();
		String key = null;
		Object res = null;
		if(event.getMetricName().equals(MonitorConstants.INSTANCECOUNT))
		{
			key = new String(flow + MonitorConstants.KEYSEPARATOR + k);
			res = shell.lookupFor(TenantConstants.MONITOR_SPACE,
					MonitorConstants.OBJECTTYPEGROUP, key);
		}
		else if (event.getMetricName().equals(MonitorConstants.ACCESSCOUNT))
		{
			key = new String(flow + MonitorConstants.KEYSEPARATOR + k);
			res = shell.lookupFor(TenantConstants.MONITOR_SPACE,
					MonitorConstants.OBJECTACCESSGROUP, key);
		}
		else if (event.getMetricName().equals(MonitorConstants.EVENTCOUNT))
		{
			key = new String(flow + MonitorConstants.KEYSEPARATOR + k);
			res = shell.lookupFor(TenantConstants.MONITOR_SPACE,
					MonitorConstants.EVENTEXECUTIONGROUP, key);
		}
		
		System.out.println("Looking for obj with key:" + key);
		
		MetricResponse resp = null;
		if (res == null) {
			System.out.println("Metrics for :" + k + " are not enabled...");// TODO
																				// ERRORRESPONSE

			resp = new MetricResponse(0);
		} else {

			assertion().assertTrue((res instanceof MetricCounter),
					"Result is not instance of MetricCounter");

			int count = ((MetricCounter) res).getCount();
			System.out.println("!!!!!!! METRIC RESULT:" + k
					+ ":------->:" + count);
			resp = new MetricResponse(count);
		}

	}
}
