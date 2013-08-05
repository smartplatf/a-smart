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
 * File:                org.anon.smart.d2cache.store.repository.hbase.HBaseTransaction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A transaction for hbase
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.hadoop.hbase.client.Put;

import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.AbstractStoreTransaction;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public class HBaseTransaction extends AbstractStoreTransaction
{
    public HBaseTransaction(UUID txn, StoreConnection conn)
    {
        super(txn, conn);
    }

    protected StoreRecord createNewRecord(String group, Object primarykey, Object curr, Object orig)
        throws CtxException
    {
    	return new HBaseRecord(group, primarykey, curr, orig, (HBaseConnection)_connection);
    }

    public void commit()
        throws CtxException
    {
        try
        {
            Map<String, List<Put>> docs = new HashMap<String, List<Put>>();
            for (String group : _recordsInTxn.keySet())
            {
                Map<Object, StoreRecord> records = _recordsInTxn.get(group);
                for (StoreRecord rec : records.values())
                {
                    HBaseRecord hbase = (HBaseRecord)rec;
                    List<Put> recs = docs.get(hbase.getTable());
                    if (recs == null)
                        recs = new ArrayList<Put>();
                    recs.add(hbase.putRecord());
                    docs.put(hbase.getTable(), recs);
                }
            }

            if (docs.size() > 0)
            {
                HBaseConnection conn = (HBaseConnection)_connection;
                for (String table : docs.keySet())
                {
                	if(!conn.getCRUD().tableExists(table))
                		conn.createMetadata(table, null);
                	
                    conn.getCRUD().putRecords(conn.getTableName(table), docs.get(table));
                }
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("HBaseTransaction.commit", "Exception"));
        }
    }



	public void rollback()
        throws CtxException
    {
        //nothing to do, it is not yet committed.
    }

	@Override
	public StoreRecord addRecord(String group, Object primarykey, Object curr,
			Object orig, Object relatedKey) throws CtxException {
		RelatedObject obj = new RelatedObject(relatedKey);
		return addRecord(group, primarykey, obj, null);
	}
}

