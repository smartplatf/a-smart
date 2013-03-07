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
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 7, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.memory.jcs;

import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;

public class JCSConnection implements StoreConnection {

	private JCSStore _store;
	
	public JCSConnection(JCSStore store)
	{
		_store = store;
	}
	@Override
	public Repeatable repeatMe(RepeaterVariants parms) throws CtxException {
		return new JCSConnection(_store);
	}

	@Override
	public void connect(StoreConfig cfg) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(String related, String name) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createMetadata(String name, Class cls) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public StoreTransaction startTransaction(UUID txn) throws CtxException {
		return new JCSTransaction(txn, this);
	}

	@Override
	public void close() throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object find(String group, Object key) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> search(String group, String query) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public JCSStore store() { return _store; }
	
	

}