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
import org.apache.hadoop.hbase.TableNotFoundException;
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
            //createRowIDTableFor(tableName);
            System.out.println("Table Created:"+tableName);
        }
        
    }
    private void createRowIDTableFor(String tableName) 
        throws IOException
    {
        HTableDescriptor  desc = new HTableDescriptor(tableName+ROWID_SUFFIX);
        HColumnDescriptor hc = new HColumnDescriptor(SYNTHETIC_COL_FAMILY);
        desc.addFamily(hc);
        _admin.createTable(desc);
        
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

    private void closeTable(HTable table)
    {
        try
        {
            if (table != null)
                table.close();
        }
        catch (Exception e)
        {
            //ignore for now.
            System.out.println(e.getMessage());
        }
    }

	Map<String, byte[]> oneRecord(String tableName, Object rowKey) 
        throws CtxException
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
            closeTable(table);
            return ret;
        }

        try
        {
            Get get = new Get(BytesConverter.convertBytes(rowKey));
            Result rs = table.get(get);
            
            for(KeyValue kv : rs.raw())
            {
                //System.out.println(new String(kv.getQualifier())+"--->"+new String(kv.getValue()));
                ret.put(new String(kv.getQualifier()), kv.getValue());
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Exception", e.getMessage()));
        }
        finally
        {
            closeTable(table);
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
        finally
        {
            closeTable(table);
        }
        
	        
	}

	ResultScanner listAll(String tableName, long size, Scan s)
		throws CtxException 
	{
        HTable table = null;
	    try
	    {
            System.out.println("Searching in table: " + tableName);
	        table = new HTable(_config, tableName);
            /*if (size > 0)
                s.setMaxResultSize((long)size);*/
        
	        ResultScanner rs = table.getScanner(s);
	        return rs;
	    }
	    catch(TableNotFoundException ex)
	    {
	        //except().te(this, "Exception in Listing for group: Table Does NOT Exist:"+tableName);
	        return null;
	    }
	    catch(Exception ex)
	    {
	        except().te(this, "Exception in Listing from table:"+tableName);
	    }
        finally
        {
            closeTable(table);
        }
	    
	    return null;
	}
	Put newRecord(Object rowKey, String family, String qualifier, Object value)
        throws Exception
    {
        //mostly for lists we want to add .1, .2 etc in the qualifier
        //and for maps add .key and .value at the end
        //Maybe we can have same put object with multiple adds. Check. TODO
	    Put put = new Put(BytesConverter.convertBytes(rowKey));
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
        HTable table = null;
        try
        {
        table = new HTable(_config, tableName);
        table.put(puts);
        
        /* adding ROWIDs */
        //insertRowIDs(tableName, puts);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeTable(table);
        }
    }

    /**
     * insert rowIDs in a separate table for all those records created in data table
     * and update the rowCount at the end
     * @param tableName
     * @param puts
     * @throws Exception
     */
    private void insertRowIDs(String tableName, List<Put> puts)
        throws Exception
    {
        String rowIDTable = tableName+ROWID_SUFFIX;
        //TODO Delet this if ... rowID table will not be present for those tables created before this patch  
        if(!tableExists(rowIDTable))
            return;
        HTable table = null;
        try
        {
            table = new HTable(_config, rowIDTable);
            /* get rowCount */
            Get g = new Get(Bytes.toBytes("Count"));
            long val = 0;
            Result r = table.get(g);
            
            if(r != null)
            {
                KeyValue kv  = r.getColumnLatest(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes("value"));
                if(kv != null)
                    val = Bytes.toLong(kv.getValue());
            }
            List<Put> rowIDPuts = new ArrayList<Put>(puts.size());
            for(Put put : puts)
            {
                //If this record is for related object(for user keys) skip the rowID
                if(isRelatedPut(put))
                    continue;
                val++;
                Put p = new Put(Bytes.toBytes(val));
                p.add(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes(ACTUAL_KEY),put.getRow());
                rowIDPuts.add(p);
                
            }
            table.put(rowIDPuts);
            
            /* now update rowCount */
            table.incrementColumnValue(Bytes.toBytes("Count"), Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes("value"), rowIDPuts.size());
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeTable(table);
        }
        
    }
    private boolean isRelatedPut(Put put)
    {
        List<KeyValue> kvList = put.get(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes(SMART_CLASS_NAME));
        if((kvList != null) && (kvList.size() > 0))
        {
            KeyValue kv = kvList.get(0);
            String clsName = new String(kv.getValue());
            return clsName.equals(RelatedObject.class.getName());
        }
        
        return false;
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

    public Iterator slidingWindowForKeys(String tableName, String group, String sortBy,
        int listingsPerPage, int pageNum)
        throws CtxException
    {
        HTable table = null;
        try
        {
            table = new HTable(_config, tableName);
            Slider slider = SliderManager.instance().getSlider(table, group, sortBy);
            byte[] sr = getStartRow(tableName, listingsPerPage, pageNum);
            Iterator keyIter = slider.getKeys(listingsPerPage, pageNum, sr);
            return keyIter;
        }
        catch(TableNotFoundException ex)
        {
            //except().te(this, "Exception in Listing for group: Table Does NOT Exist:"+tableName);
            return null;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            except().te(this, "Exception in Listing from table:"+tableName);
        }
        finally
        {
            closeTable(table);
        }
        
        return null;
    }

    private byte[] getStartRow(String tableName, int listingsPerPage,
            int pageNum)
        throws Exception
    {
        /*try
        {
        long index = (listingsPerPage * (pageNum-1))  + 1;
        Get g = new Get(Bytes.toBytes(index));
        HTable table = new HTable(_config, tableName+ROWID_SUFFIX);
        Result r = table.get(g);
        if(r != null)
        {
            KeyValue kv = r.getColumnLatest(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes("actualKey"));
            if(kv != null)
            {
                return kv.getValue();
            }
        }
        }
        catch(Exception ex)
        {
            return null;
        }*/
        return null;
    }

   

}

