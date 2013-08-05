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
 * File:                org.anon.smart.d2cache.segment.HadoopFileSegement.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 1, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.d2cache.segment;

import java.io.InputStream;
import java.util.List;

import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.fileStore.FileStore;
import org.anon.smart.d2cache.store.fileStore.disk.DiskFSConfig;
import org.anon.smart.d2cache.store.fileStore.disk.DiskFileStoreConnection;
import org.anon.smart.d2cache.store.fileStore.disk.DiskStore;
import org.anon.smart.d2cache.store.fileStore.hadoop.HadoopFileStoreConnection;
import org.anon.smart.d2cache.store.fileStore.hadoop.HadoopStore;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 * 
 */
public class FileStoreSegment implements CSegment {

	private Store _store;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.segment.CSegment#getStore()
	 */
	@Override
	public Store getStore() {
		// TODO Auto-generated method stub
		return _store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.segment.CSegment#setupSegment(java.lang.String,
	 * org.anon.smart.d2cache.store.StoreConfig)
	 */
	@Override
	public void setupSegment(String name, StoreConfig cfg) 
			throws CtxException {
		
		if(cfg instanceof DiskFSConfig){
			_store = new DiskStore(new DiskFileStoreConnection());
		}else{
			_store = new HadoopStore(new HadoopFileStoreConnection());
		}
		_store.setup(name, cfg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.segment.CSegment#storeItem(org.anon.smart.d2cache
	 * .store.StoreItem)
	 */
	@Override
	public void storeItem(StoreItem item) throws CtxException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.segment.CSegment#storeItem(java.util.List)
	 */
	@Override
	public void storeItem(List<StoreItem> items) throws CtxException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.segment.CSegment#cleanup()
	 */
	@Override
	public void cleanup() throws CtxException {
		if (_store != null)
			_store.close();
	}
	public InputStream getFileAsStream(String fileName , String group,ClassLoader cl) throws CtxException{
		return ((FileStore)_store).getFileAsStream(fileName,group,cl);
	}

}
