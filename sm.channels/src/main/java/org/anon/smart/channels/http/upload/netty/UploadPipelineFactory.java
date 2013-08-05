package org.anon.smart.channels.http.upload.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import java.util.UUID;

import org.anon.smart.channels.MessageReader;
import org.anon.smart.channels.http.ServerSSLContext;
import org.anon.smart.channels.http.netty.NettyResponseReader;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.shell.SCShell;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

public class UploadPipelineFactory implements ChannelPipelineFactory {

	private SCShell _shell;
	private SCConfig _config;
	private UUID _channelID;
	private ServerSSLContext _sslContext;
	private UploadRequestReader _uploadRReader;
    private DownloadRequestReader _downloadRReader;

	public UploadPipelineFactory(UUID channelID, SCShell shell, SCConfig cfg,
			ServerSSLContext ctx) {
		_shell = shell;
		_config = cfg;
		_sslContext = ctx;
		_channelID = channelID;
		_uploadRReader = new UploadRequestReader("");
		_downloadRReader = new DownloadRequestReader();
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		// TODO add the necessary pipeline

		if (_sslContext != null)
			pipeline.addLast("ssl",
					new SslHandler(_sslContext.createEngine(false)));

		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());

		// Remove the following line if you don't want automatic content
		// compression.
		pipeline.addLast("deflater", new HttpContentCompressor());

		//pipeline.addLast("handler", new HttpUploadServerHandler());
		pipeline.addLast("handler", new HttpUploadServerHandler(_shell, _config,_uploadRReader, _downloadRReader, _channelID));
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

		return pipeline;
	}

}
