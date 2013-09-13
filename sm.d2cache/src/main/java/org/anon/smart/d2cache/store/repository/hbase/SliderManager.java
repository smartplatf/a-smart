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
 * File:                org.anon.smart.d2cache.store.repository.hbase.SlidingManager
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

import java.util.Map;

import org.anon.utilities.exception.CtxException;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Scan;
import org.hsqldb.lib.HashMap;

public class SliderManager
{
    private static SliderManager _instance;
    
    private Map<String, Slider> _cachedSliders;
    
    private SliderManager()
    {
        _cachedSliders = new java.util.HashMap<String, Slider>();
    }
    
    public Slider getSlider(HTable table, String group, String sortBy)
        throws CtxException
    {
        Slider s = null;
        String tableName = new String(table.getTableName());
        System.out.println("Getting slider for:"+tableName+sortBy);
        if(_cachedSliders.get(tableName+sortBy) != null)
        {
            s = _cachedSliders.get(tableName+sortBy);
        }
        else
        {
            //if(sortBy == null)
            {
                s = new KeySlider(table, group);
                //s = new RandomPageSlider(table, group, sortBy);
                _cachedSliders.put(tableName+sortBy, s);
                System.out.println("Created Slider for:"+tableName+sortBy);
            }
        }
        return s;
        
    }
    
    public static SliderManager instance()
        throws CtxException
    {
        if(_instance == null)
            _instance = new SliderManager();
        
        return _instance;
    }
}
