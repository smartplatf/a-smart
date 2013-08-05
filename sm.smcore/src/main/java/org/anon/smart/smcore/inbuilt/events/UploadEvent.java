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
 * File:                org.anon.smart.smcore.inbuilt.events.UploadEvent.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 21, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.inbuilt.events;

import java.util.HashMap;
import java.util.Map;

/**
 * @author raooll
 * 
 */
public class UploadEvent implements java.io.Serializable {

	private Map<String, String> files;
	
	private String uploadUri ;

	public Map getFiles() {
		
		System.out.println("UploadURI " + uploadUri);
		System.out.println("--------------- " + files.toString()
				+ "--------------" + files.size());
		return files;
	}

	public UploadEvent() {

	}
	
	public String getUploadUri(){
		return uploadUri;
	}

	@Override
	public String toString() {
		return "UploadEvent [fileName=" + files + "]";
	}

}
