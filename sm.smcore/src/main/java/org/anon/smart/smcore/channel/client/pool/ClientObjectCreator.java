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
 * File:                org.anon.smart.smcore.channel.client.pool.ClientObjectCreator
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A creator for a client object
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.client.pool;

import java.util.Map;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.utilities.config.Format;
import org.anon.utilities.pool.Pool;
import org.anon.utilities.pool.EntityPool;
import org.anon.utilities.pool.PoolEntity;
import org.anon.utilities.pool.EntityCreator;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

public class ClientObjectCreator implements EntityCreator
{
    private static Map<String, Pool> CLIENT_POOLS = new ConcurrentHashMap<String, Pool>();

    private ClientConfig _parsed;

    //can be got from a file
    private ClientObjectCreator(String cfg)
        throws CtxException
    {
        _parsed = readconfig(cfg);
    }

    //can be got from a configuration
    private ClientObjectCreator(ClientConfig cfg)
    {
        _parsed = cfg;
    }

    private ClientConfig readconfig(String cfg)
        throws CtxException
    {
        String file = cfg + ".cfg"; //so if someone wanted smsgateway, then it will look for smsgateway.cfg file?
        InputStream str = this.getClass().getClassLoader().getResourceAsStream(file);
        Format fmt = config().readYMLConfig(str);
        Map values = fmt.allValues();
        ClientConfig conf = convert().mapToObject(ClientConfig.class, values);
        return conf;
    }

    public PoolEntity newEntity()
        throws CtxException
    {
        return new HTTPClientObject(_parsed);
    }

    public static Pool getPool(String cfg)
        throws CtxException
    {
        Pool pool = CLIENT_POOLS.get(cfg);
        if (pool == null)
        {
            pool = new EntityPool(new ClientObjectCreator(cfg));
            CLIENT_POOLS.put(cfg, pool);
        }

        pool = CLIENT_POOLS.get(cfg);
        return pool;
    }

    public static Pool getPool(ClientConfig cfg)
        throws CtxException
    {
        Pool pool = CLIENT_POOLS.get(cfg.getClientName());
        if (pool == null)
        {
            pool = new EntityPool(new ClientObjectCreator(cfg));
            CLIENT_POOLS.put(cfg.getClientName(), pool);
        }

        pool = CLIENT_POOLS.get(cfg.getClientName());
        return pool;
    }

    public static void cleanup()
        throws CtxException
    {
        CLIENT_POOLS.clear();
        CLIENT_POOLS = null;
    }
}

