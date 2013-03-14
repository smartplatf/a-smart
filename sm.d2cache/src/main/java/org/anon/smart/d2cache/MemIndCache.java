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
 * File:                org.anon.smart.d2cache.MemIndCache
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 12, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

import java.util.UUID;

import org.anon.smart.d2cache.segment.CSegment;
import org.anon.smart.d2cache.segment.IndexSegment;
import org.anon.smart.d2cache.segment.MemorySegment;
import org.anon.smart.d2cache.segment.ReaderFactory;
import org.anon.smart.d2cache.segment.ReplicationWriter;
import org.anon.smart.d2cache.store.index.solr.BasicSolrConfig;
import org.anon.smart.d2cache.store.index.solr.SolrConfig;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.utilities.exception.CtxException;

public class MemIndCache implements D2Cache {

	private CSegment[] _segments; 
	private int _flags;
	private StoreConfig _config;
	 public MemIndCache(String name, String related, int flags) throws CtxException {
		CSegment memSegment = new MemorySegment();
		_config = null;//TODO
		memSegment.setupSegment(name, related, _config);
		
		SolrConfig solrConfig = new BasicSolrConfig();
		CSegment indexSegment = new IndexSegment();
		indexSegment.setupSegment(name, related, solrConfig);
		
		_segments = new CSegment[2];
		_segments[0] = memSegment;
		_segments[1] = indexSegment;
		
		_flags = flags;
	}
	
	@Override
	public D2CacheTransaction startTransaction(UUID txnid) throws CtxException {
		StoreConnection[] connections = new StoreConnection[_segments.length];
		int i = 0;
		for(CSegment seg : _segments){
			connections[i++] = seg.getStore().getConnection();
		}
		return new D2CacheTransactionImpl(txnid, connections, new ReplicationWriter());//TODO
	}

	@Override
	public Reader myReader() throws CtxException {
		Store[] stores = new Store[_segments.length];
		for(int i = 0; i< _segments.length;i++){
			stores[i] = _segments[i].getStore();
		}
		return ReaderFactory.getReaderFor(stores, _flags);
		
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
