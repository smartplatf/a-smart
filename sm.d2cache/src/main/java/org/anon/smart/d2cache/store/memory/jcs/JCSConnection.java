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
 * File:                org.anon.smart.d2cache.store.memory.jcs.JCSConnection
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A connection object for jcs cache
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.memory.jcs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.jcs.JCS;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;

import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.ListParams;

import static org.anon.utilities.objservices.ObjectServiceLocator.convert;
import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.sleepycat.je.util.DbSpace;

public class JCSConnection implements StoreConnection
{
    private JCS _cache;
    private String _name;

    public JCSConnection()
    {
    }

    public void open(String name)
        throws CtxException
    {
        try
        {
            _cache = JCS.getInstance(name);
            _name = name;
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("JCSConnection.connect", "Exception"));
        }
    }

    public void connect(StoreConfig cfg)
        throws CtxException
    {
    }

    public void createMetadata(String name, Class cls)
        throws CtxException
    {
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new JCSConnection();
    }

    public JCS cache() { return _cache; }

   

    public Object find(String group, Object key)
        throws CtxException
    {
        assertion().assertNotNull(_cache, "JCS region is Null");
        Object ret = null;
        try
        {
            if (group != null)
                ret = _cache.getFromGroup(key, group);
            else
                ret = _cache.get(key);
            //System.out.println("FOUND: grp:"+group+"::: key:"+key+ ":---->" + ret);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("JCSConnection.find", "Exception"));
        }

        return ret;
    }

    public StoreTransaction startTransaction(UUID txn)
        throws CtxException
    {
        return new JCSTransaction(txn, this);
    }

    public void close()
        throws CtxException
    {
        try
        {
            if (_cache != null)
            {
                _cache.clear();
                CompositeCacheManager.getInstance().freeCache(_name);
                //we are currently just removing the element event q.
                CompositeCache.elementEventQ.destroy(); //so that the reference to the classloader is released
                _cache = null;
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("JCSConnection.clear", "Exception"));
        }
    }

	
    @Override
	public void remove(String group, Object key) throws CtxException {
		_cache.remove(key, group);

	}

	@Override
	public List<Object> search(String group, Object query, int size, int pn, int ps, String sby, boolean asc) throws CtxException {
		return null; //Does not support
	}

	@Override
	public Iterator<Object> list(ListParams parms) throws CtxException {

        boolean noPers = convert().stringToBoolean(System.getProperty("Smart.Development.Mode", "false"));
        if(noPers)
        {
            String group = parms.getGroup();
            long size = parms.getSize();
            assertion().assertNotNull(_cache, "JCS region is Null");
                System.out.println("Listing:"+group+":::"+size);
                List<Object> ret = new ArrayList<Object>();
                try
                {
                    if (group != null)
                    {
                         Set<Object> keySet = _cache.getGroupKeys(group);
                         return keySet.iterator();
                    }
                }
                catch (Exception e)
                {
                     except().rt(e, new CtxException.Context("JCSConnection.find", "Exception"));
                }
        }
        return null;
	}

    @Override
    public boolean exists(String group, Object key) 
        throws CtxException
    {
        assertion().assertNotNull(_cache, "JCS region is Null");
        Object obj = null;
        try
        {
            if (group != null)
                obj = _cache.getFromGroup(key, group);
            else
                obj = _cache.get(key);
            //System.out.println("FOUND: grp:"+group+"::: key:"+key+ ":---->" + ret);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("JCSConnection.find", "Exception"));
        }

        return (obj != null) ? true:false;
    }

    @Override
    public Iterator<Object> getListings(String group, String sortBy,
        int listingsPerPage, int pageNum) 
        throws CtxException
    {
        boolean noPers = convert().stringToBoolean(System.getProperty("Smart.Development.Mode", "false"));
        if(noPers)
        {
            assertion().assertNotNull(_cache, "JCS region is Null");
            List<Object> ret = new ArrayList<Object>();
            try
            {
                if (group != null)
                {
                    Set<Object> keySet = _cache.getGroupKeys(group);
                    int sInd = (pageNum -1)*listingsPerPage;
                    int eInd = sInd+(listingsPerPage - 1);
                    if(sInd >= keySet.size())
                    {
                        return null;
                    }
                    if((eInd+1) > keySet.size())
                    {
                        eInd = keySet.size() -1;
                    }
                    List<Object> keyList = new ArrayList<Object>(keySet);
                    //System.out.println("Returning from :"+sInd+ " to :"+eInd +" from cache out of :"+keySet.size());
                    return keyList.subList(sInd, eInd+1).iterator();
                
                }
            
            
            }
            catch (Exception e)
            {
                except().rt(e, new CtxException.Context("JCSConnection.find", "Exception"));
            }
        }
        return null;
    }
}
