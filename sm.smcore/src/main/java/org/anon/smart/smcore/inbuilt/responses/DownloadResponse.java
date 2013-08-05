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
 * File:                org.anon.smart.smcore.inbuilt.responses.DownloadResponse.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 9, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.inbuilt.responses;

import org.anon.smart.d2cache.Reader;

/**
 * @author raooll
 *
 */
public class DownloadResponse {

	private Reader _reader;
	private long _size;
	private String fileName;
	private String group;
	private String flType;

	public DownloadResponse(Reader r, long size, String flnm, String grp,String type) {
		_reader = r;
		_size = size;
		fileName = flnm;
		group = grp;
		flType = type;
	}

	/**
	 * @return the _reader
	 */
	public Reader reader() {
		return _reader;
	}

	/**
	 * @return the _size
	 */
	public long size() {
		return _size;
	}

	/**
	 * @return the fileName
	 */
	public String fileName() {
		return fileName;
	}

	/**
	 * @return the group
	 */
	public String group() {
		return group;
	}

	/**
	 * @return the group
	 */
	public String fileType() {
		return flType;
	}

}
