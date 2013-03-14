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

package org.anon.smart.d2cache.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.QueryObject;
import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.D2CacheScheme.scheme;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.test.reflect.ComplexTestObject;
import org.anon.utilities.test.reflect.SimpleTestObject;
import org.junit.Test;

public class TestD2Cache {

	//@Test
	public void testMemOnlyCache() throws CtxException
	{
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		D2Cache cache = D2CacheScheme.getCache(scheme.mem, name, flags);
	
		SimpleTestObject obj = new SimpleTestObject();
		Object[] keys = new String[]{"myObj"};
		TestCacheUtil.setTestData(cache, obj, keys, "SimpleTestObject");
		
		Reader reader = cache.myReader();
		Object fromCache = reader.lookup("SimpleTestObject", "myObj");
		
		assertTrue(fromCache != null);
		
		System.out.println("Read from Mem Only Cache:"+fromCache);
		
		
		
	}
	
	//@Test
	public void testMemIndexCache() throws Exception{
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		
		D2Cache cache = D2CacheScheme.getCache(scheme.memind, name, flags);
		
		Object obj = new SimpleTestObject();
		String[] keys = new String[]{"myObj2"};
		TestCacheUtil.setTestData(cache, obj, keys, "SimpleTestObject");
		
		Reader reader = cache.myReader();
		Object fromCache = reader.lookup("SimpleTestObject", "myObj2");
		
		assertTrue(fromCache != null);
		
		System.out.println("Read from Mem Ind Cache:"+fromCache);
		
		System.out.println("Searching:");
		QueryObject qo = new QueryObject();
		qo.addCondition("_string", "SimpleTestObject");
		qo.setResultType(SimpleTestObject.class);
		List<Object> resultSet = reader.search("SimpleTestObject", qo);
		assertTrue(resultSet.size()>0);
		for(Object o : resultSet)
		{
			System.out.println("Result:"+o);
			assertTrue(o instanceof SimpleTestObject);
			
		}
	}
	
	//@Test
	public void testMemIndexCacheWithComplexObject() throws CtxException {
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		D2Cache cache = D2CacheScheme.getCache(scheme.memind, name, flags);
		
		Object obj = new ComplexTestObject();
		String[] keys = new String[]{"compObject"};
		TestCacheUtil.setTestData(cache, obj, keys, "ComplexTestObject");
		
		Reader reader = cache.myReader();
		Object fromCache = reader.lookup("ComplexTestObject", "compObject");
		
		assertTrue(fromCache != null);
		
		System.out.println("Read from Mem Ind Cache:"+fromCache);
		
		System.out.println("Searching:");
		QueryObject qo = new QueryObject();
		qo.addCondition("id", "compObject");
		qo.setResultType(ComplexTestObject.class);
		List<Object> resultSet = reader.search("ComplexTestObject", qo);
		assertTrue(resultSet.size()>0);
		for(Object o : resultSet)
		{
			System.out.println("Result:"+o);
			assertTrue(o instanceof ComplexTestObject);
			
		}
	}
	
	@Test
	public void testMemStoreIndexCache() throws CtxException {
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		
		D2Cache cache = D2CacheScheme.getCache(scheme.memstoreind, name, flags);
		
		Object obj = new SimpleTestObject();
		String[] keys = new String[]{"myObj2"};
		TestCacheUtil.setTestData(cache, obj, keys, "SimpleTestObject");
		
		Reader reader = cache.myReader();
		Object fromCache = reader.lookup("SimpleTestObject", "myObj2");
		
		assertTrue(fromCache != null);
		
		System.out.println("Read from Mem Ind Cache:"+fromCache);
		
	}
}
