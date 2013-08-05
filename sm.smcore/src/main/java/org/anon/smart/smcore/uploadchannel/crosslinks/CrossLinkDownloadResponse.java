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
 * File:                org.anon.smart.smcore.uploadchannel.distill.CrossLinkDownloadResponse.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 10, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.uploadchannel.crosslinks;

import java.io.InputStream;

import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.store.fileStore.FileStoreReader;
import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 * 
 */
public class CrossLinkDownloadResponse extends CrossLinker {

	public CrossLinkDownloadResponse(Object o) {
		super(o);
	}

	/**
	 * @return the _reader
	 * @throws CtxException
	 */
	public Object reader() throws CtxException {
		return linkMethod("reader");
	}

	/**
	 * @return the _size
	 * @throws CtxException
	 */
	public long size() throws CtxException {
		return (Long) linkMethod("size");
	}

	/**
	 * @return the fileName
	 * @throws CtxException
	 */
	public String fileName() throws CtxException {
		return (String) linkMethod("fileName");
	}

	/**
	 * @return the group
	 * @throws CtxException
	 */
	public String group() throws CtxException {
		return (String) linkMethod("group");
	}
	
	public String fileType() throws CtxException{
		return (String) linkMethod("fileType");
	}

}
