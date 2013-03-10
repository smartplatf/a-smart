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
 * File:                org.anon.smart.d2cache.store.memory.jcs.JCSSegment
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 *  JCS Implementation for CSegment
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.memory.jcs;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.segment.CSegment;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.utilities.exception.CtxException;


public class JCSSegment implements CSegment {

	//JCS Store instance
	private Store _store; // TODO Can have array of Stores in a single segment???
	private StoreConnection _connection;
	
	@Override
	public Store getStore() {
		return _store;
	}

	@Override
	public void setupSegment(String name, String related, StoreConfig cfg)
			throws CtxException {
		_store = new JCSStore();
		_store.setup(name, related, cfg);
		
		_connection = new JCSConnection((JCSStore)_store);
	}

	@Override
	public void storeItem(StoreItem item) throws CtxException {
		storeItem(Arrays.asList(item));
	}

	@Override
	public void storeItem(List<StoreItem> items) throws CtxException {
		UUID txnID = UUID.randomUUID();
		StoreTransaction txn = _connection.startTransaction(txnID);
		for(StoreItem item : items)
		{
			for(Object key : item.keys()) {
				txn.addRecord(item.group(), key, item.item());
			}
			
		}
		txn.commit();
	}
	
}
