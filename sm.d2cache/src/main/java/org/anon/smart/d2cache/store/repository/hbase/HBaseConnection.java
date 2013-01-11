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
 * File:                org.anon.smart.d2cache.store.repository.hbase.HBaseConnection
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A connection for hbase
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import java.util.UUID;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.smart.d2cache.store.StoreConnection;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class HBaseConnection implements StoreConnection, Constants
{
    private Configuration _hadoopConf;
    private HBaseConfig _config;
    private HBaseCRUD _crud;
    private String _related; //this is the tenant?
    private String _name; //this is like global, user etc

    public HBaseConnection()
    {
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new HBaseConnection();
    }

    public void connect(StoreConfig cfg)
        throws CtxException
    {
        try
        {
            _config = (HBaseConfig)cfg;
            _hadoopConf = HBaseConfiguration.create();
            if (!_config.isLocal())
            {
                _hadoopConf.set(ZOOKEEPER_QUORUM, _config.zookeeperQuorum());
                _hadoopConf.set(ZOOKEEPER_CLIENT_PORT, _config.zookeeperPort());
                _hadoopConf.set(HBASE_MASTER, _config.hbaseMaster());
            }

            HBaseAdmin.checkHBaseAvailable(_hadoopConf);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("HBaseConnection.connect", "Exception"));
        }
    }

    public void open(String related, String name)
        throws CtxException
    {
        _related = related;
        _name = name;
    }

    String getTableName(String name)
    {
        //The name will be in the format related__globalname__name
        //assumption is that the name contains the version in it.
        return "__" + _related + "__" + _name + "__" + name;
    }

    public void createMetadata(String name, Class cls)
        throws CtxException
    {
        try
        {
            String tablename = getTableName(name);
            _crud.newTable(tablename, new String[] { SYNTHETIC_COL_FAMILY, DATA_COL_FAMILY });
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("HBaseConnection.createMetadata", "Exception"));
        }
    }

    public StoreTransaction startTransaction(UUID txn)
        throws CtxException
    {
        return new HBaseTransaction(txn, this);
    }

    public void close()
        throws CtxException
    {
        //nothing to do?
    }

    public Object find(String group, Object key)
        throws CtxException
    {
        try
        {
            String tablename = getTableName(group);
            //assumption is that the key passed here is the primary key
            //retrieval by anyother means is not supported currently>
            Map<String, byte[]> obj = _crud.oneRecord(tablename, key.toString());
            //this will have to be changed correctly. TODO: this has to be corrected
            String clsName = new String(obj.get(CLASSNAME));
            Object ret = convert().mapToObject(Class.forName(clsName), obj);
            return ret;
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("HBaseConnection.find", "Exception"));
        }
        return null;
    }

    public List<Object> search(String group, String query)
        throws CtxException
    {
        //TODO: can be done with filters and scans
        return null;
    }

    public HBaseCRUD getCRUD() { return _crud; }
}

