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
 * File:                org.anon.smart.base.dspace.AbstractDSpace
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A space implementation to be used
 *
 * ************************************************************
 * */

package org.anon.smart.base.dspace;

import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.D2Cache;
import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.DataFilter;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public abstract class AbstractDSpace implements TransactDSpace
{
    private D2Cache _cache;
	private D2Cache _fileCache;
    private String _name;
    private DataFilter[] _dataFilters;
    private String _fileType;

    protected AbstractDSpace(String name, String filetype)
        throws CtxException
    {
        this(name, null, filetype);
    }

    protected AbstractDSpace(String name, DataFilter[] filters, String filetype)
        throws CtxException
    {
        _fileType = filetype;
        _cache = D2CacheScheme.getCache(getCacheScheme(), name, getFlags());
        _fileCache = D2CacheScheme.getCache(getFileCacheScheme(), name, getFlags());
        _dataFilters = filters;
    }

    protected abstract D2CacheScheme.scheme getCacheScheme();
    protected abstract D2CacheScheme.scheme getFileCacheScheme();
    protected int getFlags()
        throws CtxException
    {
        int flags = D2CacheScheme.REPLICATION_CACHE | D2CacheScheme.LAYEREDREAD_CACHE |  D2CacheScheme.BROWSABLE_CACHE;
        if (anatomy().jvmEnv().isDistributed())
            flags = flags | D2CacheScheme.DISTRIBUTED_CACHE;
        if ((_fileType != null) && _fileType.equals("disk"))
            flags = flags | D2CacheScheme.DISK_FILESTORE;
        return flags;
    }

    public Reader myReader()
        throws CtxException
    {
        return this.myReader(false);
    }
    
    public Reader myReader(boolean memOnly)
            throws CtxException
    {
        Reader rdr = _cache.myReader(memOnly);
        if (_dataFilters != null)
            rdr.userFilters(_dataFilters);

        return rdr;
    }
    public Reader getBrowsableReader()
        throws CtxException
    {
        Reader rdr =  _cache.getBrowsableReader();
        if (_dataFilters != null)
            rdr.userFilters(_dataFilters);

        return rdr;
    }

	public D2CacheTransaction startTransaction(UUID id, boolean isFSTransaction)
			throws CtxException {
                D2CacheTransaction txn = null;
		if (isFSTransaction)
			txn = _fileCache.startTransaction(id);
		else
			txn = _cache.startTransaction(id);
		
		if (_dataFilters != null)
                    txn.setupFilters(_dataFilters);

        return txn;
	}

    public void cleanup()
        throws CtxException
    {
        _cache.cleanupMemory();
    }

    public String name() { return _name; }
    public D2Cache cacheImpl() { return _cache; }
	
	public D2Cache fsCacheImpl() {
		return _fileCache;
	}
	
	 public Reader myfsReader()
	            throws CtxException{
		 return _fileCache.myReader();
	 }
}

