package org.anon.smart.channels.http.upload.netty;

import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.ServerSSLContext;
import org.anon.smart.channels.http.netty.HTTPPipelineFactory;
import org.anon.smart.channels.netty.NettyServerChannel;
import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.shell.SCShell;
import org.anon.utilities.exception.CtxException;
import org.jboss.netty.channel.ChannelPipelineFactory;

public class UploadServer extends NettyServerChannel{
	
	private ServerSSLContext _sslContext;

	public UploadServer(SCShell shell, ExternalConfig cfg) throws CtxException {
		super(shell, cfg);
	}

	@Override
	protected ChannelPipelineFactory pipelineFactory() throws CtxException {
		return new UploadPipelineFactory(_id, _shell, _config, _sslContext);
	}

	@Override
	protected void initialize(SCShell shell, SCConfig cfg) throws CtxException {
		HTTPConfig httpcfg = (HTTPConfig)cfg;
        if (httpcfg.makeSecure())
            _sslContext = new ServerSSLContext(httpcfg);
	}

}
