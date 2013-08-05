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
 * File:                org.anon.smart.d2cache.store.repository.hbase.HBaseCRUD
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of crud operations on hbase
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import static org.anon.utilities.objservices.ObjectServiceLocator.convert;
import static org.anon.utilities.services.ServiceLocator.except;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.anon.utilities.exception.CtxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseCRUD implements Constants
{
    private static Configuration _config;
    private static HBaseAdmin _admin;

    public HBaseCRUD(Configuration conf) throws Exception
    {
    	if(_config == null)
    		_config = conf;
    	if(_admin == null)
    		_admin = new HBaseAdmin(_config);
    }

    void newTable(String tableName, String[] cf)
        throws Exception 
    {
        //HBaseAdmin admin = new HBaseAdmin(_config);
        if (!_admin.tableExists(tableName)) 
        {
            HTableDescriptor desc = new HTableDescriptor(tableName);
            for (int i = 0; i < cf.length; i++) 
                desc.addFamily(new HColumnDescriptor(cf[i]));
            _admin.createTable(desc);
            System.out.println("Table Created:"+tableName);
        }
        
    }
    void deleteTable(String tableName)
    	throws Exception
    {
    	//HBaseAdmin admin = new HBaseAdmin(_config);
    	if(_admin.tableExists(tableName))
    	{
    		_admin.disableTable(tableName);
    		_admin.deleteTable(tableName);
    	}
    	
    }

	Map<String, byte[]> oneRecord(String tableName, String rowKey) 
        throws IOException
    {
        //possibly we can do this using filter also?? need to check out
        //TODO: can filter by any key value
		HTable table  = null;
		Map<String, byte[]> ret = new HashMap<String, byte[]>();
		try
		{
			if(tableExists(tableName))
				table = new HTable(_config, tableName);
			else
				return ret;
		}
		catch(Exception ex)
		{
			return ret;
		}
        Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        
        for(KeyValue kv : rs.raw())
        {
       	    //System.out.println(new String(kv.getQualifier())+"--->"+new String(kv.getValue()));
            ret.put(new String(kv.getQualifier()), kv.getValue());
        }

        return ret;
    }
	
	public boolean exists(String tableName, String rowKey)
	    throws CtxException
	{
	    HTable table  = null;
	    try
	    {
	        if(tableExists(tableName))
	        {
	            table = new HTable(_config, tableName);
	        }
	        else
	        {
	            return false;
	        }
	    
	        Get get = new Get(rowKey.getBytes());
	        return table.exists(get);
	    }
        catch(Exception ex)
        {
            return false;
        }
        
	        
	}

	ResultScanner listAll(String tableName, int size, Scan s)
		throws IOException 
	{
		HTable table = new HTable(_config, tableName);
        
        ResultScanner rs = table.getScanner(s);
        return rs;
	}
	Put newRecord(String rowKey, String family, String qualifier, Object value)
        throws Exception
    {
        //mostly for lists we want to add .1, .2 etc in the qualifier
        //and for maps add .key and .value at the end
        //Maybe we can have same put object with multiple adds. Check. TODO
	    Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(convert().objectToString(value)));
        
        return put;
	}

    Put addTo(Put p, String family, String qualifier, Object value)
        throws Exception
    {
    	//System.out.println("ADDING COLUMN:"+family+"::"+qualifier+"::"+convert().objectToString(value));
    	p.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(convert().objectToString(value)));
        return p;
    }

    void putRecords(String tableName, List<Put> puts)
        throws Exception
    {
    	HTable table = new HTable(_config, tableName);
        table.put(puts);
    }

    public boolean tableExists(String tableName) throws CtxException
    {
    	//HBaseAdmin admin = new HBaseAdmin(_config);
		try {
			if(_admin.tableExists(tableName))
					return true;
		} catch (IOException e) {
			except().rt(e, "IOException while checking existance of table:"+tableName, null);
		}
    	
		return false;
    	
    	
    	
    	
    }

}

