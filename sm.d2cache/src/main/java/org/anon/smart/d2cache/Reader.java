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
 * File:                org.anon.smart.d2cache.Reader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                16-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A reader for the cache
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

import java.util.Set;
import java.util.List;

import org.anon.utilities.exception.CtxException;

public interface Reader
{
    public void userFilters(DataFilter[] filter);

    public Object lookup(String group, Object key)
        throws CtxException;

    public List<Object> search(String group, Object query)
        throws CtxException;
    
    public List<Object> listAll(String group, int size)
    	throws CtxException;
    
    public boolean exists(String group, Object key)
            throws CtxException;
}

