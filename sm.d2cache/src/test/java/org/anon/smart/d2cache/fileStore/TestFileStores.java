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
 * File:                org.anon.smart.d2cache.fileStore.TestFileStores.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 28, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.d2cache.fileStore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import junit.framework.Assert;

import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.fileStore.FileStoreReader;
import org.anon.smart.d2cache.store.fileStore.FileStoreTransaction;
import org.anon.smart.d2cache.store.fileStore.disk.DiskFSConfig;
import org.anon.smart.d2cache.store.fileStore.hadoop.HadoopFSConfig;
import org.anon.utilities.exception.CtxException;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * @author raooll
 * 
 */
public class TestFileStores {

	String projectHome = System.getProperty("user.dir");
	String fsStore  = projectHome + "/fsStore";
	private String masterFl = "pom.xml";
	private String fl1 = "pom.xml.1";
	private String fl2 = "pom.xml.2";

	@Test
	public void testDiskFS() throws Exception {

		prepare();
		
		System.setProperty("Smart.Development.Mode", "true");

		TestConfig tsConf = new TestConfig();
		D2Cache dc = D2CacheScheme.getCache(D2CacheScheme.scheme.filestore,
				"testStore", 1,tsConf);

		D2CacheTransaction tx = dc.startTransaction(UUID.randomUUID());

		String[] one = {fl1 , fl1};		
		String[] two = {fl2 , fl2};		
	
		StoreItem sItem1 = new StoreItem(null, one, "testGroup");
		StoreItem sItem2 = new StoreItem(null, two, "testGroup");

		tx.add(sItem1);
		tx.add(sItem2);

		tx.commit();
		
		String fsRepo = ((DiskFSConfig)tsConf.getDiskStoreConfig()).baseDirectory();
	
		
		FileStoreReader r = (FileStoreReader) dc.myReader();
		InputStream s1 = r.getFileAsStream( fl1,sItem1.group(),null);
		InputStream s2 = r.getFileAsStream( fl2,sItem2.group(),null);
	
		assertNotNull(s1);	
		assertNotNull(s2);
	
		writeToFile("dFile1", s1);
		writeToFile("dFile2", s2);
		
		assertTrue(validate(masterFl, "dFile1"));
		assertTrue(validate(masterFl, "dFile2"));
		
		
		FileUtils.deleteDirectory(new File(fsRepo));


	}

	/**
	 * 
	 */

	private void prepare() {
		try {
			Files.copy(new File(projectHome + "/pom.xml"), new File(projectHome
					+ "/pom.xml.1"));
			Files.copy(new File(projectHome + "/pom.xml"), new File(projectHome
					+ "/pom.xml.2"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testHadoopFS() throws Exception {

		prepare();
	
		System.setProperty("Smart.Development.Mode", "false");
		TestConfig tsConf = new TestConfig();
		D2Cache dc = D2CacheScheme.getCache(D2CacheScheme.scheme.filestore,
				"testStore", 1,tsConf);

		D2CacheTransaction tx = dc.startTransaction(UUID.randomUUID());

		String[] one = {fl1 , fl1};		
		String[] two = {fl2 , fl2};		
		
		StoreItem sItem1 = new StoreItem(null, one, "testGroup");
		StoreItem sItem2 = new StoreItem(null, two, "testGroup");

		tx.add(sItem1);
		tx.add(sItem2);

		tx.commit();
		
		Configuration conf = new Configuration();
		conf.addResource(new Path(
				System.getProperty("user.dir")
						+ "/../sm.kernel/src/main/resources/dbscripts/hadoop-0.20.2-cdh3u5/conf/core-site.xml"));
		conf.addResource(new Path(
				System.getProperty("user.dir")
						+ "/../sm.kernel/src/main/resources/dbscripts/hadoop-0.20.2-cdh3u5/conf/hdfs-site.xml"));

		FileStoreReader r = (FileStoreReader) dc.myReader();
		System.out.println(fsStore + "/" + fl1);		

		InputStream s1 = r.getFileAsStream( fl1 ,sItem1.group(),null);
		InputStream s2 = r.getFileAsStream( fl2 ,sItem2.group(),null);
		
		assertNotNull(s1);	
		assertNotNull(s2);
		
		writeToFile("hFile1", s1);
		writeToFile("hFile2", s2);
		
		assertTrue(validate(masterFl, "hFile1"));
		assertTrue(validate(masterFl, "hFile2"));
		
		//deleteFile("hFile1");
		//deleteFile("hFile2");
		
	}
	
	private void writeToFile(String flName , InputStream flStream) throws Exception{
		 try {
	         BufferedInputStream bis = new BufferedInputStream(flStream);
	         
	         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(projectHome + "/" + flName)));
	         int inByte;
	         while ((inByte = bis.read()) != -1 ) {
	             bos.write(inByte);
	         }
	         bis.close();
	         bos.close();
	     } catch (Exception ex) {
	         throw ex;
	     } 
	}
	
	private boolean validate(String oFile, String dFile)  throws Exception{

		File oriFile = new File(projectHome + "/" + oFile);
		File dwnFile = new File(projectHome + "/" + dFile);

		if (!dwnFile.exists()) {
			return false;
		}
		if (oriFile.length() != dwnFile.length()) {
			return false;
		}
		
		if (!org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(
				projectHome + "/" + oFile)).toString().equals(
				org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(projectHome
						+ "/" + dFile)).toString())) {
			return false;
		}

		dwnFile.deleteOnExit();
		return true;
	}
	
	private void deleteFile(String fl) throws Exception {
		File f = new File(projectHome + "/" + fl);
		if (f.exists())
			f.deleteOnExit();
	}
	
}
