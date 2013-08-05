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

import static org.anon.utilities.objservices.ObjectServiceLocator.anatomy;
import static org.anon.utilities.services.ServiceLocator.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.anon.smart.base.anatomy.SmartModuleContext;
import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.d2cache.BrowsableReader;
import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.d2cache.QueryObject;
import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.deployment.ArtefactType;
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

    public static SpaceFilter[] spaceFilters()
        throws CtxException
    {
        SmartModuleContext ctx = (SmartModuleContext)anatomy().overriddenContext(DSpaceService.class); 
        DSpaceAuthor author = null;
        if (ctx != null)
            author = ctx.spaceAuthor();

        if (author == null)
            author = new DefaultAuthor();

        return author.spaceFilters();
    }

    public static Reader readerFor(DSpace space)
            throws CtxException
    {
            return readerFor(space, false);
    }
    
    public static Reader readerFor(DSpace space, boolean memOnly)
        throws CtxException
    {
        return space.myReader(memOnly);
    }

    public static BrowsableReader browsableReaderFor(DSpace space)
        throws CtxException
    {
        assertion().assertTrue(isBrowsable(space), "Cannot get a browsable reader for nonbrowsable space");
        return (BrowsableReader)space.getBrowsableReader();
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
    
    public static Object lookupIn(DSpace space, Object key, String group, boolean memOnly)
        throws CtxException
    {
        Object ret = null;
        Reader rdr = readerFor(space, memOnly);
        if (rdr != null)
            ret = rdr.lookup(group, key);
        return ret;
    }
    
    public static boolean existsObject(DSpace space, Object key, String group, boolean memOnly)
            throws CtxException
        {
            boolean ret = false;
            Reader rdr = readerFor(space, memOnly);
            if (rdr != null)
                ret = rdr.exists(group, key);
            return ret;
        }

    public static List<Object> searchIn(DSpace space, Map<String, String> query, Class clz)
        throws CtxException
    {
        List<Object> ret = null;
        Reader rdr = readerFor(space);
        QueryObject qo = new QueryObject();
        qo.setResultType(clz);
        //qo.addCondition((String) query);
        qo.addConditions(query);
        if (rdr != null)
        {
        	String group = ArtefactType.artefactTypeFor(FlowConstants.PRIMEDATA).getName(clz);
        	ret = rdr.search(group, qo);
        }

        return ret;
    }
    
    /**
     * 1) gets all keys from different stores(memory, repo) and creates a concurrent List.
     * 2) looks up for each key in concurrent list and removes all keys of the result object from list
     * 
     * TODO batch lookup
     * 
     * @param space
     * @param group
     * @param size
     * @return
     * @throws CtxException
     */
    public static List<Object> listAllIn(DSpace space, String group, int size)
    		throws CtxException
    {
    	List<Object> keyList = null;
    	List<Object> resultSet = new ArrayList<Object>();
    	Reader rdr = readerFor(space);
    	int totSize = 0;
        if (rdr != null)
        {
            keyList = rdr.listAll(group, size); //Got keyList
            //System.out.println("KEY LIST:"+keyList);
            CopyOnWriteArrayList<Object> conKeyList = new CopyOnWriteArrayList<Object>(keyList);
            System.out.println("Total Keys before filtering dups:"+keyList.size());
        
        for(int i = 0; ((totSize < size) && (!conKeyList.isEmpty()));i++)
        {
            Object obj = lookupIn(space, conKeyList.get(0), group, false);
        	List<Object> keysForObject = ((DSpaceObject)obj).smart___keys();
        	conKeyList.removeAll(keysForObject);
        	resultSet.add(obj);
        	totSize++;
        }
        }
        return resultSet;
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
        //StoreItem item = new StoreItem(keys.toArray(new Object[0]), sobj, sobj.smart___objectGroup());
        StoreItem item = new StoreItem(keys.toArray(new Object[0]), null, sobj.smart___objectGroup());
        item.setModified(sobj);
        transaction.add(item);
        Object compare = sobj;
        if (compare instanceof RelatedObject)
        {
            DSpaceObject[] rel = ((RelatedObject)compare).relatedTo();
            for (int r = 0; (rel != null) && (r < rel.length); r++)
                addObject(transaction, rel[r]);
        }
    }
    
    public static void addObject(D2CacheTransaction transaction, DSpaceObject sobj, DSpaceObject modified, DSpaceObject orig)
            throws CtxException
    {
            List<Object> keys = modified.smart___keys();
            StoreItem item = new StoreItem(keys.toArray(new Object[0]), sobj, modified.smart___objectGroup());
            item.setOriginal(orig);
            item.setModified(modified);
            transaction.add(item);
            Object compare = sobj;
            if (compare instanceof RelatedObject)
            {
                DSpaceObject[] rel = ((RelatedObject)compare).relatedTo();
                for (int r = 0; (rel != null) && (r < rel.length); r++)
                    addObject(transaction, rel[r]); //TODO
            }
    }

    public static void addToSpace(TransactDSpace space, DSpaceObject[] sobj)
        throws CtxException
    {
        if ((sobj == null) || (sobj.length <= 0))
            return;

        UUID txnid = UUID.randomUUID();
        D2CacheTransaction transaction = space.startTransaction(txnid,false);
        for (int i = 0; i < sobj.length; i++)
            addObject(transaction, sobj[i]);
        transaction.commit();
    }

    public static D2CacheTransaction startTransaction(TransactDSpace space, UUID txnid)
        throws CtxException
    {
        return space.startTransaction(txnid,false);
    }
    
    public static D2CacheTransaction startFSTransaction(TransactDSpace space, UUID txnid)
            throws CtxException
        {
            return space.startTransaction(txnid,true);
        }
    
    public static void addFSObject(D2CacheTransaction transaction, String srcFl , String group)
            throws CtxException
        {
            StoreItem item = new StoreItem(null, srcFl, group);
            transaction.add(item);
            /*Object compare = sobj;
            if (compare instanceof RelatedObject)
            {
                DSpaceObject[] rel = ((RelatedObject)compare).relatedTo();
                for (int r = 0; (rel != null) && (r < rel.length); r++)
                    addObject(transaction, rel[r]);
            }*/
        }
}

