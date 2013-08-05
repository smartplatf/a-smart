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
 * File:                org.anon.smart.channels.data.DScope
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A scope for the data transmitted via channels
 *
 * ************************************************************
 * */

package org.anon.smart.channels.data;

import java.util.UUID;

import org.anon.utilities.exception.CtxException;

public interface DScope
{
    public Responder responder();

    public void processingData(UUID did)
        throws CtxException;

    public void processedData()
        throws CtxException;

    public boolean processedAll()
        throws CtxException;

    public Source source();

    public UUID channelID();

    public UUID requestID();

    public PData primary();

    public void transmit(PData[] resp)
        throws CtxException;

    public void close()
        throws CtxException;
    
    public Object eventLegend(ClassLoader ldr)
            throws CtxException;
    
    public String origin();
    public String flow();
}

