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
 * File:                org.anon.smart.d2cache.test.D2CacheTester
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 2, 2013
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
import org.anon.utilities.test.reflect.MyComplexTestObject;

public class D2CacheTester implements Runnable {

	@Override
	public void run() {
		System.out.println("D2CacheTester is loaded by: " + this.getClass().getClassLoader());
		int flags = D2CacheScheme.BROWSABLE_CACHE;
		String name = "TestCache";
		D2CacheConfig config = new BasicD2CacheConfig();
        String home = System.getenv("HOME");
		config.createIndexConfig(home + "/solr/solr-datastore/");
		config.createStoreConfig("hadoop", "2181", "hadoop:60000", false);
		D2Cache cache = null;
		try {
			cache = D2CacheScheme.getCache(scheme.memstoreind, name, flags, config);
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(cache != null);
		Object obj = new MyComplexTestObject();
		String[] keys = new String[]{"myCompObj"};
		try {
			TestCacheUtil.setTestData(cache, obj, keys, "MyComplexTestObject");
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Reader reader = null;
		try {
			reader = cache.myReader();
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object fromCache = null;
		try {
			fromCache = reader.lookup("MyComplexTestObject", "myCompObj");
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(fromCache != null);
		assertTrue(fromCache instanceof MyComplexTestObject);
		
		System.out.println("Read from Mem Store Ind Cache:"+fromCache);
		
	}

	
}
