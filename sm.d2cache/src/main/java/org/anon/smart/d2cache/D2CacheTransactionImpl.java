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
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.reflect.ObjectTraversal;

import com.google.common.collect.Lists;


public class D2CacheTransactionImpl implements D2CacheTransaction {

	private UUID _txnID;
	private StoreTransaction[] _storeTransactions;
	private SegmentWriter _writer;
	
	
	
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
	@Override
	public void add(StoreItem item) throws CtxException {
		List<StoreRecord> recList = new ArrayList<StoreRecord>();
		for (Object key : item.keys()) {
			for (StoreTransaction txn : _storeTransactions) {
				System.out.println(txn);
				recList.add(txn.addRecord(item.group(), key, item.item()));
			}
		}
		
		CacheObjectTraversal cot = new CacheObjectTraversal(recList);
		ObjectTraversal ot = new ObjectTraversal(cot, item.item(), false, null);
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
