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
 * File:                org.anon.smart.d2cache.store.index.solr.SolrStore
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 14, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

import java.util.List;

import org.anon.smart.d2cache.store.AbstractStore;
import org.anon.smart.d2cache.store.IndexedStore;
import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;

public class SolrStore extends AbstractStore implements IndexedStore {

	
	public SolrStore(StoreConnection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Repeatable repeatMe(RepeaterVariants parms) throws CtxException {
		return new SolrStore(_connection);
	}

	@Override
	public void setup(String name, StoreConfig config)
			throws CtxException {
		super.setup(name, config);

	}

	@Override
	public void create(String name, Class cls) throws CtxException {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void clear() throws CtxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String group, Object key) throws CtxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void index(List<StoreItem> obj) throws CtxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Object> search(String group, Object query) throws CtxException {
		return getConnection().search(group, query);
	}

}
