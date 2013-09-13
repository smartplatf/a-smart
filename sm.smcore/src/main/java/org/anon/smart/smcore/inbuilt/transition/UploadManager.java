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

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.assertion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.lang.reflect.Constructor;

import org.anon.smart.d2cache.Reader;
import org.anon.smart.base.dspace.TransactDSpace;
import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.FileStoreCache;
import org.anon.smart.d2cache.store.fileStore.FileStoreReader;
import org.anon.smart.smcore.inbuilt.data.SmartFileObject;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.inbuilt.events.DownloadEvent;
import org.anon.smart.smcore.inbuilt.events.UploadEvent;
import org.anon.smart.smcore.inbuilt.responses.DownloadResponse;
import org.anon.smart.smcore.inbuilt.responses.UploadResponse;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.utilities.exception.CtxException;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;

/**
 * @author raooll
 * 
 */
public class UploadManager {

	private static HashMap<String,String> cType = new HashMap<String, String> ();

	static {
		cType.put(".gz","application/gzip");
		cType.put(".jar","application/java-archive");
		cType.put(".pdf","application/pdf");
		cType.put(".zip","application/zip");
		cType.put(".gif","image/gif");
		cType.put(".jpeg","image/jpeg");
		cType.put(".bmp","image/bmp");
		cType.put(".jpg","image/jpeg");
		cType.put(".jpe","image/jpeg");
		cType.put(".png","image/png");
		cType.put(".svg","image/svg+xml");
		cType.put(".tiff","image/tiff");
		cType.put(".xml","application/xml");
	
	}
	
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
	 
	private String getFileExtension(String flName){
		
		if(flName== null)
		return null;
		
		if(flName.lastIndexOf('.') == -1)
		    	return "";
					
		return flName.substring(flName.lastIndexOf("."), flName.length());
	}

	
	private String getFileType(String flName){
		
		if(flName== null)
		return null;
		
		if(flName.lastIndexOf('.') == -1)
		    	return "application/octet-stream";
					
		String type = flName.substring(flName.lastIndexOf("."), flName.length());
		
		if(cType.get(type) != null)
			return cType.get(type);
		else
		    	return "application/octet-stream";
	}


	public void handleUploads(UploadEvent u, FlowAdmin adm) throws CtxException {

		TransitionContext ctx = (TransitionContext) threads().threadContext();
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		RuntimeShell rshell = (RuntimeShell) tenant.runtimeShell();
		TransactDSpace space = rshell.getSpaceFor(adm.myFlow());
		D2Cache fsCache = space.fsCacheImpl();
		Reader readr = fsCache.myReader();
		String baseDir = ((FileStoreReader) readr).getBaseDir();
		
		Map<String, String> files = u.getFiles();

		HashMap<String,String> umap = new HashMap<String, String>();

		for (String f : files.keySet()) {

			File file = new File(f.toString());
			SmartFileObject fo = new SmartFileObject();
			
			String[] fs = f.split(File.separator);
			String filenm = fs[fs.length -1];
			String fileExtension = getFileExtension(filenm);			

			fo._dateOfUpload = new Date();
			fo._fieldName = files.get(f);
			fo._uploadId = UUID.randomUUID();
			fo._fileName = fo._uploadId.toString() + fileExtension;
			fo._fileSrc = f.toString();
			fo._itemId = UUID.randomUUID();
			fo._group = adm.myFlow();
			fo._size = file.length();
			fo._fileType = getFileType(filenm);

			fo._md5sum = getMd5Sum(file);
			fo._fileLocation = u.getUploadUri();
			fo._canonicalLocation = baseDir + File.separator +  tenant.getName() + File.separator + adm.myFlow() + File.separator + fo._fileName;

            Object foo = fo;
            SmartData data = (SmartData)foo;
            data.smart___setGroup(adm.myFlow());


            if (u.getCustomGroup() != null)
            {
                String grp = u.getCustomGroup();
                String flow = adm.myFlow();
                CrossLinkDeploymentShell shell = tenant.deploymentShell();
                CrossLinkFlowDeployment dep = shell.deploymentFor(flow);
                assertion().assertNotNull(dep, "Cannot find the deployment for " + flow);
                String clsname = dep.classFor(grp);
                assertion().assertNotNull(clsname, "Cannot find the deployment class for: " + grp + " in " + flow);
                Class cls = shell.primeClass(flow, grp); 
                assertion().assertNotNull(cls, "Cannot find the class for: " + grp + ":" + clsname);
                
		try
                {
                    Constructor cons = cls.getDeclaredConstructor(String.class, String.class, String.class, Map.class);
                    assertion().assertNotNull(cons, "The object should have a constructor <init>(fileName, filesrc)");
                    Object obj = cons.newInstance(fo._fileName, fo._fileSrc, fo._canonicalLocation, u.getPostData());
                    SmartData d = (SmartData)obj;
                    System.out.println("Object created in state: " + d.utilities___currentState());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    except().te("Cannot create an object of type: " + grp + ":" + e.getMessage());
                }
            }
			
			if (ctx != null) {
				ctx.atomicity().includeUpload(fo, adm);
				umap.put(fo._fieldName,fo._fileName);
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
		FileStoreReader fr = (FileStoreReader) readr;
		String flName = tenant.getName() + File.separator + fOb._group + File.separator + fOb._fileName;
		assertion().assertNotNull( fr.getFileAsStream(flName,fOb._group , null),"File does not exist on server");
		DownloadResponse r = new DownloadResponse(readr, fOb._size,flName,fOb._group,fOb._fileType);


	}
}
