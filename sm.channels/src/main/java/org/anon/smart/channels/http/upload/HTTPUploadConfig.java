package org.anon.smart.channels.http.upload;

import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.shell.DataInstincts;
import org.anon.smart.channels.shell.SCFactory;
import org.anon.smart.channels.shell.SCType;

public class HTTPUploadConfig extends HTTPConfig {

	public HTTPUploadConfig(int port, boolean secure) {
		super(port, secure);
		
	}

	public SCType scType() {
		return SCType.external;
	}

	public SCFactory creator() {
		return new HTTPUploadFactory();
	}

}
