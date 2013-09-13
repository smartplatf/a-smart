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
 * File:                org.anon.smart.d2cache.store.repository.hbase.KeySlider
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Aug 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import static org.anon.utilities.services.ServiceLocator.type;
import static org.anon.utilities.services.ServiceLocator.except;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.anon.utilities.exception.CtxException;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class KeySlider implements Slider, Constants
{
    private List<Object> _keyList;
    private int _curPos;
    private HTable _table;
    private String _keyTypeCol;
    
    public KeySlider()
        throws CtxException
    {
        
    }
    
    public KeySlider(HTable table, String group)
        throws CtxException
    {
        _table = table;
        
        _keyTypeCol = group+PART_SEPARATOR+SMART_KEY_NAME+FIELDTYPE;
        
        constructKeyList(_keyTypeCol);
        //System.out.println("Total Keys:"+_keyList.size());
    }
    
    private void constructKeyList(String keyTypeCol) 
        throws CtxException
    {
        _keyList = new ArrayList<Object>();
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes(keyTypeCol));
        
        byte[] keyType = null;

        try
        {
            ResultScanner rs = _table.getScanner(scan);
            for (Result result = rs.next(); result != null; result = rs.next())
            {
                Class keyFieldType = null;
                keyType = result.getValue(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes(keyTypeCol));
                if(keyType != null)
                {
                    keyFieldType =  Class.forName(new String(keyType));
                }
                else
                {
                    keyFieldType = String.class;
                }
                Object keyObj = type().convertToPrimitive(keyFieldType, result.getRow());
                _keyList.add(keyObj);
            }
            
            System.out.println("--------->TOTAL KEYS:"+_keyList.size());
            rs.close();
        
        } catch (ClassNotFoundException e)
        {
            except().te(this, "Class for Key type not found:"+new String(keyType));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().te(this, "IOException in KeySlider");
        }

    }

    @Override
    public Iterator getKeys(int pageSize, int pageNum, byte[] sr) throws CtxException
    {
        if(needRefresh())
            constructKeyList(_keyTypeCol);
        if(_keyList == null)
            return null;
        
        int startInd = pageSize*(pageNum-1);
        if(startInd > (_keyList.size() -1))
            return null;
        //endInd is exclusive
        int endInd = (startInd+pageSize > _keyList.size()) ? _keyList.size() : startInd+pageSize;
        System.out.println("GEtting window:"+startInd+":"+endInd);
        List subList = _keyList.subList(startInd, endInd);
        return subList.iterator();
    }

    private boolean needRefresh()
    {
       return true; //TODO calculate based on time?
    }

}
