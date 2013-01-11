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
 * File:                org.anon.smart.channels.http.HTTPConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Configuration required for http channel
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http;

import org.anon.smart.channels.shell.SCType;
import org.anon.smart.channels.shell.SCFactory;
import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.smart.channels.shell.DataInstincts;
import org.anon.smart.channels.distill.Rectifier;

public class HTTPConfig implements ExternalConfig
{
    private int _port;
    private boolean _client;
    private boolean _secure;
    private String _server;
    private String _keyStoreFile;
    private String _keyStorePassword;
    private String _keyAlgorithm;
    private DataInstincts _instinct;

    public HTTPConfig(int port, boolean secure)
    {
        _port = port;
        _secure = secure;
        _server = "localhost";
        _keyAlgorithm = "SunX509";
        _keyStorePassword = "";
        _keyStoreFile = "cert.jks";
    }

    public void setServer(String server)
    {
        _server = server;
    }

    public void setClient() { _client = true; }

    public void setKeyStore(String algo, String pwd, String storefile)
    {
        _keyAlgorithm = algo;
        _keyStorePassword = pwd;
        _keyStoreFile = storefile;
    }

    public int port() { return _port; }
    public String server() { return _server; }
    public boolean makeSecure() { return _secure; }
    public String keyAlgorithm() { return _keyAlgorithm; }
    public String keyStoreFile() { return _keyStoreFile; }
    public String keyStorePwd() { return _keyStorePassword; }

    public void setInstincts(DataInstincts d) { _instinct = d; }

    public void setRectifierInstinct(Rectifier rectifier, HTTPDataFactory fact) 
    { 
        if (!_client) 
            _instinct = new HTTPDataInstinct(rectifier, fact); 
        else
            _instinct = new HTTPClientDataInstinct(rectifier, fact);
    }

    public DataInstincts instinct() { return _instinct; }
    public SCType scType() { return SCType.external; }
    public SCFactory creator() 
    { 
        if (!_client) 
            return new HTTPServerFactory(); 
        else
            return new HTTPClientFactory();
    }
}

