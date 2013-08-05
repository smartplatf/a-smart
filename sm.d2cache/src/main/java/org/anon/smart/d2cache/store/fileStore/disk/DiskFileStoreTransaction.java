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
 * File:                org.anon.smart.d2cache.store.fileStore.DiskFileStoreTransaction.java
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.anon.smart.d2cache.store.AbstractStoreTransaction;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.smart.d2cache.store.fileStore.FileStoreTransaction;
import org.anon.utilities.exception.CtxException;
import org.apache.commons.io.FileUtils;

import static org.anon.utilities.services.ServiceLocator.except;

/**
 * @author raooll
 * 
 */
public class DiskFileStoreTransaction extends AbstractStoreTransaction
		implements FileStoreTransaction {

	private HashMap<Object, String> files;

	public DiskFileStoreTransaction(UUID txnid, StoreConnection conn) {
		super(txnid, conn);
		files = new HashMap<Object, String>();;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.smart.d2cache.store.StoreTransaction#commit()
	 */
	@Override
	public void commit() throws CtxException {
		
		String repo =( (DiskFileStoreConnection) _connection).getDiskConf().baseDirectory();
		try{
		
		for (Object fi : files.keySet()) {

			String folder = files.get(fi);
			String filePath = (String) fi;
			String[] tmp = filePath.split("/");
			String fileName = tmp[tmp.length - 1];

			File destFile = new File(repo + "/" + folder + "/" + fileName);

			File srcFile = new File(filePath);
			FileUtils.copyFile(srcFile, destFile,false);
			
			srcFile.deleteOnExit();

		}
		}catch(Exception e){
			except().rt(e, new CtxException.Context("DiskFileStoreTransaction.commit", "Exception"));			
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
