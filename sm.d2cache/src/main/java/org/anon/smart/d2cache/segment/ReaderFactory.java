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
 * File:                org.anon.smart.d2cache.segment.ReaderFactory
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


import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.utilities.exception.CtxException;

public class ReaderFactory {

	public static Reader getReaderFor(Store store, int flags, StoreConfig cfg) throws CtxException {
		if( (flags & D2CacheScheme.BROWSABLE_CACHE) == D2CacheScheme.BROWSABLE_CACHE)
			return new BrowsableReaderImpl(store, cfg);
		else 
			return new DefaultReader(store, cfg); //TODO
	}
	
	public static Reader getReaderFor(Store[] stores, int flags, boolean memOnly)
		throws CtxException {
		Reader rdr = null;
		
		if(memOnly)
			rdr = new MemoryReader(stores);
		else
			rdr = new LayeredReader(stores, new ReplicationWriter());
		
		return rdr;
		
	}
}
