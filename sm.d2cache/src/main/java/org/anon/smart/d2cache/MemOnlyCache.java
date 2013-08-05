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
 * File:                org.anon.smart.d2cache.D2CacheImpl
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

package org.anon.smart.d2cache;


import org.anon.smart.d2cache.segment.CSegment;
import org.anon.smart.d2cache.segment.MemorySegment;
import org.anon.smart.d2cache.segment.ReaderFactory;
import org.anon.utilities.exception.CtxException;

public class MemOnlyCache extends  AbstractD2Cache {

	public MemOnlyCache(String name, int flags) throws CtxException {
		this(name, flags, null);
	}
	
	public MemOnlyCache(String name, int flags, D2CacheConfig config) throws CtxException {
		super(name, flags, config);
		_segments = new CSegment[1];
		_segments[0] = createMemSegment();
		
	}
	

	@Override
	public void cleanupMemory() throws CtxException {
		// TODO Auto-generated method stub
        for (int i = 0; i < _segments.length; i++)
            _segments[i].cleanup();
	}



}
