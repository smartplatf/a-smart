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
 * File:                org.anon.smart.channels.http.upload.data.UploadPData.java
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
package org.anon.smart.channels.http.upload.data;

import org.anon.smart.channels.data.CData;
import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.data.PData;

/**
 * @author raooll
 *
 */
public class UploadPData extends PData {

	private long _size = 0;
	
	private String fileName;
	private String flType;
	
	public UploadPData(DScope scope, CData data,long size,String flnm,String flt) {
		super(scope, data);
		this._size = size;
		fileName =flnm;
		flType = flt;
		// TODO Auto-generated constructor stub
	}
	
	public long size(){
		return _size;
	}
	
	public String fileName(){
		return fileName;
	}
	
	public String fileType(){
		return flType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UploadPData [_size=" + _size + ", fileName=" + fileName
				+ ", flType=" + flType + "]";
	}

}
