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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.segment.CSegment;
import org.anon.smart.d2cache.segment.ReaderFactory;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.memory.jcs.JCSSegment;
import org.anon.utilities.exception.CtxException;

public class MemOnlyCache implements D2Cache {

	private CSegment[] _segments; 
	private int _flags;
	private StoreConfig _config;
	public MemOnlyCache(String name, String related, int flags) throws CtxException {
		CSegment segment = new JCSSegment();
		_config = null;//TODO
		segment.setupSegment(name, related, _config);
		
		_segments = new CSegment[1];
		_segments[0] = segment;
		
		_flags = flags;
	}
	@Override
	public D2CacheTransaction startTransaction(UUID txnid) throws CtxException {
		return new D2CacheTransactionImpl(txnid, _segments);
	}

	@Override
	public Reader myReader() throws CtxException {
		return ReaderFactory.getReaderFor(_segments, _flags, _config);
		
	}

	@Override
	public void cleanupMemory() throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled(int flags) {
		return true;
	}

}
