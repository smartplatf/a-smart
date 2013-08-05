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
 * File:                org.anon.smart.d2cache.fileStore.TestConfig.java
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
package org.anon.smart.d2cache.fileStore;

import org.anon.smart.d2cache.D2CacheConfig;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.fileStore.disk.DiskFSConfig;
import org.anon.smart.d2cache.store.fileStore.hadoop.HadoopFSConfig;

/**
 * @author raooll
 *
 */
public class TestConfig implements D2CacheConfig {

	private HadoopFSConfig hfsConfig;

	public TestConfig() {
		hfsConfig = new HadoopFSConfig("hdfs://hadoop:9000",
				"/#hadoophome#/hadoop/datastore", "1");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.D2CacheConfig#createMemoryConfig()
	 */
	@Override
	public void createMemoryConfig() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.D2CacheConfig#createIndexConfig(java.lang.String)
	 */
	@Override
	public void createIndexConfig(String indexHome) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.D2CacheConfig#createStoreConfig(java.lang.String,
	 * java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void createStoreConfig(String zookeeper, String zookeeperPort,
			String hbaseHost, boolean isLocal) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.D2CacheConfig#getMemoryConfig()
	 */
	@Override
	public StoreConfig getMemoryConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.D2CacheConfig#getIndexConfig()
	 */
	@Override
	public StoreConfig getIndexConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.D2CacheConfig#getStoreConfig()
	 */
	@Override
	public StoreConfig getStoreConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreConfig getDiskStoreConfig() {
		// TODO Auto-generated method stub
		return new DiskFSConfig();
	}

	@Override
	public StoreConfig getHadoopStoreConfig() {
		// TODO Auto-generated method stub
		return hfsConfig;
	}

	@Override
	public void createHadoopStoreConfig() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createDiskStoreConfig() {
		// TODO Auto-generated method stub

	}

}
