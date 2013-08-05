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
 * File:                org.anon.smart.d2cache.store.fileStore.DiskFileStore.java
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
import java.io.FileInputStream;
import java.io.InputStream;
import org.anon.smart.d2cache.store.AbstractStore;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.fileStore.FileStore;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;

import static org.anon.utilities.services.ServiceLocator.except;
import org.anon.utilities.exception.CtxException;
/**
 * @author raooll
 *
 */
public class DiskStore extends AbstractStore implements FileStore {

	public DiskStore(StoreConnection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create(String name, Class cls) throws CtxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Repeatable repeatMe(RepeaterVariants parms) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getFileAsStream(String fileName, String group, ClassLoader cl)
			throws CtxException {
		
		try{
		String baseDir = ((DiskFileStoreConnection) _connection).getDiskConf()
				.baseDirectory();
		File reqFile = new File(baseDir + "/" + group + "/" + fileName);
		if (reqFile.exists()){
			return new FileInputStream(reqFile);
		}
		}
		catch(Exception e){
			except().rt(e, new CtxException.Context("DiskStore.getFileAsStream", "Exception"));
		}
		return null;
	}
}
