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
 * File:                org.anon.smart.d2cache.segment.LayeredReader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A reader that reads in sequence
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.segment;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.d2cache.QueryObject;
import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.store.MemoryStore;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.IndexedStore;
import org.anon.smart.d2cache.store.StoreItem;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;
import org.anon.utilities.logger.Logger;

public class LayeredReader implements Reader
{
	private Store[] _stores;
	private SegmentWriter _currWriter;
	private transient Logger _logger;
    
	public void setCurrentWriter(SegmentWriter writer) {
		this._currWriter = writer;
	}

	public LayeredReader(Store[] stores)
    {
    	_stores = stores;
    	_logger = logger().rlog(this);
    }



	@Override
	public Object lookup(String group, Object key) throws CtxException {
		//assumption is that the stores are passed in the order 
        //they have to be searched?
        Object ret = null;
        int foundat = -1;
        for (int i = 0; (ret == null) && (i < _stores.length); i++)
        {
        	if((_stores[i] == null) || (_stores[i] instanceof IndexedStore))
        		continue;
        		
                ret = _stores[i].getConnection().find(group, key);
                foundat = i;
        }

        if (ret != null)
        {
            List<StoreItem> item = new ArrayList<StoreItem>();
            item.add(new StoreItem(new Object[] { key }, ret, group));
            if(_currWriter != null)
            	_currWriter.handleReadAt(item, _stores, foundat);
            else
            	_logger.debug("Writer is not set in Layered Reader");
        }

        return ret;
	}

	@Override
	public List<Object> search(String group, Object query) throws CtxException {
		List<Object> ret = new ArrayList<Object>();
		
		assertion().assertTrue((query instanceof QueryObject), "query is NOT an instance of QueryObject");
		List<Object> resultKeys = new ArrayList<Object>();
		
		for (int i = 0; (i < _stores.length); i++)
        {
			if(_stores[i] instanceof IndexedStore)
			{
				resultKeys.addAll(((IndexedStore)_stores[i]).search(group, query));
			}
        }
		for(Object key : resultKeys)
		{
			Object obj = this.lookup(group, key);
			if(obj != null)
				ret.add(obj);
		}
		return ret;
	}

	@Override
	public List<Object> listAll(String group, int size) throws CtxException {
		
		List<Object> resultSet = new ArrayList<Object>();
		Iterator<Object> keyIter = null;
		for(int i = 0; (i < _stores.length); i++)
		{
			if(!(_stores[i] instanceof IndexedStore))
			{
				keyIter = (_stores[i].getConnection().listAll(group, size));
				if(keyIter != null)
				{
					while(keyIter.hasNext())
					{
						resultSet.add(keyIter.next());
					}
				}
			}
		}
		return resultSet;
	}

}

