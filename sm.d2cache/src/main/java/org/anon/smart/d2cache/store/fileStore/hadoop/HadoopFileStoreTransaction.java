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
 * File:                org.anon.smart.d2cache.store.fileStore.HadoopFileStoreTransaction.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.anon.smart.d2cache.store.AbstractStoreTransaction;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.smart.d2cache.store.fileStore.FileStoreTransaction;
import org.anon.utilities.exception.CtxException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;

import static org.anon.utilities.services.ServiceLocator.*;

/**
 * @author raooll
 * 
 */
public class HadoopFileStoreTransaction extends AbstractStoreTransaction
		implements FileStoreTransaction {

	private HashMap<Object, String> files;

	public HadoopFileStoreTransaction(UUID txnid, StoreConnection conn) {
		super(txnid, conn);
		files = new HashMap<Object, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.store.StoreTransaction#commit()
	 */
	@Override
	public void commit() throws CtxException {

		FileSystem hdfs = ((HadoopFileStoreConnection) _connection)
				.getHadoopFS();

		assertion().assertNotNull(hdfs, "Hadoop FileSystem is null");

		String repo = hdfs.getWorkingDirectory().toUri().toString();

		for (Object fi : files.keySet()) {
			try {

				String filePath = (String) fi;
				String[] tmp = filePath.split("/");
				String fileName = tmp[tmp.length - 1];

				Path fldr = new Path(files.get(fi));
				if (!hdfs.exists(fldr))
					hdfs.mkdirs(fldr);

				hdfs.copyFromLocalFile(true, new Path(filePath),
						new Path(files.get(fi) + "/" + fileName));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.store.StoreTransaction#rollback()
	 */
	@Override
	public void rollback() throws CtxException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.AbstractStoreTransaction#addRecord(java.
	 * lang.String, java.lang.Object, java.lang.Object, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public StoreRecord addRecord(String group, Object primarykey, Object curr,
			Object orig, Object relatedKey) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.smart.d2cache.store.AbstractStoreTransaction#createNewRecord
	 * (java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected StoreRecord createNewRecord(String group, Object primarykey,
			Object curr, Object orig) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Object file, String group) throws CtxException {
		files.put(file, group);

	}

}
