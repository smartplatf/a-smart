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
 * File:                org.anon.smart.d2cache.TestCacheUtil
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 14, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.test;

import java.util.Map;
import java.util.UUID;

import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.utilities.exception.CtxException;

public class TestCacheUtil {

	public static void setTestData(D2Cache cache, Object obj, Object[] keys, String group)
	throws CtxException {
		D2CacheTransaction txn =  cache.startTransaction(UUID.randomUUID());
		StoreItem item = new StoreItem(keys, null, group);
		item.setModified(obj);
		txn.add(item);
		txn.commit();
		
	}
	
	public static void setTestData(D2Cache cache, Map<Object, Object[]> data, String group)
		throws CtxException {
		
		D2CacheTransaction txn =  cache.startTransaction(UUID.randomUUID());
		for(Map.Entry<Object, Object[]> e : data.entrySet()) {
			StoreItem item = new StoreItem(e.getValue(), e.getKey(), group);
			txn.add(item);
		}
		
		txn.commit();
	}
}
