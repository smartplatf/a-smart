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
 * File:                org.anon.smart.base.dspace.BrowsableTransactDSpace
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A dspace that has a browse flag turned on
 *
 * ************************************************************
 * */

package org.anon.smart.base.dspace;

import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.DataFilter;

import org.anon.utilities.exception.CtxException;

public class BrowsableTransactDSpace extends TransactDSpaceImpl
{
    public BrowsableTransactDSpace(String name)
        throws CtxException
    {
        super(name);
    }

    public BrowsableTransactDSpace(String name, DataFilter[] filters)
        throws CtxException
    {
        super(name, filters);
    }

    @Override
    protected int getFlags()
        throws CtxException
    {
        int flags = super.getFlags();
        return flags | D2CacheScheme.BROWSABLE_CACHE;
    }
}

