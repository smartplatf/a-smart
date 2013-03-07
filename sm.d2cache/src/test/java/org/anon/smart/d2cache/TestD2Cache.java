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
 * File:                org.anon.smart.d2cache.TestD2Cache
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 7, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

import static org.junit.Assert.*;

import java.util.UUID;

import org.anon.smart.d2cache.D2CacheScheme.scheme;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.test.reflect.SimpleTestObject;
import org.junit.Test;

public class TestD2Cache {

	@Test
	public void testCreateCache() throws CtxException
	{
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		D2Cache cache = D2CacheScheme.getCache(scheme.mem, name, flags);
		UUID cacheTxn = UUID.randomUUID();
		D2CacheTransaction txn =  cache.startTransaction(cacheTxn);
		
		SimpleTestObject obj = new SimpleTestObject();
		Object[] keys = new String[]{"myObj"};
		StoreItem item = new StoreItem(keys, obj, "SimpleTestObject");
		txn.add(null, item);
		txn.commit();
		
		Reader reader = cache.myReader();
		Object fromCache = reader.lookup("SimpleTestObject", "myObj");
		
		assertTrue(fromCache != null);
		
		System.out.println("Read from Mem Only Cache:"+fromCache);
		
		
	}
	
}
