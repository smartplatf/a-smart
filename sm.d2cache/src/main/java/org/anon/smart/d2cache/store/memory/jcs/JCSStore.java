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
 * File:                org.anon.smart.d2cache.store.memory.jcs.JCSStore
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

package org.anon.smart.d2cache.store.memory.jcs;

import static org.anon.utilities.services.ServiceLocator.except;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.anon.smart.d2cache.store.AbstractStore;
import org.anon.smart.d2cache.store.BrowsableStore;
import org.anon.smart.d2cache.store.MemoryStore;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreItem;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

public class JCSStore extends AbstractStore implements BrowsableStore {

	public JCSStore(StoreConnection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	
	private Set<String> _groupNames;

	@Override
	public void setup(String name, StoreConfig config)
			throws CtxException {
		try {
			super.setup(name, config);
			_groupNames = new HashSet<String>();

		} catch (Exception e) {
			except().rt(e,
					new CtxException.Context("JCSStore.setup", "Exception"));
		}

	}

	@Override
	public void create(String name, Class cls) throws CtxException {
		// TODO Auto-generated method stub

	}

	

	
	@Override
	public Repeatable repeatMe(RepeaterVariants parms) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() throws CtxException {

	}

	@Override
	public void remove(String group, Object key) throws CtxException {
		((JCSConnection)_connection).cache().remove(key, group);

	}

	@Override
	public Set<Object> keySet(String group) throws CtxException {
		// TODO Auto-generated method stub
		return ((JCSConnection)_connection).cache().getGroupKeys(group);
	}

	@Override
	public Map<String, Set<Object>> keySet() throws CtxException {
		//TODO implements in  JCS 2.0, but not released 
		//Set<String> groupNames = _cache.getGroupNames(); 
		Map<String, Set<Object>> keySetMap = new HashMap<String, Set<Object>>();
		for (String groupName : _groupNames) {
			keySetMap.put(groupName, keySet(groupName));
		}
		return keySetMap;
	}

	

}
