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
 * File:                org.anon.smart.d2cache.store.AbstractStoreTransaction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An abstract implementation of store transaction
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.utilities.exception.CtxException;

public abstract class AbstractStoreTransaction implements StoreTransaction
{
    private UUID _txnID;
    protected StoreConnection _connection;
    protected Map<String, Map<Object, StoreRecord>> _recordsInTxn;

    public AbstractStoreTransaction(UUID txnid, StoreConnection conn)
    {
        _txnID = txnid;
        _connection = conn;
        _recordsInTxn = new ConcurrentHashMap<String, Map<Object, StoreRecord>>();
    }

    public UUID getTransactionId()
    {
        return _txnID;
    }

    public Collection<StoreRecord> allRecords()
    {
        List<StoreRecord> ret = new ArrayList<StoreRecord>();
        for (String group : _recordsInTxn.keySet())
        {
            Map<Object, StoreRecord> records = _recordsInTxn.get(group);
            ret.addAll(records.values());
        }
        return ret;
    }

    public Collection<StoreRecord> getRecords(String group)
    {
        Map<Object, StoreRecord> records = _recordsInTxn.get(group);
        if (records != null)
            return records.values();

        return null;
    }

    public StoreRecord recordFor(Object primary)
    {
        //not required??
        return null;
    }

    public StoreRecord addRecord(String group, Object primarykey, Object curr, Object orig)
        throws CtxException
    {
        Map<Object, StoreRecord> grp = _recordsInTxn.get(group);
        if (grp == null)
            grp = new ConcurrentHashMap<Object, StoreRecord>();

        StoreRecord rec = grp.get(primarykey);
        if (rec == null)
            rec = createNewRecord(group, primarykey, curr, orig);
        grp.put(primarykey, rec);
        _recordsInTxn.put(group, grp);
        return rec;
    }
    
    public abstract StoreRecord addRecord(String group, Object primarykey, Object curr, Object orig, Object relatedKey)
            throws CtxException;

    //by default wait.
    public boolean waitToComplete()
    {
        return true;
    }

    protected abstract StoreRecord createNewRecord(String group, Object primarykey, Object curr, Object orig)
        throws CtxException;
}

