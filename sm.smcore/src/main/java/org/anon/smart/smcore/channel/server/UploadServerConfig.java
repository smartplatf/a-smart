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
 * File:                org.anon.smart.smcore.channel.server.UploadServerConfig.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 20, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.channel.server;

import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.http.upload.HTTPUploadConfig;
import org.anon.smart.smcore.channel.distill.alteration.AlterationStage;
import org.anon.smart.smcore.channel.distill.sanitization.SanitizationStage;
import org.anon.smart.smcore.channel.distill.storage.StorageStage;
import org.anon.smart.smcore.channel.distill.translation.TranslationStage;
import org.anon.smart.smcore.uploadchannel.distill.UploadStorageStage;
import org.anon.utilities.objservices.ConvertService.translator;

/**
 * @author raooll
 * 
 */
public class UploadServerConfig extends HTTPUploadConfig {

	public UploadServerConfig(int port, boolean secure) {
		super(port, secure);
		Rectifier rectifier = new Rectifier();


		rectifier.addStep(new TranslationStage(translator.json));
		rectifier.addStep(new SanitizationStage());
		rectifier.addStep(new AlterationStage());
		rectifier.addStep(new UploadStorageStage());

		rectifier.setupErrorHandler(new EventErrorHandler());
		setRectifierInstinct(rectifier, new EventDataFactory());

	}
}
