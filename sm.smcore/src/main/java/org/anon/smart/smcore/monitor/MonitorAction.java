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
 * File:                org.anon.smart.smcore.monitor.MonitorAction
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                May 21, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.monitor;

import static org.anon.smart.base.utils.AnnotationUtils.flowFor;
import static org.anon.utilities.services.ServiceLocator.assertion;

import java.util.ArrayList;
import java.util.List;

import org.anon.smart.base.monitor.MetricCounter;
import org.anon.smart.base.monitor.MonitorConstants;
import org.anon.smart.base.monitor.EventExecutionCounter;
import org.anon.smart.base.monitor.ObjectAccessCounter;
import org.anon.smart.base.monitor.ObjectCreationCounter;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.utilities.exception.CtxException;

public enum MonitorAction {

	OBJECTACCESS, OBJECTCREATION,
	EVENTEXECUTED;
	
	public List<MetricCounter> getCounters(SmartData object) 
		throws CtxException
	{
		List<MetricCounter> counters = new ArrayList<MetricCounter>();
		String objGroup = object.smart___objectGroup();
		String flow = flowFor(object.getClass());
		
		switch(this)
		{
			case OBJECTCREATION:
			{
				String key = flow+ MonitorConstants.KEYSEPARATOR+objGroup;
				counters.add(getCounterFor(key, MonitorConstants.OBJECTTYPEGROUP));
			}
			break;
			case OBJECTACCESS:
			{
				for(Object k : object.smart___keys())
				{
					String key = flow+MonitorConstants.KEYSEPARATOR+k;
					counters.add(getCounterFor(key, MonitorConstants.OBJECTACCESSGROUP));
				}
			}
		}
		
		//System.out.println("TOTAL COUNTERS:"+counters.size());
		return counters;
	}
	
	public List<MetricCounter> getCounters(SmartEvent object) 
			throws CtxException
	{
		List<MetricCounter> counters = new ArrayList<MetricCounter>();
		String flow = object.smart___flowname();
		String event = object.smart___name();
		
		String key = flow+ MonitorConstants.KEYSEPARATOR+event;
		counters.add(getCounterFor(key, MonitorConstants.EVENTEXECUTIONGROUP));
		
		return counters;
	}
	
	public List<MetricCounter> getCounters(Object object) 
			throws CtxException
	{
		if(object instanceof SmartData)
			return getCounters((SmartData)object);
		if(object instanceof SmartEvent)
			return getCounters((SmartEvent)object);
		
		return null;
	}
		
	
	
	private MetricCounter getCounterFor(String key, String group) 
			throws CtxException
		{
			
			CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
			RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
			assertion().assertNotNull(shell, "MetricsManager: Runtime Shell is NULL");
			Object c = shell.lookupFor(TenantConstants.MONITOR_SPACE, group, key);
			MetricCounter counter = null;
			if(c != null)
			{
				counter = (MetricCounter)c;
			}
			else
			{
				if(group.equals(MonitorConstants.OBJECTACCESSGROUP))
					counter = new ObjectAccessCounter(key);
				else if(group.equals(MonitorConstants.OBJECTTYPEGROUP))
					counter = new ObjectCreationCounter(key);
				else if(group.equals(MonitorConstants.EVENTEXECUTIONGROUP))
					counter = new EventExecutionCounter(key);
			}
			return counter;
		}
	
	
}
