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
 * File:                org.anon.smart.d2cache.store.StoreTransaction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A transaction for a store which is created and can be completed when required
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store;

import java.util.UUID;
import java.util.Collection;

import org.anon.utilities.exception.CtxException;

public interface StoreTransaction
{
    public UUID getTransactionId();
    public Collection<StoreRecord> allRecords();
    public Collection<StoreRecord> getRecords(String group);
    public StoreRecord recordFor(Object primaryKey);
    public StoreRecord addRecord(String group, Object primarykey, Object curr, Object orig)
        throws CtxException;
    public StoreRecord addRecord(String group, Object primarykey, Object curr, Object orig, Object relatedKey)
            throws CtxException;
    public void commit()
        throws CtxException;
    public void rollback()
        throws CtxException;
    public boolean waitToComplete();
}

