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
 * File:                org.anon.smart.smcore.uploadchannel.distill.UploadStorageStage.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 9, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.uploadchannel.distill;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.channels.http.upload.data.UploadPData;
import org.anon.smart.smcore.channel.distill.storage.StorageStage;
import org.anon.smart.smcore.channel.server.EventPData;
import org.anon.smart.smcore.channel.server.EventRData;
import org.anon.smart.smcore.channel.server.EventResponses;
import org.anon.smart.smcore.inbuilt.responses.DownloadResponse;
import org.anon.smart.smcore.uploadchannel.crosslinks.CrossLinkDownloadResponse;
import org.anon.smart.smcore.uploadchannel.crosslinks.CrossLinkFSReader;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 * 
 */
public class UploadStorageStage extends StorageStage {

	@Override
	public Distillate condense(Distillate prev) throws CtxException {
		if (prev.current() instanceof EventRData) {
			EventRData r = (EventRData) prev.current();
			if (r.event().toString().contains("DownloadResponse")) {
				List<Object> resp = ((EventResponses)r.event()).responses();
				
				CrossLinkDownloadResponse dr = new CrossLinkDownloadResponse(resp.get(0));
				CrossLinkFSReader rdr = new CrossLinkFSReader(dr.reader());
				try {
					Method m = dr.reader().getClass().getDeclaredMethod("getFileAsStream", new Class[]{String.class,String.class,ClassLoader.class});
					InputStream fIs = (InputStream) m.invoke(dr.reader(),dr.fileName(),dr.group(), this.getClass().getClassLoader());
					ContentData cd = new ContentData(fIs);
					UploadPData pd = new UploadPData(r.dScope(),cd,dr.size(),dr.fileName(),dr.fileType());
					return new Distillate(prev, pd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return super.condense(prev);
	}
}
