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
 * File:                org.anon.smart.d2cache.store.fileStore.DiskFileStoreConnection.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 30, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.d2cache.store.fileStore.disk;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;

/**
 * @author raooll
 * 
 */
public class DiskFileStoreConnection implements StoreConnection {

	DiskFSConfig _dfsConfig;
	
	public DiskFileStoreConnection(){
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.utilities.utils.Repeatable#repeatMe(org.anon.utilities.utils
	 * .RepeaterVariants)
	 */
	@Override
	public Repeatable repeatMe(RepeaterVariants parms) throws CtxException {
		// TODO Auto-generated method stub
		return new DiskFileStoreConnection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.StoreConnection#connect(org.anon.smart.d2cache
	 * .store.StoreConfig)
	 */
	@Override
	public void connect(StoreConfig cfg) throws CtxException {

		_dfsConfig = (DiskFSConfig) cfg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.store.StoreConnection#open(java.lang.String)
	 */
	@Override
	public void open(String name) throws CtxException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.StoreConnection#createMetadata(java.lang
	 * .String, java.lang.Class)
	 */
	@Override
	public void createMetadata(String name, Class cls) throws CtxException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.StoreConnection#startTransaction(java.util
	 * .UUID)
	 */
	@Override
	public StoreTransaction startTransaction(UUID txn) throws CtxException {
		// TODO Auto-generated method stub
		return new DiskFileStoreTransaction(txn, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.store.StoreConnection#close()
	 */
	@Override
	public void close() throws CtxException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.store.StoreConnection#find(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Object find(String group, Object key) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.StoreConnection#search(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public List<Object> search(String group, Object query) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.StoreConnection#listAll(java.lang.String,
	 * int)
	 */
	@Override
	public Iterator<Object> listAll(String group, int size) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.StoreConnection#remove(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void remove(String group, Object key) throws CtxException {
		// TODO Auto-generated method stub

	}

	public DiskFSConfig getDiskConf(){
		return _dfsConfig;
	}
    @Override
    public boolean exists(String group, Object key) 
        throws CtxException
    {
        // TODO Auto-generated method stub
        return false;
    }
}
