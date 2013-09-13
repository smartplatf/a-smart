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
 * File:                org.anon.smart.channels.http.HTTPMessageReader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A route that can identify and process http related requests
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http;

import java.util.Map;
import java.util.List;
import java.io.InputStream;

import org.anon.smart.channels.MessageReader;
import org.anon.smart.channels.data.PData;

import org.anon.utilities.exception.CtxException;

public interface HTTPMessageReader extends MessageReader
{
    public String getURI(Object msg);
    public String getPath(Object msg);
    public List<Map.Entry<String, String>> getHeaders(Object msg);
    public InputStream contentStream(Object msg)
        throws CtxException;
    public boolean isKeepAlive(Object msg);
    public boolean isOptionsRequest(Object msg);
}

