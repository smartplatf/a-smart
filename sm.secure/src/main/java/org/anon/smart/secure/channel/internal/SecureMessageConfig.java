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
 * File:                org.anon.smart.secure.channel.internal.SecureMessageConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                17-02-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A secure message config for internal messages
 *
 * ************************************************************
 * */

package org.anon.smart.secure.channel.internal;

import org.anon.smart.smcore.channel.internal.MessageConfig;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.smcore.channel.distill.translation.TranslationStage;
import org.anon.smart.smcore.channel.distill.alteration.AlterationStage;
import org.anon.smart.smcore.channel.distill.storage.StorageStage;
import org.anon.smart.smcore.channel.server.EventErrorHandler;
import org.anon.smart.smcore.channel.server.EventDataFactory;
import org.anon.smart.smcore.events.SmartEvent;

import org.anon.smart.secure.channel.distill.sanitization.SecureSanitizationStage;
import org.anon.smart.secure.channel.distill.storage.SecureStorageStage;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ConvertService.*;

public class SecureMessageConfig extends MessageConfig
{
    public SecureMessageConfig(SmartEvent message)
        throws CtxException
    {
        super(message);
    }

    @Override
    protected void addSteps(Rectifier rectifier)
    {
        rectifier.addStep(new TranslationStage(translator.json));
        rectifier.addStep(new SecureSanitizationStage());
        rectifier.addStep(new AlterationStage());
    }

    @Override
    protected void addLastSteps(Rectifier rectifier)
    {
        rectifier.addStep(new SecureStorageStage());
    }

}

