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

public class JCSStore implements BrowsableStore {

	private String _name;
	private String _related;
	private StoreConfig _cfg;

	private JCS _cache;
	private Set<String> _groupNames;

	@Override
	public void setup(String name, String related, StoreConfig config)
			throws CtxException {
		try {
			_name = name;
			_related = related;
			_cfg = config;
			_groupNames = new HashSet<String>();

			_cache = JCS.getInstance(_related+"_"+_name);
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
	/**
	 * @param items List of StoreItem objects to write into Cache
	 */
	public void write(List<StoreItem> items) throws CtxException {
		for (StoreItem item : items) {
			try {
				String grp = item.group();
				for (Object key : item.keys()) {
					if (grp != null) {
						_cache.putInGroup(key, item.group(), item.item());
						_groupNames.add(item.group());
					}
					else {
						_cache.put(key,  item.item());
					}
				}
			} catch (CacheException e) {
				except().rt(e,
						new CtxException.Context("JCSStore.write", "Exception"));
			}
		}

	}

	@Override
	public Object read(String group, Object key) throws CtxException {
		if(group != null)
			return _cache.getFromGroup(key, group);
		return _cache.get(key);
	}

	@Override
	public StoreConnection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Repeatable repeatMe(RepeaterVariants parms) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() throws CtxException {
		try {
			_cache.clear();
		} catch (CacheException e) {
			except().rt(e,
					new CtxException.Context("JCSStore.clear", "Exception"));
		}

	}

	@Override
	public void remove(String group, Object key) throws CtxException {
		_cache.remove(key, group);

	}

	@Override
	public Set<Object> keySet(String group) throws CtxException {
		// TODO Auto-generated method stub
		return _cache.getGroupKeys(group);
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

	public void writeRecord(JCSRecord rec) throws CtxException {
		String group = rec.getGroup();
	    Object mod = rec.getModified();
	    List<Object> keys = rec.getKeys();
	    for (Object k : keys) {
			try {
				if (group != null){
					_cache.putInGroup(k, group, mod);
				}
				else
					_cache.put(k, mod);
			} catch (CacheException ex) {
				except().rt(
						ex,
						new CtxException.Context("JCSStore.writeRecord",
								"Exception"));
			}
		}
		
	}

}
