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
 * File:                org.anon.smart.d2cache.test.TestObjectStorage
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 5, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.test;

import static org.junit.Assert.assertTrue;

import org.anon.smart.d2cache.BasicD2CacheConfig;
import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.D2CacheConfig;
import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.D2CacheScheme.scheme;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.test.reflect.ComplexTestObject;
import org.anon.utilities.test.reflect.MyComplexTestObject;
import org.junit.Test;

public class TestObjectStorage {

	//@Test
	public void testMyTestClassStorage() throws CtxException
	{
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		D2CacheConfig config = new BasicD2CacheConfig();
        String home = System.getenv("HOME");
		config.createIndexConfig(home + "/solr/solr-datastore/");
		config.createStoreConfig("hadoop", "2181", "hadoop:60000", false);
		D2Cache cache = D2CacheScheme.getCache(scheme.memstoreind, name, flags, config);
		
		Object obj = new MyTestClass();
		String[] keys = new String[]{"myTestClass"};
		TestCacheUtil.setTestData(cache, obj, keys, "MyTestClass");
		
		Reader reader = cache.myReader();
		Object fromCache = reader.lookup("MyTestClass", "myTestClass");
		
		assertTrue(fromCache != null);
		assertTrue(fromCache instanceof MyTestClass);
		
		System.out.println("Read from Mem Store Ind Cache:"+fromCache);
	}
	@Test
	public void testPersistenceForMyTestClass()
			throws CtxException {
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		D2CacheConfig config = new BasicD2CacheConfig();
        String home = System.getenv("HOME");
		config.createIndexConfig(home + "/solr/solr-datastore/");
		config.createStoreConfig("hadoop", "2181", "hadoop:60000", false);
		D2Cache cache = D2CacheScheme.getCache(scheme.memstoreind, name, flags, config);

		Reader reader = cache.myReader();
		Object fromCache = reader.lookup("MyTestClass", "myTestClass");

		assertTrue(fromCache != null);
		assertTrue(fromCache instanceof MyTestClass);

		System.out.println("Read from Mem Store Ind Cache:" + fromCache);

	}
}
