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
 * File:                org.anon.smart.smcore.channel.server.EventDataFactory
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A factory for creating data events
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.server;

import java.io.InputStream;

import org.anon.smart.channels.Route;
import org.anon.smart.channels.MessageReader;
import org.anon.smart.channels.data.CData;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.http.HTTPMessageDScope;
import org.anon.smart.channels.http.HTTPMessageReader;
import org.anon.smart.channels.http.HTTPDataFactory;
import org.anon.smart.channels.distill.Rectifier;

import org.anon.utilities.exception.CtxException;

public class EventDataFactory implements HTTPDataFactory
{
    public EventDataFactory()
    {
    }

    public HTTPMessageDScope createDScope(Route chnl, Object msg, MessageReader rdr)
        throws CtxException
    {
        return new EventDScope(chnl, msg, (HTTPMessageReader)rdr, this);
    }

    public PData createPrimal(DScope scope, CData data)
        throws CtxException
    {
        return new EventPData(scope, data);
    }

}

