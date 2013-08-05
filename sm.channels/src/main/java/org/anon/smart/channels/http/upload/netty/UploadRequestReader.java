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
 * File:                org.anon.smart.channels.http.upload.netty.UploadRequestReader.java
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
package org.anon.smart.channels.http.upload.netty;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.anon.smart.channels.http.netty.NettyRequestReader;
import org.anon.smart.channels.http.netty.NettyResponseReader;
import org.anon.smart.channels.http.upload.UploadRequest;
import org.anon.utilities.exception.CtxException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

/**
 * @author raooll
 * 
 */
public class UploadRequestReader extends NettyRequestReader {

	private String _uri;

	public UploadRequestReader(String uri) {
		super();
		// TODO Auto-generated constructor stub
	}

	public InputStream contentStream(Object msg) throws CtxException {
		UploadRequest r = (UploadRequest) msg;
		String temp = "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'"
				+ r.tenant()
				+ "'}, "
				+ "'files':"
				+ r.toString()
				+ " ,'uploadUri':'" + r.getRequest().getUri() + "'}";

		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(temp,
				CharsetUtil.UTF_8);
		ChannelBufferInputStream stream = new ChannelBufferInputStream(buffer);
		return stream;
	}

	public List<Map.Entry<String, String>> getHeaders(Object msg) {
		UploadRequest r = (UploadRequest) msg;
		return r.getRequest().getHeaders();
	}

	public boolean isKeepAlive(Object msg) {
		UploadRequest r = (UploadRequest) msg;
		return HttpHeaders.isKeepAlive(r.getRequest());
	}

	public String getURI(Object msg) {
		UploadRequest r = (UploadRequest) msg;
		return r.getRequest().getUri().toString();
	}

	public String getPath(Object msg) {
		UploadRequest r = (UploadRequest) msg;
		String uri = r.getRequest().getUri().toString();
		QueryStringDecoder decoder = new QueryStringDecoder(uri);
		String path = decoder.getPath();
		return path;
	}

}
