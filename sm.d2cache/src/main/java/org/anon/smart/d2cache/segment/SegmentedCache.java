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
 * File:                org.anon.smart.d2cache.segment.SegmentedCache
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a cache that has multiple segments and based on the reader or writer behaves differently
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.segment;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreItem;

import org.anon.utilities.exception.CtxException;

public abstract class SegmentedCache implements D2Cache
{
    private List<CSegment> _segments;
    private SegmentWriter _writer;
    private SegmentReader _reader;
    private Store[] _stores;

    private Map<String, List<StoreItem>> _queued;

    public SegmentedCache(List<CSegment> segments, SegmentWriter writer, SegmentReader reader)
    {
        _segments = segments;
        _writer = writer;
        _reader = reader;
        _queued = new ConcurrentHashMap<String, List<StoreItem>>();
    }

    public void queueToPut(String qname, StoreItem item)
        throws CtxException
    {
        List<StoreItem> items = null;
        if (_queued.containsKey(qname))
            items = _queued.get(qname);
        else
            items = new ArrayList<StoreItem>();

        items.add(item);
        _queued.put(qname, items);
    }

    private Store[] getStores()
    {
        if (_stores == null)
        {
            int len = _segments.size();
            _stores = new Store[len];
            for (int i = 0; i < len; i++)
                _stores[i] = _segments.get(i).getStore();
        }

        return _stores;
    }

    public void commitPut(String qname)
        throws CtxException
    {
        if (_queued.containsKey(qname))
        {
            List<StoreItem> items = _queued.get(qname);
            _queued.remove(qname);
            Store[] stores = getStores();
            //_writer.write(items, stores);
        }
    }

    public Object get(String group, Object key)
        throws CtxException
    {
        return _reader.read(group, key, getStores(), _writer);
    }

    public List<Object> search(String group, Object query)
        throws CtxException
    {
        return _reader.search(group, query, getStores(), _writer);
    }
}

