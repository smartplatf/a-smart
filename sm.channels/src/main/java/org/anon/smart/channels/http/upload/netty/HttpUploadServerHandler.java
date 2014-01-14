package org.anon.smart.channels.http.upload.netty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import org.anon.smart.channels.MessageReader;
import org.anon.smart.channels.http.upload.DownloadRequest;
import org.anon.smart.channels.http.upload.UploadRequest;
import org.anon.smart.channels.netty.NettyRoute;
import org.anon.smart.channels.shell.DataInstincts;
import org.anon.smart.channels.shell.SCConfig;
import org.anon.smart.channels.shell.SCShell;
import org.anon.utilities.services.AssertionService;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelFutureProgressListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DefaultFileRegion;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.FileRegion;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.DiskAttribute;
import org.jboss.netty.handler.codec.http.multipart.DiskFileUpload;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.NotEnoughDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.stream.ChunkedFile;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.util.CharsetUtil;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;

public class HttpUploadServerHandler extends SimpleChannelUpstreamHandler {

	private UUID _channelID;
	private SCShell _shell;
	private DataInstincts _instinct;
	private MessageReader _upReader;
	private MessageReader _dwnReader;

	public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	public static final int HTTP_CACHE_SECONDS = 60;

	private String tmpUploadDir;
	private HttpRequest request;
	private boolean readingChunks;
	private HashMap<String, String> filesUploaded;
	private String customGroup;
	private Map<String, String> postData;

	private static final HttpDataFactory factory = new DefaultHttpDataFactory(
			DefaultHttpDataFactory.MINSIZE); // Disk if size exceed MINSIZE

	private HttpPostRequestDecoder decoder;
	static {
		DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
															// on exit (in
															// normal
															// exit)
		DiskFileUpload.baseDirectory = "/tmp/"; // system temp
												// directory
		DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
														// exit (in normal exit)
		DiskAttribute.baseDirectory = null; // system temp directory
	}

	public HttpUploadServerHandler() {

	}

