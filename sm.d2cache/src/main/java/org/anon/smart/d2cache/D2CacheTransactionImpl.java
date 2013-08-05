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
 * File:                org.anon.smart.d2cache.D2CacheTransactionImpl
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.segment.CSegment;
import org.anon.smart.d2cache.segment.CacheObjectTraversal;
import org.anon.smart.d2cache.segment.SegmentWriter;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.smart.d2cache.store.repository.hbase.RelatedObject;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.reflect.DirtyFieldTraversal;
import org.anon.utilities.reflect.ObjectTraversal;

import com.google.common.collect.Lists;


public class D2CacheTransactionImpl implements D2CacheTransaction {

	protected UUID _txnID;
	protected StoreTransaction[] _storeTransactions;
	protected SegmentWriter _writer;
	protected DataFilter[] _filters;
	
	
	public D2CacheTransactionImpl(UUID id, StoreConnection[] storeConnections, SegmentWriter writer)
	{
		_txnID = id;
		_storeTransactions = new StoreTransaction[storeConnections.length];
		_writer = writer;
		int i = 0;
		for(StoreConnection conn : storeConnections) {
			try {
				_storeTransactions[i++] = conn.startTransaction(id);
				
			} catch (CtxException e) {
				e.printStackTrace();
			}
		}
	}

    public void setupFilters(DataFilter[] filters)
    {
        _filters = filters;
    }

    private boolean isFilter(Object obj)
        throws CtxException
    {
        boolean ret = true;
        for (int i = 0; (ret) && (_filters != null) && (i < _filters.length); i++)
            ret = _filters[i].filterObject(obj, DataFilter.dataaction.write, true);

        return ret;
    }

	@Override
	public void add(StoreItem item) throws CtxException {
		List<StoreRecord> recList = new ArrayList<StoreRecord>();

        //if the filter says I cannot do anything with this data, then return.
        if ((item.getTruth() != null) && !isFilter(item.getTruth()))
            return;
		
		/*
		 * Now do the DirtyFieldTraversal here and store the modified truth object against set of keys
		 */
		item.mergeChanges();
		Object[] keyList = item.keys();
		for (StoreTransaction txn : _storeTransactions) {
			recList.add(txn.addRecord(item.group(), keyList[0], item.getModified(), item.getTruth()));
		}
		
		for (int i =1 ; i < keyList.length; i++) {
			for (StoreTransaction txn : _storeTransactions) {
				StoreRecord rec = txn.addRecord(item.group(), keyList[i], item.getModified(), item.getTruth(), keyList[0]);
				if((rec != null) && (rec.getCurrent() instanceof RelatedObject))
				{
					CacheObjectTraversal trav = new CacheObjectTraversal(rec);
					ObjectTraversal ot = new ObjectTraversal(trav, rec.getCurrent(), false, null);
					ot.traverse();
					
				}
				else if(rec != null)
				{
					recList.add(rec);
				}
			}
		}
		
		System.out.println("--->D2CacheTransactionImpl:"+item.getTruth()+"::"+item.getModified()+"::"+item.getOriginal());
		
		CacheObjectTraversal cot = new CacheObjectTraversal(recList);
		ObjectTraversal ot = null;
		if((item.getTruth() == null) || item.getTruth().equals(item.getModified()))
		{
			//Truth is null..new Object
			ot =  new ObjectTraversal(cot, item.getModified(), false, null);
		}
		else
		{
			//ot  = new DirtyFieldTraversal(cot, item.getModified(), item.getOriginal(), false);
			ot  = new DirtyFieldTraversal(cot, item.getModified(), item.getTruth(), item.getOriginal(), false);
		}
		ot.traverse();
	}

	@Override
	public void commit() throws CtxException {
		_writer.write(_storeTransactions);
	}

	@Override
	public void rollback() throws CtxException {
		for(StoreTransaction txn : _storeTransactions){
			txn.rollback();
		}
	}

}
