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
 * File:                org.anon.smart.smcore.channel.client.pool.ClientConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A client config object that defines the connection for the client
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.client.pool;

public class ClientConfig
{
    private String clientName;
    private String server;
    private int port;
    private int secure = 0;
    //the parser can be standard ones or implemented external, 
    //if implemented external, they should have implemented the
    //a constructor accepting an InputStream
    private String parser = "string";

    public ClientConfig(String nm, String svr, int p, int sec, String parse)
    {
        clientName = nm;
        server = svr;
        port = p;
        secure = sec;
        parser = parse;
    }

    public ClientConfig()
    {
    }

    public String getServer() { return server; }
    public int getPort() { return port; }
    public boolean useSecure() { return (secure > 0); }
    public String getParser() 
    { 
        if ((parser == null) || (parser.length() <= 0) ||parser.equals("string"))
            return "org.anon.smart.smcore.channel.client.StringResponse";
        else if (parser.equals("json"))
            return "org.anon.smart.smcore.channel.client.JSONResponse";
        else
            return parser; 
    }
    public String getClientName() { return clientName; }

    public String getFormatter(String format)
    {
        if ((format == null) || (format.length() <= 0) || (format.equals("form")))
            return "org.anon.smart.smcore.channel.client.FormFormat";
        else if (format.equals("json"))
            return "org.anon.smart.smcore.channel.client.JSONFormat";

        return format;
    }
}

