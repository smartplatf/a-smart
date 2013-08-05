package org.anon.smart.channels.test.http.upload;

import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.upload.HTTPUploadConfig;
import org.anon.smart.channels.shell.SCShell;

/**
 * Example how to use unbuffered chunk-encoded POST request.
 */
public class TestUploadServer {

	@Test
	public void startServer() throws Exception {
		HTTPConfig cfg = new HTTPUploadConfig(9020, false);
		SCShell shell = new SCShell();
		SmartChannel chnl = shell.addChannel(cfg);
		shell.startAllChannels();
		Thread.sleep(5000000);
	}
	
	
	
}
