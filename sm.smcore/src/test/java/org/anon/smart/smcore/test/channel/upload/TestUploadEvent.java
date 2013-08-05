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
 * File:                org.anon.smart.smcore.test.channel.upload.TestUploadEvent.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 23, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.test.channel.upload;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.smart.smcore.test.TestClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

/**
 * @author raooll
 * 
 */
public class TestUploadEvent {

	String projectHome = System.getProperty("user.dir");
	
/*	private HTTPClientChannel postTo(SCShell shell, int port, String server,
			String uri, String post, boolean wait) throws Exception {
		Rectifier rr = new Rectifier();
		rr.addStep(new TestDistillation());
		HTTPConfig ccfg = new HTTPConfig(port, false);
		ccfg.setClient();
		ccfg.setServer(server);
		ccfg.setRectifierInstinct(rr, new TestDataFactory());
		HTTPClientChannel cchnl = (HTTPClientChannel) shell.addChannel(ccfg);
		cchnl.connect();
		ByteArrayInputStream istr = new ByteArrayInputStream(post.getBytes());
		PData d = new TestPData(null, new ContentData(istr));
		cchnl.post(uri, new PData[] { d });
		if (wait) {
			Thread.sleep(6000);
			cchnl.disconnect();
		}

		return cchnl;
	}
*/
	@Test
	public void testUploadEvents() throws Exception {
		
		String testFile = "pom.xml";
		
		this.getClass()
				.getClassLoader()
				.getResources(
						projectHome
								+ "/../sm.kernel/src/main/resources/dbscripts/hadoop-0.20.2-cdh3u5/conf/hdfs-site.xml");
		this.getClass()
				.getClassLoader()
				.getResources(
						projectHome
								+ "/../sm.kernel/src/main/resources/dbscripts/hadoop-0.20.2-cdh3u5/conf/core-site.xml");

		int port = 9081;
        int uploadPort = 9020;

		UploadServerUtilities utils = new UploadServerUtilities(port,uploadPort);
		utils.runServer("org.anon.smart.smcore.test.channel.upload.RunSmartUploadServer");
		Thread.sleep(3000);
		 TestClient clnt = new TestClient(port, "localhost", "errortenant", "ErrorCases", "ErrorCases.soa");
	     clnt.deployFromSampleJar();
	     clnt.createTenant();

/*		postTo(shell,
				port,
				"localhost",
				"/SmartOwner/AdminSmartFlow/DeployEvent",
				"{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'"

						+ "/home/raooll/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'ErrorCases.soa'}",
				true);
		postTo(shell,
				port,
				"localhost",
				"/SmartOwner/AdminSmartFlow/NewTenant",
				"{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'errortenant','enableFlow':'ErrorCases','enableFeatures':['all']}",
				false);

		System.out
				.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		postTo(shell,
				port,
				"localhost",
				"/errortenant/ErrorCases/CreatePrime",
				"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ErrorCases'}, 'create':'ErrorObject', 'data':{'name':'ohGod','embed':{'_start':'04/04/2013 10:30'}}}",
				true);

		
		Thread.sleep(100);
*/		uploadFile(projectHome + "/"+ testFile);
		Thread.sleep(3000);
		String dFile = downloadFile("pom.xml");
		Thread.sleep(3000);
		assertTrue(validate(testFile, dFile));
		
		System.out.println("All test passed ");

		utils.stopServer();
	}

    private void uploadFile(String file) {
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://localhost:9020"
				+ "/errortenant/ErrorCases/UploadEvent");

		FileBody bin = new FileBody(new File(file));
		
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("bin", bin);

		httppost.setEntity(reqEntity);

		System.out.println("executing request " + httppost.getRequestLine());
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		String response = null;
		try {
			response = httpclient.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
	}

    private String downloadFile(String file) throws Exception{
		HttpClient httpclient = new DefaultHttpClient();
		String dwnFile =   file + ".1" ;
		 HttpGet httpget = new HttpGet("http://localhost:9020/errortenant/ErrorCases/DownloadEvent/" + file);
		 HttpResponse response = httpclient.execute(httpget);
		 System.out.println(response.getStatusLine());
		 HttpEntity entity = response.getEntity();
		 if (entity != null) {
		     InputStream instream = entity.getContent();
		     try {
		         BufferedInputStream bis = new BufferedInputStream(instream);
		         
		         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(projectHome + "/" + dwnFile)));
		         int inByte;
		         while ((inByte = bis.read()) != -1 ) {
		             bos.write(inByte);
		         }
		         bis.close();
		         bos.close();
		     } catch (IOException ex) {
		         throw ex;
		     } catch (RuntimeException ex) {
		         httpget.abort();
		         throw ex;
		     } finally {
		         instream.close();
		     }
		     httpclient.getConnectionManager().shutdown();
		 }
		 return dwnFile;
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
}
