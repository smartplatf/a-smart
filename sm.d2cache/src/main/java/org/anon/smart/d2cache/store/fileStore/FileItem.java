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
 * File:                org.anon.smart.d2cache.store.fileStore.FileItem.java
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
package org.anon.smart.d2cache.store.fileStore;

import org.anon.smart.d2cache.store.StoreItem;

/**
 * @author raooll
 * 
 */
public class FileItem extends StoreItem {

	private String _fileName;
	private String _srcFldr;
	private String _destFldr;

	public FileItem(Object[] keys, Object obj) {
		super(keys, obj);
		// TODO Auto-generated constructor stub
	}

	public FileItem(String fileName, String src, String dest) {
		super(null, null);
		_fileName = fileName;
		_destFldr = dest;
		_srcFldr = src;

	}

	/**
	 * @return the _fileName
	 */
	public String getfileName() {
		return _fileName;
	}

	/**
	 * @param _fileName
	 *            the _fileName to set
	 */
	public void setfileName(String fileName) {
		this._fileName = fileName;
	}

	/**
	 * @return the _srcFldr
	 */
	public String getsrcFldr() {
		return _srcFldr;
	}

	/**
	 * @param _srcFldr
	 *            the _srcFldr to set
	 */
	public void setsrcFldr(String srcFldr) {
		this._srcFldr = srcFldr;
	}

	/**
	 * @return the _destFldr
	 */
	public String getdestFldr() {
		return _destFldr;
	}

	/**
	 * @param _destFldr
	 *            the _destFldr to set
	 */
	public void setdestFldr(String destFldr) {
		this._destFldr = destFldr;
	}

}
