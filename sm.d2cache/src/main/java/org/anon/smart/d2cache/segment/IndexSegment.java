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
 * File:                org.anon.smart.d2cache.segment.IndexSegment
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

package org.anon.smart.d2cache.segment;

import java.util.List;

import org.anon.smart.d2cache.store.Store;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.smart.d2cache.store.index.solr.SolrConnection;
import org.anon.smart.d2cache.store.index.solr.SolrStore;
import org.anon.utilities.exception.CtxException;

public class IndexSegment implements CSegment {

	private Store _store;
	@Override
	public Store getStore() {
		return _store;
	}

	@Override
	public void setupSegment(String name, StoreConfig cfg)
			throws CtxException {
		_store = new SolrStore(SolrConnection.getConnection());
		_store.setup(name, cfg);
	}

	@Override
	public void storeItem(StoreItem item) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeItem(List<StoreItem> items) throws CtxException {
		// TODO Auto-generated method stub

	}

    public void cleanup()
        throws CtxException
    {
        if (_store != null)
            _store.close();

        _store = null;
    }

}
