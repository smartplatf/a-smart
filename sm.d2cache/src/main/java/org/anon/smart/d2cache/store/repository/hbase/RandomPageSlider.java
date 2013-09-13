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
 * File:                org.anon.smart.d2cache.store.repository.hbase.SequentialPageSlider
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

import static org.anon.utilities.services.ServiceLocator.except;
import static org.anon.utilities.services.ServiceLocator.type;

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


public class RandomPageSlider implements Slider, Constants
{
    private HTable _table;
    private byte[] _lastRow = null;
    private String _group;
    
    public static final byte[] POSTFIX = new byte[]{0x00};
    
    public RandomPageSlider()
    {
        
    }
    public RandomPageSlider(HTable table, String group, String sortBy)
    {
        _table = table;
        _group = group;
    }
    @Override
    public Iterator getKeys(int pageSize, int pageNum, byte[] sr) throws CtxException
    {
        List<Object> keyList = new ArrayList<Object>();
        Scan s = new Scan();
        PageFilter filter = new PageFilter(pageSize);
        s.setFilter(filter);
        
        //if(_lastRow != null)
        if(sr != null)
        {
           //byte[] startRow = Bytes.add(_lastRow, POSTFIX);
           
           //s.setStartRow(startRow);
            s.setStartRow(sr);
        }
        byte[] keyType = null;
        String keyTypeCol = _group+PART_SEPARATOR+SMART_KEY_NAME+FIELDTYPE;
        //Fetch all key name fieldtype along with rowKeys
        //TODO will not return if columnVal is null(for relatedObjects)
        s.addColumn(Bytes.toBytes(SYNTHETIC_COL_FAMILY), Bytes.toBytes(keyTypeCol));
        
        try
        {
            ResultScanner rs = _table.getScanner(s);
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
                keyList.add(keyObj);
                _lastRow = result.getRow();
            }
            rs.close();
            
        
        }catch (ClassNotFoundException e)
        {
            except().te(this, "Class for Key type not found:"+new String(keyType));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().te(this, "IOException in KEySlider");
        } 
        return keyList.iterator();
    }

}
