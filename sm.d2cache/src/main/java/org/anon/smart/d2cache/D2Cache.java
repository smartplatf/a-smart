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
 * File:                org.anon.smart.d2cache.D2Cache
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A durable cache interface through which access to this cache is given
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

import java.util.UUID;
import java.util.List;
import java.util.Set;

import org.anon.smart.d2cache.store.StoreItem;

import org.anon.utilities.exception.CtxException;

public interface D2Cache
{
    public D2CacheTransaction startTransaction(UUID txnid)
        throws CtxException;

    public Reader myReader()
            throws CtxException;
    
    public Reader myReader(boolean memOnly)
        throws CtxException;

    public Reader getBrowsableReader()
            throws CtxException;
    
    public void cleanupMemory()
        throws CtxException;

    public boolean isEnabled(int flags);
}

