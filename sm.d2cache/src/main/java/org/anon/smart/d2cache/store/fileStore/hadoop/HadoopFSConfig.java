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
 * File:                org.anon.smart.d2cache.hadoopfs.HadoopFSConfig.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 27, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.d2cache.store.fileStore.hadoop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author raooll
 * 
 */
public class HadoopFSConfig implements HadoopStoreConfig {

	private String _baseDirectory;
	private String fs_default_name;
	private String hadoop_tmp_dir;
	private String dfs_replication;

	public HadoopFSConfig(String defaultName, String hadoopTmpDir,
			String dfsReplication) {
		this("/fsStore", defaultName, hadoopTmpDir, dfsReplication);
	}

	public HadoopFSConfig(String defalutRepo, String defaultName,
			String hadoopTmpDir, String dfsReplication) {
		fs_default_name = defaultName;
		_baseDirectory = defalutRepo;
		hadoop_tmp_dir = hadoopTmpDir;
		dfs_replication = dfsReplication;
	}

	@Override
	public String baseDirectory() {
		// TODO Auto-generated method stub
		return _baseDirectory;
	}

	@Override
	public String getFSDefaultName() {
		// TODO Auto-generated method stub
		return fs_default_name;
	}

	@Override
	public String getHadoopTempDir() {
		// TODO Auto-generated method stub
		return hadoop_tmp_dir;
	}

	@Override
	public String getDFSReplication() {
		// TODO Auto-generated method stub
		return dfs_replication;
	}

	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

}
