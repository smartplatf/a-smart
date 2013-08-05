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
 * File:                org.anon.smart.d2cache.BasicD2CacheConfig
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 2, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.fileStore.disk.DiskFSConfig;
import org.anon.smart.d2cache.store.fileStore.hadoop.HadoopFSConfig;
import org.anon.smart.d2cache.store.index.solr.BasicSolrConfig;
import org.anon.smart.d2cache.store.repository.hbase.TestHBaseConfig;

public class BasicD2CacheConfig implements D2CacheConfig {

	private StoreConfig _memConfig;
	private StoreConfig _solrConfig;
	private StoreConfig _storeConfig;
	private StoreConfig _diskConfig;
	private StoreConfig _hadoopConfig;

	public BasicD2CacheConfig() {

	}

	public BasicD2CacheConfig(String solrHome, String zookeeper,
			String zookeeperPort, String hbaseHost, boolean isLocal) {
		_solrConfig = new BasicSolrConfig(solrHome);
		_storeConfig = new TestHBaseConfig(zookeeper, zookeeperPort, hbaseHost,
				isLocal);
		createHadoopStoreConfig();
		createDiskStoreConfig();
	}

	public void createIndexConfig(String solrHome) {
		_solrConfig = new BasicSolrConfig(solrHome);
	}

	public void createStoreConfig(String zookeeper, String zookeeperPort,
			String hbaseHost, boolean isLocal) {
		_storeConfig = new TestHBaseConfig(zookeeper, zookeeperPort, hbaseHost,
				isLocal);
	}

	@Override
	public StoreConfig getMemoryConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreConfig getIndexConfig() {
		// TODO Auto-generated method stub
		return _solrConfig;
	}

	@Override
	public StoreConfig getStoreConfig() {
		// TODO Auto-generated method stub
		return _storeConfig;
	}

	@Override
	public void createMemoryConfig() {
		// TODO Auto-generated method stub

	}

	@Override
	public StoreConfig getDiskStoreConfig() {
		// TODO Auto-generated method stub
		return _diskConfig;
	}

	@Override
	public StoreConfig getHadoopStoreConfig() {
		// TODO Auto-generated method stub
		return _hadoopConfig;
	}

	@Override
	public void createHadoopStoreConfig() {
		_hadoopConfig = new HadoopFSConfig("hdfs://hadoop:9000",
				"/#hadoophome#/hadoop/datastore", "1");

	}

	@Override
	public void createDiskStoreConfig() {
		_diskConfig = new DiskFSConfig();

	}

}
