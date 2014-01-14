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
 * File:                org.anon.smart.smcore.channel.client.pool.HTTPClientObject
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A client pool for http connections
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.client.pool;

import java.util.Map;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.smcore.anatomy.SMCoreContext;
import org.anon.smart.smcore.channel.client.GenericPData;
import org.anon.smart.smcore.channel.client.GenericDataFactory;
import org.anon.smart.smcore.channel.client.GenericResponseHandler;

import org.anon.utilities.pool.Pool;
import org.anon.utilities.pool.PoolEntity;
import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class HTTPClientObject implements PoolEntity
{
    private PoolEntity _next;
    private Pool _pool;
    private ClientConfig _config;
    private HTTPConfig _httpconfig;
    private GenericResponseHandler _respHandler;

    private HTTPClientChannel _channel;

    public HTTPClientObject(ClientConfig cfg)
    {
        _config = cfg;
        System.out.println("Creating a configuration for: " + cfg.getPort());
        _httpconfig = new HTTPConfig(cfg.getPort(), cfg.useSecure());
        _httpconfig.setClient();
        _httpconfig.setServer(_config.getServer());
        //checking if this will work after that will fix
        _httpconfig.setKeyStore("SunX509", "changeit", "cacerts.cer"); //this is bundled with out server and hence does not change
        Rectifier rectify = new Rectifier();
        _respHandler = new GenericResponseHandler();
        rectify.addStep(_respHandler);
        //TODO:rectify.addStep(new GenericRequestHandler());
        _httpconfig.setRectifierInstinct(rectify, new GenericDataFactory());
    }

    private void connect(boolean wait)
        throws CtxException
    {
        _respHandler.clearOut(wait);
        if (_channel == null)
        {
            SCShell shell = SMCoreContext.coreContext().smartChannelShell();
            _channel = (HTTPClientChannel)shell.addChannel(_httpconfig);
            _channel.connect();
        }
    }

    public Object postData(String uri, String post, boolean wait)
        throws CtxException
    {
        connect(wait);
        ByteArrayInputStream istr = new ByteArrayInputStream(post.getBytes());
        PData data = new GenericPData(null, new ContentData(istr));
        _channel.post(uri, new PData[] { data });
        return handleResult(wait);
    }

    public Object getData(String uri, boolean wait)
        throws CtxException
    {
        connect(wait);
        _channel.get(uri);
        return handleResult(wait);
    }

    private Object handleResult(boolean wait)
        throws CtxException
    {
        Object ret = null;
        if (wait)
        {
            _respHandler.waitForResponse();
            InputStream str = _respHandler.getResponse();
            if ((_config.getParser() != null) && (_config.getParser().length() > 0))
            {
                CrossLinkAny cl = new CrossLinkAny(_config.getParser(), this.getClass().getClassLoader());
                ret = cl.create(new Class[] { InputStream.class }, new Object[] { str });
            }
            _channel.disconnect();
            _channel = null;
            //TODO:return new AssertJSONResponse(jret);
        }

        return ret;
    }

    public PoolEntity nextEntity()
    {
        return _next;
    }

    public void setNextEntity(PoolEntity entity)
    {
        _next = entity;
    }

    public Pool pool()
    {
        return _pool;
    }

    public void storePool(Pool p)
    {
        _pool = p;
    }

    public String getFormatted(Map map, String format)
        throws CtxException
    {
        String formatter = _config.getFormatter(format);
        CrossLinkAny cl = new CrossLinkAny(formatter, this.getClass().getClassLoader());
        Object obj = cl.create(new Class[] { Map.class }, new Object[] { map });
        assertion().assertNotNull(obj, "Cannot create a formatter of type " + formatter);
        return obj.toString();
    }

    public String getObjectFormatted(Object val, String format)
        throws CtxException
    {
        String formatter = _config.getFormatter(format);
        CrossLinkAny cl = new CrossLinkAny(formatter, this.getClass().getClassLoader());
        Object obj = cl.create(new Class[] { Object.class }, new Object[] { val });
        assertion().assertNotNull(obj, "Cannot create a formatter of type " + formatter);
        return obj.toString();
    }
}

