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
 * File:                org.anon.smart.smcore.inbuilt.transition.UploadManager.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 21, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.inbuilt.transition;

import static org.anon.utilities.objservices.ObjectServiceLocator.threads;
import static org.anon.utilities.services.ServiceLocator.except;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.anon.smart.d2cache.Reader;
import org.anon.smart.base.dspace.TransactDSpace;
import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.FileStoreCache;
import org.anon.smart.d2cache.store.fileStore.FileStoreReader;
import org.anon.smart.smcore.data.SmartFileObject;
import org.anon.smart.smcore.inbuilt.events.DownloadEvent;
import org.anon.smart.smcore.inbuilt.events.UploadEvent;
import org.anon.smart.smcore.inbuilt.responses.DownloadResponse;
import org.anon.smart.smcore.inbuilt.responses.UploadResponse;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 * 
 */
public class UploadManager {
	
	private String getMd5Sum(File fl) throws CtxException {
		String md5 = "";
		try {
			FileInputStream fis = new FileInputStream(fl);
			md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
		} catch (Exception e) { // TODO Auto-generated catch block
								// e.printStackTrace();
			except().rt(
					e,
					new CtxException.Context("Exception in finding md5sum : "
							+ fl.getName(), "Exception"));
		}
		return md5;
	}
	 
	
	private String getFileType(String flName){
		
		if(flName== null)
		return null;
		
		if(flName.lastIndexOf('.') == -1)
			return null;
					
		String type = flName.substring(flName.lastIndexOf("."), flName.length());
		return type;
	}
	public void handleUploads(UploadEvent u, FlowAdmin adm) throws CtxException {

		Map<String, String> files = u.getFiles();

		HashMap<UUID, Object> umap = new HashMap<UUID, Object>();

		for (String f : files.keySet()) {

			File file = new File(f.toString());
			SmartFileObject fo = new SmartFileObject();

			String[] fs = f.split("/");

			fo._dateOfUpload = new Date();
			fo._fieldName = files.get(f);
			fo._fileName = fs[fs.length - 1];
			fo._uploadId = UUID.randomUUID();
			fo._fileSrc = f.toString();
			fo._itemId = UUID.randomUUID();
			fo._group = adm.myFlow();
			fo._size = file.length();
			fo._fileType = getFileType(fo._fileName);

			fo._md5sum = getMd5Sum(file);
			fo._fileLocation = u.getUploadUri();
			
			TransitionContext ctx = (TransitionContext) threads()
					.threadContext();
			if (ctx != null) {
				ctx.atomicity().includeUpload(fo, adm);
				umap.put(fo._uploadId, fo._fieldName);
			}
		}

		UploadResponse r = new UploadResponse("success", umap);
	}

	public void handleDownloads(DownloadEvent u, SmartFileObject fOb)
			throws CtxException {

		TransitionContext ctx = (TransitionContext) threads().threadContext();
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		RuntimeShell rshell = (RuntimeShell) tenant.runtimeShell();
		TransactDSpace space = rshell.getSpaceFor(fOb._group);

		D2Cache fsCache = space.fsCacheImpl();

		Reader readr = fsCache.myReader();

		DownloadResponse r = new DownloadResponse(readr, fOb._size,u.getFileName(),fOb._group,fOb._fileType);


	}
}
