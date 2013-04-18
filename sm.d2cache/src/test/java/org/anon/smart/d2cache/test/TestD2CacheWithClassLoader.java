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
 * File:                org.anon.smart.d2cache.test.TestD2CahceWithClassLoader
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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.anon.utilities.loader.RelatedLoader;
import org.junit.Test;

public class TestD2CacheWithClassLoader {

	List<URL>   urlList = new ArrayList<URL>();
	
	
	@Test
	public void testMemStoreIndWithClassLoader() throws Exception
	{
		String home = System.getenv("HOME");
        String mvnrep = System.getProperty("maven.repository");
        String url = "file://" + home + "/" + mvnrep;
        String projectHome = System.getProperty("user.dir");
        File fi = new File(projectHome);
        URL fu = fi.toURL();
		urlList.add(new URL(url + "org/jboss/netty/netty/3.2.5.Final/netty-3.2.5.Final.jar"));
		urlList.add(new URL(url + "org/apache/solr/solr-core/4.0.0/solr-core-4.0.0.jar"));	
		urlList.add(new URL(url + "org/apache/solr/solr-solrj/4.0.0/solr-solrj-4.0.0.jar"));
		urlList.add(new URL(url + "org/apache/lucene/lucene-core/4.0.0/lucene-core-4.0.0.jar"));
		urlList.add(new URL(url + "org/apache/hbase/hbase/0.90.6-cdh3u5/hbase-.0.90.6-cdh3u5.jar"));
		urlList.add(new URL(url + "jcs/jcs/1.3/jcs-1.3.jar"));
		urlList.add(new URL(fu.toString() + "/target/test-classes/"));
        urlList.add(new URL(fu.toString() + "/target/classes/"));
     
		ClassLoader cl = new  RelatedLoader(urlList.toArray(new URL[0]), "MyLoader", null);
		URL[] urls = ((URLClassLoader)cl).getURLs();
		for(URL u : urls)
			System.out.println("---------:"+u.getPath());
		Class testCls = cl.loadClass("org.anon.smart.d2cache.test.D2CacheTester");
		Runnable run = (Runnable)testCls.newInstance();
		
		Thread t = new Thread(run);
		t.setContextClassLoader(cl);
		t.start();
		t.join();
		
		
		
	}
}
