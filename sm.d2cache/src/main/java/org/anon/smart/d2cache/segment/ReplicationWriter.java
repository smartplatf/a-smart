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
 * File:                org.anon.smart.d2cache.segment.ReplicationWriter
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A writer that replicates the data across all the stores
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.segment;

import static org.anon.utilities.objservices.ObjectServiceLocator.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.store.IndexedStore;
import org.anon.smart.d2cache.store.MemoryStore;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.utilities.concurrency.ExecutionUnit;
import org.anon.utilities.exception.CtxException;

public class ReplicationWriter implements SegmentWriter {
	private boolean _async;

	public ReplicationWriter() {
		// TODO: need to make this configurable
		_async = true;
	}

	public void write(StoreTransaction[] transactions) throws CtxException {
		List<ExecutionUnit> tasks = new ArrayList<ExecutionUnit>();
		for (StoreTransaction txn : transactions) {
			StoreWriterTask t = new StoreWriterTask(txn);
			tasks.add(t);
		}
		if (_async)
			execute().asynchWait(tasks);
		else
			execute().synch(tasks);
	}

	public void handleReadAt(List<StoreItem> items, Store[] stores, int foundat)
			throws CtxException {
		// means we have picked it up from a memory store
		if (stores[foundat] instanceof MemoryStore)
			return;

		// from the persistent store, write it back into the
		// memory stores
		List<StoreTransaction> txnList = new ArrayList<StoreTransaction>();
		for (int i = 0; i < foundat; i++) {
			if ((stores[i] instanceof MemoryStore) && (!(stores[i] instanceof IndexedStore))) {
				//System.out.println("Writing here into JCS on CacheMiss...........");
				StoreTransaction txn = stores[i].getConnection()
						.startTransaction(UUID.randomUUID());
				for (StoreItem item : items) {
					for (Object key : item.keys()) {
						txn.addRecord(item.group(), key, item.getModified(), item.getOriginal());
					}
				}

				txnList.add(txn);
			}
		}
		this.write(txnList.toArray(new StoreTransaction[0]));
	}
}
