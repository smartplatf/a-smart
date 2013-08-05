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
 * File:                org.anon.smart.d2cache.store.fileStore.HadooppFileStore.java
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.anon.smart.d2cache.store.AbstractStore;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.fileStore.FileStore;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @author raooll
 * 
 */
public class HadoopStore extends AbstractStore implements FileStore{

	public HadoopStore(StoreConnection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(String name, Class cls) throws CtxException {
		FileSystem fs = ((HadoopFileStoreConnection) _connection).getHadoopFS();
		String repo = ((HadoopFileStoreConnection) _connection).getHadoopConf()
				.baseDirectory();

		String baseDir = repo + "/" + name;

		try {

			Path wDir = new Path(baseDir);
			if (!fs.exists(wDir))
				fs.mkdirs(wDir);
			fs.setWorkingDirectory(wDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Repeatable repeatMe(RepeaterVariants parms) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getFileAsStream(String fileName, String group, ClassLoader cl)
			throws CtxException {
		
			
		FileSystem fs = ((HadoopFileStoreConnection) _connection).getHadoopFS();
		
		Path p = new Path(group + "/" + fileName);
		try {
			if(fs.exists(p)){
				FSDataInputStream in = fs.open(p);
				return in;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
