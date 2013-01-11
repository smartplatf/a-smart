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
 * File:                org.anon.smart.d2cache.store.Store
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A storage interface to be implemented by anyone wanting to be a store
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store;

import java.util.List;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.exception.CtxException;

public interface Store extends Repeatable
{
    public void setup(String name, String related, StoreConfig config)
        throws CtxException;

    public void create(String name, Class cls)
        throws CtxException;

    public void write(List<StoreItem> items)
        throws CtxException;

    public Object read(String group, Object key)
        throws CtxException;

    public StoreConnection getConnection();
}

