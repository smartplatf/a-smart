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
 * File:                org.anon.smart.channels.http.HTTPRequestPData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data to be sent on client request
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.data.CData;

public class HTTPRequestPData extends PData
{
    private Map<String, String> _headers;
    private String _contentType;

    public HTTPRequestPData(DScope scope, CData content)
    {
        super(scope, content);
        _headers = new HashMap<String, String>();
    }

    public void addHeader(String name, String value)
    {
        _headers.put(name, value);
    }

    public void setContentType(String type) { _contentType = type; }
    public Map<String, String> getHeaders() { return _headers; }
    public String getContentType() { return _contentType; }
}

