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
 * File:                org.anon.smart.d2cache.store.StoreConnection
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A connection to the store
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store;

import java.util.Iterator;
import java.util.UUID;
import java.util.List;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.exception.CtxException;

public interface StoreConnection extends Repeatable
{
    public void connect(StoreConfig cfg)
        throws CtxException;

    public void open(String name)
        throws CtxException;

    public void createMetadata(String name, Class cls)
        throws CtxException;

    public StoreTransaction startTransaction(UUID txn)
        throws CtxException;

    public void close()
        throws CtxException;

    /* Data Access */
    public Object find(String group, Object key)
        throws CtxException;

    public List<Object> search(String group, Object query)
        throws CtxException;
    
    public Iterator<Object> listAll(String group, int size)
    	throws CtxException;
    public void remove(String group, Object key)
    	throws CtxException;
    public boolean exists(String group, Object key)
            throws CtxException;
}

