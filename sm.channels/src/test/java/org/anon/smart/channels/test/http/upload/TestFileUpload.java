package org.anon.smart.channels.test.http.upload;

import java.io.File;

import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.upload.HTTPUploadConfig;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.test.http.TestDataFactory;
import org.anon.smart.channels.test.http.TestDistillation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * Example how to use multipart/form encoded POST request.
 */
public class TestFileUpload {

	String projectHome = System.getProperty("user.dir");
	
	String fileName =  projectHome + "/pom.xml";

	@Test
	public void test() throws Exception {

		prepare();
		
		HTTPConfig cfg = new HTTPUploadConfig(9020, false);
		
		Rectifier rtfr =new Rectifier();
		rtfr.addStep( new TestDistillation(true));
		
		cfg.setRectifierInstinct(rtfr, new TestDataFactory());
		SCShell shell = new SCShell();
		SmartChannel chnl = shell.addChannel(cfg);
		shell.startAllChannels();

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost("http://localhost:9020"
					+ "/a/b/c/d");

			FileBody bin = new FileBody(new File(fileName));
			StringBody comment = new StringBody("A binary file of some kind");

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("bin", bin);
			reqEntity.addPart("comment", comment);

			httppost.setEntity(reqEntity);

			System.out
					.println("executing request " + httppost.getRequestLine());
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			String response = httpclient.execute(httppost,responseHandler);
			

			File file = new File("/tmp/" + "pom.xml");
			assert (file.exists() == true);
			file.deleteOnExit();

			
			httpclient.getConnectionManager().shutdown();
			shell.stopAllChannels();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
				shell.stopAllChannels();
			} catch (Exception ignore) {
			}
		}
		System.out.println("All test passed ");
	}

	private void prepare(){
		File file = new File("/tmp/" + "pom.xml");
		if(file.exists())
			file.delete();
	}
}