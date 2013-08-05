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
 * File:                org.anon.smart.smcore.transition.MetricsManager
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 16, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.monitor;

import java.util.ArrayList;

import java.util.List;

import org.anon.smart.base.dspace.DSpaceService;
import org.anon.smart.base.monitor.MetricCounter;
import org.anon.smart.base.monitor.MonitorConstants;
import org.anon.smart.base.monitor.ObjectAccessCounter;
import org.anon.smart.base.monitor.ObjectCreationCounter;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.DataShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.smcore.data.SmartData;
import org.anon.utilities.exception.CtxException;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.smart.base.dspace.DSpaceService.*;

public class MetricsManager implements MonitorConstants {

	/*public static void handleMetricsfor(D2CacheTransaction txn, SmartData object, int action)
		throws CtxException
	{
		List<MetricCounter> counters = getMetricCountersFor(object, action);
			for(MetricCounter c : counters)
			{
				c.incrementCount();
				DSpaceService.addObject(txn, null, c, null); //TODO original
			}
			
		
	}*/
	
	public static void handleMetricsfor(D2CacheTransaction txn, Object object, MonitorAction action)
			throws CtxException
		{
			List<MetricCounter> counters = action.getCounters(object);
			
			if(counters == null) return;
			
				for(MetricCounter c : counters)
				{
					c.incrementCount();
					DSpaceService.addObject(txn, null, c, null); //TODO original
				}
				
			
		}

	/*private static List<MetricCounter> getMetricCountersFor(SmartData object, int action)
		throws CtxException 
	{
		List<MetricCounter> counters = new ArrayList<MetricCounter>();
		String objGroup = object.smart___objectGroup();
		String flow = flowFor(object.getClass());
		if((action & OBJECTCREATEACTION) == OBJECTCREATEACTION)
		{
			String key = flow+KEYSEPARATOR+objGroup;
			
			counters.add(getCounterFor(key, OBJECTTYPEGROUP));
		}
		if((action & OBJECTACCESSACTION) == OBJECTACCESSACTION)
		{
			
			for(Object k : object.smart___keys())
			{
				String key = flow+KEYSEPARATOR+k;
				counters.add(getCounterFor(key, OBJECTACCESSGROUP));
			}
				
		}
		
		return counters;
	}
	
	private static MetricCounter getCounterFor(String key, String group) 
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
			if(group.equals(OBJECTACCESSGROUP))
				counter = new ObjectAccessCounter(key);
			else if(group.equals(OBJECTTYPEGROUP))
				counter = new ObjectCreationCounter(key);
		}
		return counter;
	}*/

}
