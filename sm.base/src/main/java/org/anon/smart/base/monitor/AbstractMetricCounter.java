package org.anon.smart.base.monitor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.anon.smart.base.monitor.MetricCounter;
import org.anon.utilities.atomic.AtomicCounter;
import org.anon.utilities.atomic.DefaultCounter;
import org.anon.utilities.exception.CtxException;

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
 * File:                .AbstractMetricCounter
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

public abstract class AbstractMetricCounter implements MetricCounter, Serializable, MonitorConstants {

	//private AtomicCounter _counter;
	private int _counter;
	private String _objKey;
	
	public AbstractMetricCounter(String key)
	{
		_objKey = key;
		//System.out.println("Created with key:"+_objKey);
		//_counter = new DefaultCounter(0);
		_counter = 0;
	}
	@Override
	public void incrementCount() throws CtxException {
		//_counter.incrementAndGet();
		_counter++;

	}

	@Override
	public int getCount() throws CtxException {
		//return _counter.get();
		return _counter;
	}

	@Override
	public void reset() throws CtxException {
		
	}
	@Override
	public List<Object> smart___keys() throws CtxException {
		List<Object> keys = new ArrayList<Object>();
		keys.add(_objKey);
		return keys;
	}

	@Override
	public abstract String smart___objectGroup() throws CtxException;
		

	@Override
	public void smart___initOnLoad() throws CtxException {
		// TODO Auto-generated method stub
		
	}

}
