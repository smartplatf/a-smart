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
 * File:                org.anon.smart.d2cache.segment.DefualtReader
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.segment;

import java.util.ArrayList;
import java.util.List;

import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.utilities.exception.CtxException;

public class DefualtReader implements Reader {

	protected CSegment[] _segments;
	protected StoreConfig _config;
	
	public DefualtReader(CSegment[] segements, StoreConfig cfg)
	{
		_segments = segements;
		_config = cfg;
	}
	@Override
	public Object lookup(String group, Object key) throws CtxException {
		Object ret = null;
		for(int i = 0 ; ((ret == null) && (i < _segments.length)); i++) {
			ret = _segments[i].get(group, key);
		}
		
		return ret;
	}

	@Override
	public List<Object> search(String group, Object query) throws CtxException {
		List<Object> resultSet = new ArrayList<Object>();
		for(int i = 0 ; i < _segments.length; i++) {
			resultSet.addAll(_segments[i].search(group, query));//TODO
		}
		
		return resultSet;
	}
	
	

}
