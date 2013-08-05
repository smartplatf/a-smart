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
 * File:                org.anon.smart.smcore.uploadchannel.distill.UploadTranlationStage.java
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

import java.util.List;

import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.smcore.channel.distill.translation.TranslationStage;
import org.anon.smart.smcore.channel.server.EventRData;
import org.anon.smart.smcore.channel.server.EventResponses;
import org.anon.smart.smcore.inbuilt.responses.DownloadResponse;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.objservices.ConvertService.translator;

/**
 * @author raooll
 * 
 */
public class UploadTranslationStage extends TranslationStage {

	public UploadTranslationStage(translator t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Distillate condense(Distillate prev) throws CtxException {
		
		return super.condense(prev);
	}

}
