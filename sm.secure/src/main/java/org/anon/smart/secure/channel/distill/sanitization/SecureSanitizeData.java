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
 * File:                org.anon.smart.secure.channel.distill.sanitization.SecureSanitizeData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A sanitization for security checks
 *
 * ************************************************************
 * */

package org.anon.smart.secure.channel.distill.sanitization;

import java.util.UUID;

import org.anon.smart.channels.data.PData;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.smcore.channel.internal.MessagePData;
import org.anon.smart.smcore.channel.server.EventPData;
import org.anon.smart.smcore.channel.distill.sanitization.SanitizeData;
import org.anon.smart.smcore.channel.distill.sanitization.SearchedData;
import org.anon.smart.secure.session.SessionDirector;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SecureSanitizeData extends SanitizeData
{
    public SecureSanitizeData()
    {
        super();
    }

    @Override
    public void sanitizePData(PData data, SearchedData populate)
        throws CtxException
    {
        super.sanitizePData(data, populate);
        if (data instanceof MessagePData)
            return;

        //search for session.
        EventPData epdata = (EventPData)data;
        SecureSearchedData spopulate = (SecureSearchedData)populate;
        spopulate.setupSearchContext(epdata.eventName());
        if (epdata.sessionId() != null)
        {
            Object session = searchSession(epdata.sessionId(), populate.tenant());
            assertion().assertNotNull(session, "Cannot find session specified. Please login before executing.");
            spopulate.setSession(session);
        }
    }

    private Object searchSession(UUID sessid, SmartTenant tenant)
        throws CtxException
    {
        return SessionDirector.crosslinkSessionIn(sessid.toString(), tenant);
    }
}