	public HttpUploadServerHandler(SCShell shell, SCConfig cfg,
			MessageReader upRdr ,MessageReader dwnRdr , UUID channelID) {
		_shell = shell;
		_instinct = cfg.instinct();
		System.out.println("cfg :: " + cfg);
		System.out.println("instinct :: " + _instinct);
		tmpUploadDir = System.getProperty("java.io.tmpdir");		
		System.out.println("tmpUploadDir :: " + tmpUploadDir);
        DiskFileUpload.baseDirectory = tmpUploadDir;
		_upReader = upRdr;
		_dwnReader = dwnRdr;
		_channelID = channelID;
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		if (decoder != null) {
			decoder.cleanFiles();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		if (!readingChunks) {
			// clean previous FileUpload if Any
			if (decoder != null) {
				decoder.cleanFiles();
				decoder = null;
			}

			request = (HttpRequest) e.getMessage();
            System.out.println(request);
			// System.out.println("request.getMethod() == " +
			// request.getMethod());

			if (request.getMethod() == HttpMethod.GET) {
				// Check and server the files

				getFiles(ctx, request,e.getChannel());
				return;
			}
            else if (request.getMethod() == HttpMethod.OPTIONS) {
                Object send = _upReader.transmitDefault();
                NettyRoute route = new NettyRoute(e.getChannel(), _channelID);
                route.send(send);
                System.out.println("Have to send.. " + send);
                return;
            }

			// if GET Method: should not try to create a
			// HttpPostRequestDecoder
			try {
				decoder = new HttpPostRequestDecoder(factory, request);
			} catch (ErrorDataDecoderException e1) {
				e1.printStackTrace();
				Channels.close(e.getChannel());
				return;
			} catch (IncompatibleDataDecoderException e1) {
				// GET Method: should not try to create a
				// HttpPostRequestDecoder
				// So OK but stop here
				return;
			}

			if (request.isChunked()) {
				// Chunk version
				readingChunks = true;
			} else {
				// Not chunk version
				readHttpDataAllReceive(e.getChannel());
				// writeResponse(new UploadResponse("success",
				// "file uploaded successfully"), ctx);
				// ctx.getChannel().close();
			}
		} else {
			// New chunk is received
			HttpChunk chunk = (HttpChunk) e.getMessage();
			try {
				decoder.offer(chunk);
			} catch (ErrorDataDecoderException e1) {
				e1.printStackTrace();
				Channels.close(e.getChannel());
				return;
			}
			// example of reading only if at the end
			if (chunk.isLast()) {
				System.out
						.println("Reading the last chunk will close the channel now");
				readHttpDataAllReceive(e.getChannel());
				readingChunks = false;
				System.out
						.println("Reading the last chunk will close the channel now1");
				// writeResponse(new UploadResponse("success",
				// "file uploaded successfully"), ctx);
				// ctx.getChannel().close();
			}
		}
	}

	private String getFileName(String uri) {

		String fileName = null;
		String[] params = uri.toString().split("/");

		if (params.length == 4) {
			fileName = params[3];
		}
		return fileName;

	}

	/**
	 * Example of reading all InterfaceHttpData from finished transfer
	 * 
	 * @param channel
	 */
	private void readHttpDataAllReceive(Channel channel) throws Exception {
		List<InterfaceHttpData> datas = null;
		try {
			datas = decoder.getBodyHttpDatas();
		} catch (NotEnoughDataDecoderException e1) {
			// Should not be!
			e1.printStackTrace();
			Channels.close(channel);
			return;
		}
		filesUploaded = new HashMap<String, String>();
        postData = new HashMap<String, String>();
		for (InterfaceHttpData data : datas) {
			writeHttpData(data, channel);
		}
		handleUploadedFile(filesUploaded, channel);
	}

	private void writeHttpData(InterfaceHttpData data, Channel channel)
			throws Exception {

		if (data.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) data;
			String value;
			try {
				value = attribute.getValue();
                String name = attribute.getName();
                if (name.equals("group"))
                    customGroup = value;
                else
                    postData.put(name, value);
                System.out.println("Got HTTP data: " + attribute.getName() + ":" + attribute.getValue() + ":" + customGroup);
				// System.out.println("Attribute : " + attribute.getName());
				// System.out.println("AttributeValue : " + value);
			} catch (IOException e1) {
				// Error while reading data from File, only print name and error
				e1.printStackTrace();
				System.out.println("\r\nBODY Attribute: "
						+ attribute.getHttpDataType().name() + ": "
						+ attribute.getName() + " Error while reading value: "
						+ e1.getMessage() + "\r\n");
				return;
			}
			if (value.length() > 100) {
				// System.out.println("\r\nBODY Attribute: "
				// + attribute.getHttpDataType().name() + ": "
				// + attribute.getName() + " data too long\r\n");
			} else {
				// System.out.println("\r\nBODY Attribute: "
				// + attribute.getHttpDataType().name() + ": "
				// + attribute.toString() + "\r\n");
			}
		} else {
			if (data.getHttpDataType() == HttpDataType.FileUpload) {
				FileUpload fileUpload = (FileUpload) data;

				if (fileUpload.isCompleted()) {
					// fileUpload.isInMemory();// tells if the file is in Memory
					// or on File
					try {
						File upld = new File(tmpUploadDir + File.separator 
								+ fileUpload.getFilename());
						fileUpload.renameTo(upld);
						filesUploaded.put(upld.getAbsolutePath(),
								fileUpload.getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out
							.println("\tFile to be continued but should not!\r\n");
				}
			}
		}
	}

	private void handleUploadedFile(HashMap files, Channel channel)
			throws Exception {

		UploadRequest r = new UploadRequest(request, files, customGroup, postData);
		NettyRoute route = new NettyRoute(channel, _channelID);
		boolean transmitdefault = _instinct.whenMessage(route, r, _upReader);
		if (transmitdefault) {
			Object send = _upReader.transmitDefault();
			route.send(send);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		try {

			
			ctx.getChannel().close();
			// Do Something
		} catch (Exception e1) {
			// can't help
			e1.printStackTrace();
		}

	}

	
	private static void sendError(ChannelHandlerContext ctx,
			HttpResponseStatus status) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				status);
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
				"text/plain; charset=UTF-8");
		response.setContent(ChannelBuffers.copiedBuffer(
				"Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));

		// Close the connection as soon as the error message is sent.
		ctx.getChannel().write(response)
				.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * When file timestamp is the same as what the browser is sending up, send a
	 * "304 Not Modified"
	 * 
	 * @param ctx
	 *            Context
	 */
	private static void sendNotModified(ChannelHandlerContext ctx) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.NOT_MODIFIED);
		setDateHeader(response);

		// Close the connection as soon as the error message is sent.
		ctx.getChannel().write(response)
				.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Sets the Date header for the HTTP response
	 * 
	 * @param response
	 *            HTTP response
	 */
	private static void setDateHeader(HttpResponse response) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT,
				Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

		Calendar time = new GregorianCalendar();
		response.setHeader(DATE, dateFormatter.format(time.getTime()));
	}

	/**
	 * Sets the Date and Cache headers for the HTTP Response
	 * 
	 * @param response
	 *            HTTP response
	 * @param fileToCache
	 *            file to extract content type
	 */
	private static void setDateAndCacheHeaders(HttpResponse response,
			File fileToCache) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT,
				Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

		// Date header
		Calendar time = new GregorianCalendar();
		response.setHeader(DATE, dateFormatter.format(time.getTime()));

		// Add cache headers
		time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
		response.setHeader(EXPIRES, dateFormatter.format(time.getTime()));
		response.setHeader(HttpHeaders.Names.CACHE_CONTROL, "private, max-age="
				+ HTTP_CACHE_SECONDS);
		response.setHeader(LAST_MODIFIED,
				dateFormatter.format(new Date(fileToCache.lastModified())));
	}

	/**
	 * Sets the content type header for the HTTP Response
	 * 
	 * @param response
	 *            HTTP response
	 * @param file
	 *            file to extract content type
	 */
	private static void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
				mimeTypesMap.getContentType(file.getPath()));
	}

	private void getFiles(ChannelHandlerContext ctx, HttpRequest request,
			Channel channel) throws Exception {
		String requestUri = request.getUri();
		String[] params = requestUri.split("/");
		DownloadRequest req = new DownloadRequest(request);
		NettyRoute route = new NettyRoute(channel, _channelID);
		boolean transmitdefault = _instinct.whenMessage(route, req, _dwnReader);
	}

}
