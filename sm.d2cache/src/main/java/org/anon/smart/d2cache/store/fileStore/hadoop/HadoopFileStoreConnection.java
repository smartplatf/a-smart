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
 * File:                org.anon.smart.d2cache.store.fileStore.HadoopFileStoreConnection.java
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
package org.anon.smart.d2cache.store.fileStore.hadoop;

import static org.anon.utilities.services.ServiceLocator.assertion;
import static org.anon.utilities.services.ServiceLocator.except;
import org.anon.smart.d2cache.store.fileStore.hadoop.HadoopConstants;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author raooll
 * 
 */
public class HadoopFileStoreConnection implements StoreConnection {

	FileSystem _hadoopFs;
	Configuration _conf;
	HadoopFSConfig hfsCfg;

	public HadoopFileStoreConnection() {

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
		return new HadoopFileStoreConnection();
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

		hfsCfg = (HadoopFSConfig) cfg;

		assertion().assertNotNull(hfsCfg, "StoreConfig is null  cfg");
		assertion().assertNotNull(hfsCfg, "HadoopFSConfig is null");
		if (_conf == null) {
			_conf = new Configuration();

			if (!hfsCfg.isLocal()) {
				_conf.set(HadoopConstants.FS_DEFAULT_NAME,
						hfsCfg.getFSDefaultName());
				_conf.set(HadoopConstants.HADOOP_TMP_DIR,
						hfsCfg.getHadoopTempDir());
				_conf.set(HadoopConstants.DFS_REPLICATION,
						hfsCfg.getDFSReplication());
			}

			try {
				_hadoopFs = FileSystem.get(_conf);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				except().rt(
						e,
						new CtxException.Context("Hadoop Connection failed",
								"Exception"));
			}
		}
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
		return new HadoopFileStoreTransaction(txn, this);
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

	public FileSystem getHadoopFS() {
		return _hadoopFs;
	}

	public HadoopFSConfig getHadoopConf() {
		return hfsCfg;
	}

    @Override
    public boolean exists(String group, Object key) 
        throws CtxException
    {
        // TODO Auto-generated method stub
        return false;
    }

}
