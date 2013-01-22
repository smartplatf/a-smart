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
 * File:                org.anon.smart.base.dspace.DSpaceService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                16-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A service that provides functions to access dspace related operations
 *
 * ************************************************************
 * */

package org.anon.smart.base.dspace;

import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.BrowsableReader;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.base.anatomy.SmartModuleContext;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class DSpaceService
{
    private DSpaceService()
    {
    }

    public static DSpace spaceFor(String name, boolean browse)
        throws CtxException
    {
        DSpace ret = null;
        SmartModuleContext ctx = (SmartModuleContext)anatomy().overriddenContext(DSpaceService.class); 
        DSpaceAuthor author = null;
        if (ctx != null)
            author = ctx.spaceAuthor();

        if (author == null)
            author = new DefaultAuthor();

        if (browse)
            ret = author.browsableSpaceFor(name);
        else
            ret = author.newSpaceFor(name);

        return ret;
    }

    public static Reader readerFor(DSpace space)
        throws CtxException
    {
        return space.myReader();
    }

    public static BrowsableReader browsableReaderFor(DSpace space)
        throws CtxException
    {
        assertion().assertTrue(isBrowsable(space), "Cannot get a browsable reader for nonbrowsable space");
        return (BrowsableReader)space.myReader();
    }

    public static Reader readerFor(DSpace[] space)
        throws CtxException
    {
        D2Cache[] incache = new D2Cache[space.length];
        for (int i = 0; i < space.length; i++)
            incache[i] = space[i].cacheImpl();

        return D2CacheScheme.readerFor(incache);
    }

    public static Object lookupIn(DSpace space, Object key, String group)
        throws CtxException
    {
        Object ret = null;
        Reader rdr = readerFor(space);
        if (rdr != null)
            ret = rdr.lookup(group, key);
        return ret;
    }

    public static List<Object> searchIn(DSpace space, Object query, String group)
        throws CtxException
    {
        List<Object> ret = null;
        Reader rdr = readerFor(space);
        if (rdr != null)
            ret = rdr.search(group, query);

        return ret;
    }

    public static boolean isBrowsable(DSpace space)
        throws CtxException
    {
        boolean ret = (space instanceof BrowsableTransactDSpace);
        ret = (ret && (space.cacheImpl().isEnabled(D2CacheScheme.BROWSABLE_CACHE)));
        return ret;
    }

    public static void addObject(D2CacheTransaction transaction, DSpaceObject sobj)
        throws CtxException
    {
        List<Object> keys = sobj.smart___keys();
        StoreItem item = new StoreItem(keys.toArray(new Object[0]), sobj, sobj.smart___objectGroup());
        transaction.add(item);
    }

    public static void addToSpace(TransactDSpace space, DSpaceObject[] sobj)
        throws CtxException
    {
        if ((sobj == null) || (sobj.length <= 0))
            return;

        UUID txnid = UUID.randomUUID();
        D2CacheTransaction transaction = space.startTransaction(txnid);
        for (int i = 0; i < sobj.length; i++)
            addObject(transaction, sobj[i]);
        transaction.commit();
    }
}

