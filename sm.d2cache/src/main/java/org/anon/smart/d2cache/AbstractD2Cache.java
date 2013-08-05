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
 * File:                org.anon.smart.d2cache.AbstractD2Cache
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 6, 2013
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
import org.anon.smart.d2cache.segment.RepositorySegment;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.index.solr.BasicSolrConfig;
import org.anon.smart.d2cache.store.index.solr.SolrConfig;
import org.anon.smart.d2cache.store.repository.hbase.HBaseConfig;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.services.ServiceLocator;

public abstract class AbstractD2Cache implements D2Cache {
	
	protected CSegment[] _segments; 
	protected int _flags;
	protected D2CacheConfig _config;
	protected String _name;
	
	public AbstractD2Cache(String name, int flags, D2CacheConfig config)
	{
		_name = name;
		_flags = flags;
		_config = config;
	}
	@Override
	public D2CacheTransaction startTransaction(UUID txnid) throws CtxException {
		StoreConnection[] connections = new StoreConnection[_segments.length];
		int i = 0;
		for(CSegment seg : _segments){
			connections[i++] = seg.getStore().getConnection();
		}
		return new D2CacheTransactionImpl(txnid, connections, new ReplicationWriter());
	}

	@Override
	public Reader myReader() throws CtxException {
		return myReader(false);
	}
	
	@Override
	public Reader myReader(boolean memOnly) throws CtxException {
		Store[] stores = new Store[_segments.length];
		for (int i = 0; i < _segments.length; i++) {
			stores[i] = _segments[i].getStore();
		}
		return ReaderFactory.getReaderFor(stores, _flags, memOnly);
	}

	@Override
	public Reader getBrowsableReader() throws CtxException {
		return ReaderFactory.getReaderFor(_segments[0].getStore(), _flags, null); 
	}

	@Override
	public void cleanupMemory() throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled(int flags) {
		return true;
	}
	
	protected CSegment createMemSegment() throws CtxException
	{
		CSegment memSegment = new MemorySegment();
		memSegment.setupSegment(_name, _config.getMemoryConfig());
		
		return memSegment;
	}
	
	protected CSegment createIndexSegment() throws CtxException
	{
		SolrConfig solrConfig = null;
		
		if(_config.getIndexConfig() != null)
		{
				solrConfig = (SolrConfig)_config.getIndexConfig();
		}
		else
		{
			solrConfig = new BasicSolrConfig(null);
		}
		CSegment indexSegment = new IndexSegment();
		indexSegment.setupSegment(_name, solrConfig);
		
		return indexSegment;
	}
	
	protected CSegment createStoreSegment() throws CtxException
	{
		HBaseConfig hbaseConfig = (HBaseConfig) _config.getStoreConfig();
		CSegment repoSegment = new RepositorySegment();
		repoSegment.setupSegment(_name, hbaseConfig);
		
		return repoSegment;
		
	}

}
