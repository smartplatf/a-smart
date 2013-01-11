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
 * File:                org.anon.smart.channels.http.ServerSSLContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An ssl context to be used in https connections
 *
 * ************************************************************
 * */

package org.anon.smart.channels.http;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public class ServerSSLContext
{
    private String _keyStorePwd;
    private String _keyStoreFile;
    private String _keyAlgorithm;
    private SSLContext _serverContext;

    public ServerSSLContext(HTTPConfig cfg)
    {
        _keyStorePwd = cfg.keyStorePwd();
        _keyStoreFile = cfg.keyStoreFile();
        _keyAlgorithm = cfg.keyAlgorithm();
        if (_keyAlgorithm == null)
            _keyAlgorithm = "SunX509";
    }

    public SSLContext getServerContext()
    {
        return _serverContext;
    }

    public void initialize()
        throws CtxException
    {
        try
        {
            SSLContext serverContext;
            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(_keyStoreFile);
            ks.load(is, _keyStorePwd.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(_keyAlgorithm);
            kmf.init(ks, _keyStorePwd.toCharArray());

            serverContext = SSLContext.getInstance("TLS");
            serverContext.init(kmf.getKeyManagers(), null, null);
            _serverContext = serverContext;
        }
        catch (Exception ex)
        {
            except().rt(ex, new CtxException.Context("Failed to initialize the server-side SSLContext", "Exception"));
        }
    }

    public SSLEngine createEngine(boolean clntmode)
        throws CtxException
    {
        initialize();
        SSLEngine engine = _serverContext.createSSLEngine();
        engine.setUseClientMode(clntmode);
        return engine;
    }
}

