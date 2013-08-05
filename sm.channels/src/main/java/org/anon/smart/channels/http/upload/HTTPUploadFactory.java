package org.anon.smart.channels.http.upload;

import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.upload.netty.UploadServer;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.shell.SCFactory;
import org.anon.smart.channels.shell.SCShell;
import org.anon.utilities.exception.CtxException;

public class HTTPUploadFactory implements SCFactory {
	public HTTPUploadFactory() {
	}

	public SmartChannel createSC(SCShell shell, SCConfig cfg)
			throws CtxException {
		// default implementation is netty. There is no other currently.
		return new UploadServer(shell, (HTTPConfig) cfg);
	}
}
