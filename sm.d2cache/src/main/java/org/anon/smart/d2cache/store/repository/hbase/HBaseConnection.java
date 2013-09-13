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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

import org.anon.smart.d2cache.CacheableObject;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.ListParams;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.reflect.CVisitor;
import org.anon.utilities.services.ServiceLocator;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class HBaseConnection implements StoreConnection, Constants {
	private static Configuration _hadoopConf;
	private HBaseCRUD _crud;
	private String _name; // this is like global, user etc

	public HBaseConnection() {
	}

	private static Configuration getHadoopConfig(StoreConfig cfg) {
		HBaseConfig config = (HBaseConfig) cfg;
		if (_hadoopConf == null) {
			_hadoopConf = HBaseConfiguration.create();
			if (!config.isLocal()) {
				_hadoopConf.set(ZOOKEEPER_QUORUM, config.zookeeperQuorum());
				_hadoopConf.set(ZOOKEEPER_CLIENT_PORT, config.zookeeperPort());
				_hadoopConf.set(HBASE_MASTER, config.hbaseMaster());
			}

		}

		return _hadoopConf;
	}

	public Repeatable repeatMe(RepeaterVariants vars) throws CtxException {
		return new HBaseConnection();
	}

	public void connect(StoreConfig cfg) throws CtxException {
		System.out.println("------------ CONNETCING TO HBASE -------------");
		try {
			Configuration hadoopConfig = getHadoopConfig(cfg);

			HBaseAdmin.checkHBaseAvailable(hadoopConfig);
			_crud = new HBaseCRUD(hadoopConfig);
		} catch (Exception e) {
			except().rt(
					e,
					new CtxException.Context("HBaseConnection.connect",
							"Exception"));
		}
	}

	public void open(String name) throws CtxException {
		_name = name;
	}

	String getTableName(String name) {
		// The name will be in the format related__globalname__name
		// assumption is that the name contains the version in it.
		return "__" + _name + "__" + name;
	}

	public void createMetadata(String name, Class cls) throws CtxException {
		try {
			String tablename = getTableName(name);
			_crud.newTable(tablename, new String[] { SYNTHETIC_COL_FAMILY,
					DATA_COL_FAMILY });
		} catch (Exception e) {
			except().rt(
					e,
					new CtxException.Context("HBaseConnection.createMetadata",
							"Exception"));
		}
	}

	public void deleteMetadata(String name) throws CtxException {
		try {
			String tableName = getTableName(name);
			_crud.deleteTable(tableName);
		} catch (Exception e) {
			except().rt(
					e,
					new CtxException.Context("HBaseConnection.deleteMetadata",
							"Exception"));
		}
	}

	public StoreTransaction startTransaction(UUID txn) throws CtxException {
		return new HBaseTransaction(txn, this);
	}

	public void close() throws CtxException {
		// nothing to do?
	}

	/*
	 * public void put(String group, Object key, Object obj) throws CtxException
	 * { try { String tableName = getTableName(group); Put p =
	 * _crud.newRecord((String) key, DATA_COL_FAMILY, "DATA", obj); List<Put>
	 * recordList = new ArrayList<Put>(); recordList.add(p);
	 * _crud.putRecords(tableName, recordList); } catch(Exception ex) {
	 * except().rt(ex, new CtxException.Context("HBaseConnection.put",
	 * "Exception")); } }
	 */
	public Object find(String group, Object key) 
	    throws CtxException 
	{
		try {
			String tablename = getTableName(group);
			// assumption is that the key passed here is the primary key
			// retrieval by any other means is not supported currently>
			Map<String, byte[]> obj = _crud.oneRecord(tablename, key);
			// this will have to be changed correctly. TODO: this has to be
			// corrected

			//System.out.println("Got this from Store:" + obj);
			if ((obj != null) && (obj.containsKey(CLASSNAME))) {
				String clsName = new String(obj.get(CLASSNAME));
				CVisitor visitor = new ObjectCreatorFromMap(obj);
				Object ret = convert().recordMapToObject(
						Class.forName(clsName), visitor);

				if (ret instanceof RelatedObject) {
					
					RelatedObject relatedObj = (RelatedObject) ret;
					System.out.println("Found a related Object...fetching Primary Object for key:"+relatedObj.getRelatedKey());
					ret = find(group, relatedObj.getRelatedKey());
				}

				System.out.println("------>Created the Object:" + ret);
				// Init transient here
				ServiceLocator.assertion().assertTrue(
						(ret instanceof CacheableObject),
						ret + " is NOT CacheObject instance");
				((CacheableObject) ret).smart___initOnLoad();

				return ret;

			}
		} catch (Exception e) {
			except().rt(
					e,
					new CtxException.Context("HBaseConnection.find",
							"Exception"));
		}
		return null;
	}

	public List<Object> search(String group, String query) throws CtxException {
		// TODO: can be done with filters and scans
		return null;
	}

	public HBaseCRUD getCRUD() {
		return _crud;
	}

	@Override
	public void remove(String group, Object key) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> search(String group, Object query) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public boolean exists(String group, Object key) 
        throws CtxException
    {
        String tablename = getTableName(group);
        return _crud.exists(tablename, key.toString());
    }

    @Override
    public Iterator<Object> getListings(String group, String sortBy,
            int listingsPerPage, int pageNum)
        throws CtxException
    {
        try {
            String tablename = getTableName(group);
            Iterator<Object>  keyIter = _crud.slidingWindowForKeys(tablename, group, sortBy, listingsPerPage, pageNum);
            return keyIter;
            

        } catch (Exception e) {
            except().rt(
                    e,
                    new CtxException.Context("HBaseConnection.find",
                            "Exception"));
        }
        return null;

    }

    @Override
    public Iterator<Object> list(ListParams parms)
        throws CtxException
    {
        List<Object> resultSet = new ArrayList<Object>();
        try
        {
	        String tablename = getTableName(parms.getGroup());
            System.out.println("Reading from : " + tablename);
	        Scan s = new Scan();
            KeyColumn key = KeyFactory.getKeyColumn(parms);
            key.addToScan(s);
            if ((parms.getStartTime() > 0) && (parms.getEndTime() > 0))
                s.setTimeRange(parms.getStartTime(), parms.getEndTime());
            else if (parms.getStartTime() > 0)
                s.setTimeStamp(parms.getStartTime());

            if (parms.getStartKey() != null)
                s.setStartRow(BytesConverter.convertBytes(parms.getStartKey()));

            if (parms.getEndKey() != null)
                s.setStopRow(BytesConverter.convertBytes(parms.getEndKey()));

	        ResultScanner rs = _crud.listAll(tablename, parms.getSize(), s);
	        if(rs == null)
	            return resultSet.iterator();
	        for (Result result = rs.next(); result != null; result = rs.next())
	        {
                Object keyObj = key.read(result);
	            resultSet.add(keyObj);
	        }
	        return resultSet.iterator();
        }
        catch (Exception e)
        {
			except().rt( e, new CtxException.Context("HBaseConnection.list", "Exception"));
        }
        return null;
    }

}
