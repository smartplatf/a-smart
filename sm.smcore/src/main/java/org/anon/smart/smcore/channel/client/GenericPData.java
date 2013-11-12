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
 * File:                org.anon.smart.smcore.channel.client.GenericPData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data object that is generic to all client connections
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.client;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.CData;
import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.http.HTTPRequestPData;

import org.anon.utilities.exception.CtxException;

public class GenericPData extends HTTPRequestPData
{
    public GenericPData(DScope dscope, CData data)
        throws CtxException
    {
        super(dscope, data);
    }
}

