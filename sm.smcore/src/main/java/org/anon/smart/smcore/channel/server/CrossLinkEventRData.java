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
 * File:                org.anon.smart.smcore.channel.server.CrossLinkEventRData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A crosslink for event data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.server;

import org.anon.smart.smcore.events.SmartEvent;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkEventRData extends CrossLinker
{
    public CrossLinkEventRData(Object rdata)
    {
        super(rdata);
    }

    public SmartEvent event()
        throws CtxException
    {
        return (SmartEvent)linkMethod("event");
    }

    public String flow()
        throws CtxException
    {
        return (String)linkMethod("flow");
    }

    public void commitResponses()
        throws CtxException
    {
        linkMethod("commitResponses");
    }

    public void addResponse(Object response)
        throws CtxException
    {
        linkMethod("addResponse", response);
    }

    protected Class[] parmTypes(String mthd, Object ... params)
    {
        if (mthd.equals("addResponse"))
            return new Class[] { Object.class };

        return super.parmTypes(mthd, params);
    }

    public void clearResponses()
        throws CtxException
    {
        linkMethod("clearResponses");
    }
}

