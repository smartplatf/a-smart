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
 * File:                org.anon.smart.d2cache.segment.DefualtReader
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.segment;

import java.util.List;

import org.anon.smart.d2cache.Reader;
import org.anon.smart.d2cache.DataFilter;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.utilities.exception.CtxException;
import static org.anon.utilities.services.ServiceLocator.*;


public class DefaultReader implements Reader {

	protected Store _store;
	protected StoreConfig _config;
    protected DataFilter[] _filters;
	
	public DefaultReader(Store store, StoreConfig cfg)
	{
		_store = store;
		_config = cfg;
	}

    public void userFilters(DataFilter[] filter)
    {
        _filters = filter;
    }

    private boolean isFilter(Object obj, boolean except)
        throws CtxException
    {
        boolean ret = true;
        for (int i = 0; (ret) && (_filters != null) && (i < _filters.length); i++)
            ret = _filters[i].filterObject(obj, DataFilter.dataaction.read, except);

        return ret;
    }

	@Override
	public Object lookup(String group, Object key) throws CtxException {
		Object ret = null;
		ret = _store.getConnection().find(group, key);
        if ((ret != null) && (!isFilter(ret, true)))
            ret = null;
		return ret;
	}

	@Override
	public List<Object> search(String group, Object query) throws CtxException {
		except().te(null, "Search is NOT suppoerted in Mem Only cache schema");
		return null;
	}
	
	public List<Object> listAll(String group, int size) throws CtxException {
		return null;
	}

    @Override
    public boolean exists(String group, Object key) throws CtxException
    {
        boolean ret = false;
        ret = _store.getConnection().exists(group, key);
        
        return ret;
    }

}
