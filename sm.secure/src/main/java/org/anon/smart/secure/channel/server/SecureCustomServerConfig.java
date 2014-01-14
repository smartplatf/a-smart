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
 * File:                org.anon.smart.secure.channel.server.SecureCustomServerConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-12-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Secure custom config
 *
 * ************************************************************
 * */

package org.anon.smart.secure.channel.server;

import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.HTTPDataFactory;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.smcore.channel.distill.translation.CustomTranslationStage;
import org.anon.smart.smcore.channel.distill.alteration.AlterationStage;
import org.anon.smart.smcore.channel.distill.storage.StorageStage;
import org.anon.smart.smcore.channel.server.EventErrorHandler;
import org.anon.smart.smcore.channel.server.EventDataFactory;

import org.anon.smart.secure.channel.distill.sanitization.SecureSanitizationStage;
import org.anon.smart.secure.channel.distill.storage.SecureStorageStage;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ConvertService.*;

public class SecureCustomServerConfig extends HTTPConfig
{
    public SecureCustomServerConfig(int port, boolean secure, String translate)
        throws CtxException
    {
        super(port, secure);
        Rectifier rectifier = new Rectifier();
        rectifier.addStep(new CustomTranslationStage(translate));
        rectifier.addStep(new SecureSanitizationStage()); //note this is the sanitization stage from secure package
        rectifier.addStep(new AlterationStage());
        rectifier.addStep(new SecureStorageStage());
        rectifier.setupErrorHandler(new EventErrorHandler());
        setRectifierInstinct(rectifier, new EventDataFactory());
    }

}

