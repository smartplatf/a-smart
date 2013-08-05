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
 * File:                org.anon.smart.d2cache.store.memory.jcs.JCSTransaction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A transaction that collects and stores into a jcs cache
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.memory.jcs;

import java.util.UUID;
import java.util.List;
import java.util.Map;

import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.AbstractStoreTransaction;
import org.anon.smart.d2cache.store.index.solr.Constants;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;
import org.anon.utilities.reflect.ObjectTraversal;
import org.apache.jcs.access.exception.CacheException;

public class JCSTransaction extends AbstractStoreTransaction
{
    public JCSTransaction(UUID txnid, StoreConnection conn)
    {
        super(txnid, conn);
    }

    protected StoreRecord createNewRecord(String group, Object primarykey, Object curr, Object orig)
        throws CtxException
    {
        JCSConnection conn = (JCSConnection)_connection;
        Object obj = conn.find(group, primarykey);
        
        JCSRecord rec = new JCSRecord(group, primarykey, curr, orig);
        
        //Do We need this for JCS?
        /*
        JCSObjectTraversal jot = new JCSObjectTraversal(rec);
        ObjectTraversal ot = new ObjectTraversal(jot, curr, false, null);
        ot.traverse();
        */
        return rec;
    }

    private void putOneRecord(StoreRecord record)
        throws Exception
    {
    	JCSRecord rec = (JCSRecord)record;
        JCSConnection conn = (JCSConnection)_connection;
        String group = rec.getGroup();
        Object obj = null;
        if(rec.getOriginal() != null)
        	obj = rec.getOriginal();
        else
        	obj = rec.getModified();
	    //Object mod = rec.getModified();
	    List<Object> keys = rec.getKeys();
	    for (Object k : keys) {
			try {
				if (group != null){
					conn.cache().putInGroup(k, group, obj);
				}
				else
					conn.cache().put(k, obj);
			} catch (CacheException ex) {
				except().rt(
						ex,
						new CtxException.Context("JCSStore.writeRecord",
								"Exception"));
			}
		}
    }

    public void commit()
        throws CtxException
    {
    	try
        {
            for (String group : _recordsInTxn.keySet())
            {
                Map<Object, StoreRecord> records = _recordsInTxn.get(group);
                for (StoreRecord record : records.values())
                    putOneRecord(record);
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("JCSTransaction.commit", "Exception"));
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
		return addRecord(group, primarykey, curr, orig);
	}
}

