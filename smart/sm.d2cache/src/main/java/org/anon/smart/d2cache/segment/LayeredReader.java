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

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.IndexedStore;
import org.anon.smart.d2cache.store.StoreItem;

import org.anon.utilities.exception.CtxException;

public class LayeredReader implements SegmentReader
{
    public LayeredReader()
    {
    }

    public Object read(String group, Object key, Store[] stores, SegmentWriter currWriter)
        throws CtxException
    {
        //assumption is that the stores are passed in the order 
        //they have to be searched?
        Object ret = null;
        int foundat = -1;
        for (int i = 0; (ret == null) && (i < stores.length); i++)
        {
            //read only in non-indexed reader, since this is a 
            //keyed read
            if (!(stores[i] instanceof IndexedStore))
            {
                ret = stores[i].read(group, key);
                foundat = i;
            }
        }

        if (ret != null)
        {
            List<StoreItem> item = new ArrayList<StoreItem>();
            item.add(new StoreItem(new Object[] { key }, ret, group));
            currWriter.handleReadAt(item, stores, foundat);
        }

        return ret;
    }

    public List<Object> search(String group, Object query, Store[] stores, SegmentWriter writer)
        throws CtxException
    {
        List<Object> ret = null;
        //assumption here is that the indexed readers return keys,
        //this is read from the non-indexed readers?
        //TODO
        return ret;
    }

}

