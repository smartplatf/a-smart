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
 * File:                org.anon.smart.base.monitor.ObjectAccessCounter
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

package org.anon.smart.base.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.anon.utilities.atomic.AtomicCounter;
import org.anon.utilities.atomic.DefaultCounter;
import org.anon.utilities.exception.CtxException;
import org.apache.hadoop.metrics.MetricsContext;
import org.apache.hadoop.metrics.spi.AbstractMetricsContext;

public class ObjectAccessCounter extends AbstractMetricCounter {

	
	public ObjectAccessCounter(String key)
	{
		super(key);
		
	}
	
	@Override
	public String smart___objectGroup() throws CtxException {
		// TODO Auto-generated method stub
		return OBJECTACCESSGROUP;
	}
	@Override
	public void smart___initOnLoad() throws CtxException {
		// TODO Auto-generated method stub
		
	}

}
