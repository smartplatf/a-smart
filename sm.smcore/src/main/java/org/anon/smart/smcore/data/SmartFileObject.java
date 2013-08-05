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
 * File:                org.anon.smart.smcore.data.UploadData.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 23, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author raooll
 * 
 */
public class SmartFileObject implements java.io.Serializable{


	public List<Object> _keyList;

	public UUID _uploadId;

	public UUID _itemId;

	public String _fieldName;

	public String _fileName;

	public String _fileType;

	public String _md5sum;

	public String _fileLocation;

	public String _filePath;

	public Date _dateOfUpload;

	public String _fileSrc;
	
	public String _group;
	
	public long _size;

	public SmartFileObject() {
		_uploadId = UUID.randomUUID();
		_keyList = new ArrayList<Object>();
		_keyList.add(_uploadId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SmartFileObject [_keyList=" + _keyList + ", _uploadId="
				+ _uploadId + ", _itemId=" + _itemId + ", _fieldName="
				+ _fieldName + ", _fileName=" + _fileName + ", _fileType="
				+ _fileType + ", _md5sum=" + _md5sum + ", _fileLocation="
				+ _fileLocation + ", _filePath=" + _filePath
				+ ", _dateOfUpload=" + _dateOfUpload + ", _fileSrc=" + _fileSrc
				+ ", _group=" + _group + ", _size=" + _size + "]";
	}

	

	
	
}
