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
 * File:                org.anon.smart.d2cache.store.AbstractStoreRecord
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An abstract record for inserting
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store;

public abstract class AbstractStoreRecord implements StoreRecord
{
    protected String _group;
    protected Object _primaryKey;
    protected Object _currentObject;
    protected Object _orig;

    public AbstractStoreRecord(String group, Object primary, Object curr, Object orig)
    {
        _group = group;
        _primaryKey = primary;
        _currentObject = curr;
        _orig = orig;
    }

    public Object getRowId()
    {
        return _primaryKey;
    }

    public String getGroup()
    {
        return _group;
    }
    
    public Object getOriginal()
    {
    	return _orig;
    }
    
    public Object getCurrent()
    {
    	return _currentObject;
    }
    
    public void setGroup(String grp)
    {
    	_group = grp;
    }
    
}

