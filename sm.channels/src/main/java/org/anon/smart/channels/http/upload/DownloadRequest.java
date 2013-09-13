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
 * File:                org.anon.smart.channels.http.upload.DownloadRequest.java
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
package org.anon.smart.channels.http.upload;

import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * @author raooll
 * 
 */
public class DownloadRequest {

	private HttpRequest _hR;
	String[] params;

	public DownloadRequest(HttpRequest gr) {
		_hR = gr;
		params = _hR.getUri().split("/");
        System.out.println("DownloadRequest is: " + params.length + ":" + _hR.getUri());
	}

	public HttpRequest getRequest() {
		return _hR;
	}

	public Map getMsg() {
		return null;
	}

	public String toString() {
		return params[4];
	}
	
	public String flow(){
		return params[2];
	}
	
	public String tenant(){
		return params[1];
	}
}
