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
 * File:                org.anon.smart.channels.http.netty.NettyRequestReader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A reader of netty requests
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http.netty;

import java.util.Map;
import java.util.List;
import java.io.InputStream;

import static org.jboss.netty.handler.codec.http.HttpHeaders.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;

import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.Route;
import org.anon.smart.channels.http.HTTPMessageReader;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class NettyRequestReader implements HTTPMessageReader
{
    public NettyRequestReader()
    {
    }

    public String getURI(Object msg)
    {
        HttpRequest request = (HttpRequest)msg;
        String uri = request.getUri();
        return uri;
    }

    public String getPath(Object msg)
    {
        HttpRequest request = (HttpRequest)msg;
        String uri = request.getUri();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String path = decoder.getPath();
        return path;
    }

    public List<Map.Entry<String, String>> getHeaders(Object msg)
    {
        HttpRequest request = (HttpRequest)msg;
        return request.getHeaders();
    }

    public InputStream contentStream(Object msg)
        throws CtxException
    {
        HttpRequest request = (HttpRequest)msg;
        ChannelBuffer buffer = request.getContent();
        ChannelBufferInputStream stream = new ChannelBufferInputStream(buffer);
        return stream;
    }

    public Object transmitObject(PData[] resp,Route r)
        throws CtxException
    {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        for (int i = 0; i < resp.length; i++)
        {
            StringBuffer buff = io().readStream(resp[i].cdata().data());
            response.setContent(ChannelBuffers.copiedBuffer(buff.toString(), CharsetUtil.UTF_8));
        }
        response.setHeader(CONTENT_TYPE, "application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());

        return response;
    }

    public Object transmitDefault()
        throws CtxException
    {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        String buff = "<HTML><H>You have reached the channel of SMART Platform.</H><b>Happy coding.</HTML>";
        response.setContent(ChannelBuffers.copiedBuffer(buff, CharsetUtil.UTF_8));
        response.setHeader(CONTENT_TYPE, "text/html");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Session-id, accept, cache-control, x-requested-with, content-type");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());

        return response;
    }

    public Object transmitException(Throwable t)
        throws CtxException
    {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR);
        String buff = "<HTML><H>You have hit an Exception.</H><b>" + t.getMessage() + "</HTML>";
        response.setContent(ChannelBuffers.copiedBuffer(buff, CharsetUtil.UTF_8));
        response.setHeader(CONTENT_TYPE, "text/html");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());

        return response;
    }

    public boolean isKeepAlive(Object msg)
    {
        HttpRequest request = (HttpRequest)msg;
        return HttpHeaders.isKeepAlive(request);
    }

    public boolean isOptionsRequest(Object msg)
    {
        if (msg instanceof HttpRequest)
        {
            HttpRequest request = (HttpRequest)msg;
            return request.getMethod().equals(HttpMethod.OPTIONS);
        }

        return false;
    }
}

