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
 * File:                org.anon.smart.d2cache.store.StoreItem
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An item to be stored in the store
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store;

import org.anon.utilities.exception.CtxException;
import org.anon.utilities.reflect.DirtyFieldTraversal;

public class StoreItem
{
    private String _group;
    private Object[] _keys;
    private Object _truth;
    private Object _modified;
    private boolean _isNew;
    private String _storeIn;
    

	private Object _original;

    public StoreItem(Object[] keys, Object obj)
    {
        _keys = keys;
        _truth = obj;
        _group = "";
    }

    public StoreItem(Object[] keys, Object obj, String group)
    {
        _keys = keys;
        _truth = obj;
        _group = group;
    }

    public Object[] keys()
    {
        return _keys;
    }

    public void setNew(boolean n)
    {
        _isNew = n;
    }

    public boolean isNew()
    {
        return _isNew;
    }

    public Object getTruth()
    {
        return _truth;
    }

    public String group()
    {
        return _group;
    }
    
    public Object getModified() {
		return _modified;
	}

	public void setModified(Object modified) {
		this._modified = modified;
	}

	public Object getOriginal() {
		return _original;
	}

	public void setOriginal(Object original) {
		this._original = original;
	}
	
	public boolean mergeChanges()
	throws CtxException
	{
		boolean changed = true;
		
		
		return changed;
	}

    public String getStoreIn() { return _storeIn; }
    public void setStoreIn(String store)
    {
        _storeIn = store;
    }
}

