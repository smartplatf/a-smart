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
 * File:                org.anon.smart.channels.http.upload.UploadRequest.java
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
package org.anon.smart.channels.http.upload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * @author raooll
 * 
 */
public class UploadRequest {

	private HttpRequest r;

    private String customGroup;

    private Map postData;

	private Map msg;

	private String uri;

	private String[] params;

	public UploadRequest(HttpRequest h, Map o, String grp, Map post) {
		r = h;
		params = r.getUri().split("/");
		msg = o;
        customGroup = grp;
        postData = post;
	}

    public String getCustomGroup() {
        return customGroup;
    }

    public Map getPostData() {
        return postData;
    }

	public HttpRequest getRequest() {
		return r;
	}

	public Map getMsg() {
		return msg;
	}

    public String postDataString() {
		String ret = "{";
		for (Object k : postData.keySet()) {
			ret = ret + "'" + k.toString() + "'" + ":" + "'" + postData.get(k) + "'"
					+ ",";
		}
		ret = ret.substring(0, ret.length() - 1);
		ret = ret + "}";
		return ret;
    }

	public String toString() {
		String ret = "{";
		for (Object k : msg.keySet()) {
			ret = ret + "'" + (k.toString()).replace('\\', '/') + "'" + ":" + "'" + msg.get(k) + "'"
					+ ",";
		}
		ret = ret.substring(0, ret.length() - 1);
		ret = ret + "}";
		return ret;
	}

	public String tenant() {
		return params[2];
	}
}
