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
 * File:                org.anon.smart.d2cache.store.index.solr.SolrTransaction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A transaction that stores records and commits it
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.SolrCore;

import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.smart.d2cache.store.AbstractStoreTransaction;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public class SolrTransaction extends AbstractStoreTransaction
{
    public SolrTransaction(UUID txnid, StoreConnection conn)
    {
        super(txnid, conn);
    }

    protected StoreRecord createNewRecord(String group, Object primarykey, Object curr, Object orig)
        throws CtxException
    {
        return new SolrRecord(group, primarykey, curr, orig);
    }

    public void commit()
        throws CtxException
    {
        try
        {
            List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
            for (String group : _recordsInTxn.keySet())
            {
                Map<Object, StoreRecord> records = _recordsInTxn.get(group);
                for (StoreRecord rec : records.values())
                {
                    SolrRecord solr = (SolrRecord)rec;
                    docs.add(solr.document());
                }
            }

            if (docs.size() > 0)
            {
               SolrConnection conn = (SolrConnection)_connection;
               conn.server().add(docs);
               conn.server().commit();
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("SolrTransaction.commit", "Exception"));
        }
    }

    public void rollback()
        throws CtxException
    {
        //nothing to do, it is not yet committed.
    }

    @Override
    public boolean waitToComplete()
    {
        return false;
    }

	@Override
	public StoreRecord addRecord(String group, Object primarykey, Object curr,
			Object orig, Object relatedKey) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}
}

