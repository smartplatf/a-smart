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
import org.anon.smart.d2cache.DataFilter;
import org.anon.smart.d2cache.store.MemoryStore;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.IndexedStore;
import org.anon.smart.d2cache.store.StoreItem;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;
import org.anon.utilities.logger.Logger;

public class LayeredReader implements Reader
{
    private DataFilter[] _filters;
	private Store[] _stores;
	private SegmentWriter _currWriter;
	private transient Logger _logger;
    
	public LayeredReader(Store[] stores, SegmentWriter writer)
	{
		_stores = stores;
		_currWriter = writer;
    	_logger = logger().rlog(this);
    	
	}

    public void userFilters(DataFilter[] filters)
    {
        _filters = filters;
    }

	public void setCurrentWriter(SegmentWriter writer) 
    {
		this._currWriter = writer;
	}

	public LayeredReader(Store[] stores)
    {
    	_stores = stores;
    	_logger = logger().rlog(this);
    }

    private boolean isFilter(Object obj, boolean except)
        throws CtxException
    {
        boolean ret = true;
        for (int i = 0; (ret) && (_filters != null) && (i < _filters.length); i++)
            ret = _filters[i].filterObject(obj, DataFilter.dataaction.read, except);

        return ret;
    }

	private Object lookup(String group, Object key, boolean except) 
        throws CtxException 
    {
		//assumption is that the stores are passed in the order 
        //they have to be searched?
        Object ret = null;
        int foundat = -1;
        for (int i = 0; (ret == null) && (i < _stores.length); i++)
        {
        	if((_stores[i] == null) || (_stores[i] instanceof IndexedStore))
        		continue;
        		
            ret = _stores[i].getConnection().find(group, key);
            if ((ret != null) && (!isFilter(ret, except)))
                ret = null;

            foundat = i;
        }

        if (ret != null)
        {
            List<StoreItem> items = new ArrayList<StoreItem>();
            StoreItem item = new StoreItem(new Object[] { key }, null, group);
            item.setModified(ret);
            items.add(item);
            if(_currWriter != null)
            {
            	_currWriter.handleReadAt(items, _stores, foundat);
            }
            else
            {
            	System.out.println("!!!!!!!!!!!!!!! Writer is not set in Layered Reader !!!!!!!!!!!!!!!");
            	_logger.debug("Writer is not set in Layered Reader");
            }
        }

        return ret;
	}

	@Override
	public Object lookup(String group, Object key) 
        throws CtxException 
    {
        return lookup(group, key, true);
    }

	@Override
	public List<Object> search(String group, Object query) 
        throws CtxException 
    {
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
            //Lookup takes care of the filter
			Object obj = this.lookup(group, key, false);
			if(obj != null)
				ret.add(obj);
		}
		return ret;
	}

	@Override
	public List<Object> listAll(String group, int size) 
        throws CtxException 
    {
		
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
                        Object obj = keyIter.next();
                        if ((obj != null) && (isFilter(obj, false)))
                            resultSet.add(obj);
					}
				}
			}
		}
		return resultSet;
	}

    @Override
    public boolean exists(String group, Object key)
        throws CtxException
    {
        boolean ret = false;
      //assumption is that the stores are passed in the order 
        //they have to be searched?
        for (int i = 0; (!ret) && (i < _stores.length); i++)
        {
            if((_stores[i] == null) || (_stores[i] instanceof IndexedStore))
                continue;
                
            ret = _stores[i].getConnection().exists(group, key);
        }
        
        return ret;
    }

}

