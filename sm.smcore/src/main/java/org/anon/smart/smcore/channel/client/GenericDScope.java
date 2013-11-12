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
 * File:                org.anon.smart.smcore.channel.client.GenericDScope
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data scope that is generic for all client connections
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.client;

import org.anon.smart.channels.Route;
import org.anon.smart.channels.http.HTTPMessageReader;
import org.anon.smart.channels.http.HTTPDataFactory;
import org.anon.smart.channels.http.HTTPMessageDScope;

import org.anon.utilities.exception.CtxException;


public class GenericDScope extends HTTPMessageDScope
{
    public GenericDScope(Route r, Object msg, HTTPMessageReader rdr, HTTPDataFactory fact)
        throws CtxException
    {
        super(r, msg, rdr, fact);
    }

    protected void handlePath(String path)
        throws CtxException
    {
    }

    public String flow()
    {
        return null;
    }

    public Object eventLegend(ClassLoader ldr)
    {
        return null;
    }
}

