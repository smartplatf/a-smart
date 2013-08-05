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
 * File:                org.anon.smart.d2cache.FileStoreTransactionImpl.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 2, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.d2cache.store.fileStore;

import java.util.ArrayList;
import java.util.UUID;

import org.anon.smart.d2cache.D2CacheTransactionImpl;
import org.anon.smart.d2cache.segment.SegmentWriter;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.utilities.exception.CtxException;
import static org.anon.utilities.services.ServiceLocator.*;

/**
 * @author raooll
 *
 */
public class FileStoreTransactionImpl extends D2CacheTransactionImpl {

	
	public FileStoreTransactionImpl(UUID id,
			StoreConnection[] storeConnections, SegmentWriter writer) {
		super(id, storeConnections, writer);
		
	}
	

	@Override
	public void add(StoreItem item) throws CtxException {

		Object obj = item.getTruth();

		for (StoreTransaction txn : _storeTransactions) {

			((FileStoreTransaction) txn).add(item.getTruth(), item.group());
		}
	}
	
}
