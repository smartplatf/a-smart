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
 * File:                org.anon.smart.channels.http.netty.NettyResponseReader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A response reader in netty
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http.netty;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.InputStream;

import static org.jboss.netty.handler.codec.http.HttpHeaders.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;

import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.http.HTTPRequestPData;
import org.anon.smart.channels.http.HTTPMessageReader;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class NettyResponseReader implements HTTPMessageReader
{
    private String _uri;

    public NettyResponseReader(String uri)
    {
        _uri = uri;
    }

    public void setURI(String uri)
    {
        _uri = uri;
    }

    public String getURI(Object msg)
    {
        return _uri;
    }

    public String getPath(Object msg)
    {
        String uri = _uri;
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String path = decoder.getPath();
        return path;
    }

    public List<Map.Entry<String, String>> getHeaders(Object msg)
    {
        HttpMessage request = (HttpMessage)msg;
        return request.getHeaders();
    }

    public InputStream contentStream(Object msg)
        throws CtxException
    {
        HttpMessage request = (HttpMessage)msg;
        ChannelBuffer buffer = request.getContent();
        ChannelBufferInputStream stream = new ChannelBufferInputStream(buffer);
        return stream;
    }

    public Object transmitObject(PData[] req)
        throws CtxException
    {
        HttpRequest request = new DefaultHttpRequest(HTTP_1_1, POST, _uri);
        String contentType = "application/json";
        Map<String, String> headers = new HashMap<String, String>();
        for (int i = 0; i < req.length; i++)
        {
            StringBuffer buff = io().readStream(req[i].cdata().data());
            request.setContent(ChannelBuffers.copiedBuffer(buff.toString(), CharsetUtil.UTF_8));
            if (req[i] instanceof HTTPRequestPData)
            {
                HTTPRequestPData hpdata = (HTTPRequestPData)req[i];
                if ((hpdata.getContentType() != null) && (hpdata.getContentType().length() > 0))
                    contentType = hpdata.getContentType();

                Map<String, String> hdrs = hpdata.getHeaders();
                for (String name : hdrs.keySet())
                    headers.put(name, hdrs.get(name));
            }
        }
        request.setHeader(CONTENT_TYPE, contentType);
        request.setHeader("Access-Control-Allow-Origin", "*");
        request.setHeader(CONTENT_LENGTH, request.getContent().readableBytes());

        for (String nm : headers.keySet())
            request.setHeader(nm, headers.get(nm));

        return request;
    }

    public Object transmitDefault()
        throws CtxException
    {
        HttpRequest request = new DefaultHttpRequest(HTTP_1_1, POST, _uri);
        String buff = "";
        request.setContent(ChannelBuffers.copiedBuffer(buff, CharsetUtil.UTF_8));
        request.setHeader(CONTENT_TYPE, "application/json");
        request.setHeader("Access-Control-Allow-Origin", "*");
        request.setHeader(CONTENT_LENGTH, request.getContent().readableBytes());
        return request;
    }

    public Object transmitException(Throwable t)
        throws CtxException
    {
        HttpRequest request = new DefaultHttpRequest(HTTP_1_1, POST, _uri);
        String buff = "<HTML><H>You have hit an Exception.</H><b>" + t.getMessage() + "</HTML>";
        request.setContent(ChannelBuffers.copiedBuffer(buff, CharsetUtil.UTF_8));
        request.setHeader(CONTENT_TYPE, "text/html");
        request.setHeader("Access-Control-Allow-Origin", "*");
        request.setHeader(CONTENT_LENGTH, request.getContent().readableBytes());

        return request;
    }

    public boolean isKeepAlive(Object msg)
    {
        HttpMessage request = (HttpMessage)msg;
        return HttpHeaders.isKeepAlive(request);
    }
}

