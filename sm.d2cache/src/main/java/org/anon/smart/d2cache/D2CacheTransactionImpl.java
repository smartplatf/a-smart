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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.segment.CSegment;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.utilities.exception.CtxException;

import com.google.common.collect.Lists;


public class D2CacheTransactionImpl implements D2CacheTransaction {

	private Map<String, StoreItem> _itemMap = new HashMap<String, StoreItem>();
	private UUID _txnID;
	private CSegment[] _segments;
	
	public D2CacheTransactionImpl(UUID id, CSegment[] segments)
	{
		_txnID = id;
		_segments = segments;
	}
	@Override
	public void add(String qname, StoreItem item) throws CtxException {
		_itemMap.put(qname,  item);
	}

	@Override
	public void commit() throws CtxException {
		for(CSegment segment : _segments){
			segment.storeItem(Lists.newArrayList(_itemMap.values()));
		}
	}

	@Override
	public void rollback() throws CtxException {
		_itemMap.clear();

	}

}
