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
 * File:                org.anon.smart.secure.dspace.SecureSpaceAuthor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A secure space author for spaces
 *
 * ************************************************************
 * */

package org.anon.smart.secure.dspace;

import org.anon.smart.d2cache.DataFilter;
import org.anon.smart.base.dspace.DSpace;
import org.anon.smart.base.dspace.DSpaceAuthor;
import org.anon.smart.base.dspace.SpaceFilter;
import org.anon.smart.base.dspace.TransactDSpaceImpl;
import org.anon.smart.base.dspace.BrowsableTransactDSpace;

import org.anon.utilities.exception.CtxException;

public class SecureSpaceAuthor implements DSpaceAuthor
{
    public SecureSpaceAuthor()
    {
    }

    public DSpace newSpaceFor(String name, String fileType)
        throws CtxException
    {
        SecureDataFilter filter = new SecureDataFilter();
        DSpace space = new TransactDSpaceImpl(name, new DataFilter[] { filter }, fileType);
        filter.setupSpace(space);
        return space;
    }

    public DSpace browsableSpaceFor(String name, String fileType)
        throws CtxException
    {
        SecureDataFilter filter = new SecureDataFilter();
        DSpace space = new BrowsableTransactDSpace(name, new DataFilter[] { filter }, fileType);
        filter.setupSpace(space);
        return space;
    }

    public SpaceFilter[] spaceFilters()
        throws CtxException
    {
        return new SpaceFilter[] { new SecureSpaceFilter() };
    }
}

