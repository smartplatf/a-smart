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
 * File:                org.anon.smart.channels.http.upload.netty.DownloadRequestReader.java
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
package org.anon.smart.channels.http.upload.netty;

import static org.anon.utilities.services.ServiceLocator.io;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CACHE_CONTROL;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Values.MAX_AGE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.anon.smart.channels.Route;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.http.netty.NettyRequestReader;
import org.anon.smart.channels.http.upload.DownloadRequest;
import org.anon.smart.channels.http.upload.data.UploadPData;
import org.anon.utilities.exception.CtxException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.stream.ChunkedStream;
import org.jboss.netty.util.CharsetUtil;

/**
 * @author raooll
 * 
*/
public class DownloadRequestReader extends NettyRequestReader {

	public DownloadRequestReader() {

	}

	public InputStream contentStream(Object msg) throws CtxException {
		DownloadRequest r = (DownloadRequest) msg;
		String temp = "{'SmartFileObject':{'___smart_action___':'lookup', '___smart_value___':'"+ r.toString()+"' , '___smart_flow___':'" + r.flow() + "'}, "
				+ "'fileName':'"
				+ r.toString()
				+ "'}";

		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(temp,
				CharsetUtil.UTF_8);
		ChannelBufferInputStream stream = new ChannelBufferInputStream(buffer);
		return stream;
	}

	public List<Map.Entry<String, String>> getHeaders(Object msg) {
		DownloadRequest r = (DownloadRequest) msg;
		return r.getRequest().getHeaders();
	}

	public boolean isKeepAlive(Object msg) {
		DownloadRequest r = (DownloadRequest) msg;
		return HttpHeaders.isKeepAlive(r.getRequest());
	}

	public String getURI(Object msg) {
		DownloadRequest r = (DownloadRequest) msg;
		String uri = r.getRequest().getUri().toString();
		return uri.substring(0, uri.lastIndexOf("/"));
	}

	public String getPath(Object msg) {
		DownloadRequest r = (DownloadRequest) msg;
		String uri = r.getRequest().getUri().toString();
		QueryStringDecoder decoder = new QueryStringDecoder(uri);
		String path = decoder.getPath();
		return path;
	}
	
	public Object transmitObject(PData[] pd, Route _route) throws CtxException {
	
		System.out.println("---------------- serving download content -------------- ");	
		
		if(pd[0] instanceof UploadPData){
		     for (PData p : pd){
		        UploadPData up = (UploadPData) p;
		        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		        response.setHeader(CONTENT_LENGTH, up.size());
		        response.setHeader(CONTENT_TYPE, up.fileType());
		        response.setHeader(CACHE_CONTROL, MAX_AGE + "=" + 31536000);
		
		        _route.send(response);
		
		        ChunkedStream flStrm = new ChunkedStream(up.cdata().data());
		        _route.send(flStrm);
		    }
		}
		else{
			return	super.transmitObject(pd,_route);		
		}
		
		
		return null;
	}

}
