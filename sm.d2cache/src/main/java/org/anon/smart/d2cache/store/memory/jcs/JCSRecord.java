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
 * File:                org.anon.smart.d2cache.store.memory.jcs.JCSRecord
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A record for jcs cache
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.memory.jcs;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.d2cache.store.AbstractStoreRecord;
import org.anon.smart.d2cache.annot.CacheKeyAnnotate;
import org.anon.smart.d2cache.annot.PrimeKeyAnnotate;

import org.anon.utilities.reflect.DFDataContext;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.exception.CtxException;

public class JCSRecord extends AbstractStoreRecord
{
    private List<Object> _keys;

    public JCSRecord(String group, Object primarykey, Object curr, Object orig)
    {
        super(group, primarykey, curr, orig);
        _keys = new ArrayList<Object>();
        _keys.add(getRowId());
	}

    public void append(DataContext ctx, boolean update)
        throws CtxException
    {
        if (ctx.field() != null)
        {
        	ctx.mergeToCoTraverse();
            if ((ctx.field().isAnnotationPresent(CacheKeyAnnotate.class) 
                    || ctx.field().isAnnotationPresent(PrimeKeyAnnotate.class)) &&
                (ctx.fieldVal() != null))
            {
                _keys.add(ctx.fieldVal());
            }
        }
    }

     
    List<Object> getKeys() { return _keys; }
    Object getModified() { return _currentObject; }
}

