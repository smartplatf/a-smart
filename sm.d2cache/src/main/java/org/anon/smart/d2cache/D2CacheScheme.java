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
 * File:                org.anon.smart.d2cache.D2CacheScheme
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A scheme based on which the cache is built
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

import org.anon.utilities.exception.CtxException;

public class D2CacheScheme {
	public static enum scheme {
		mem, memind, memstoreind, filestore;
	}

	public static final int BROWSABLE_CACHE = 0x01;
	public static final int REPLICATION_CACHE = 0x02;
	public static final int DISTRIBUTED_CACHE = 0x04;
	public static final int LAYEREDREAD_CACHE = 0x08;

	private D2CacheScheme() {
	}

	protected static D2Cache memoryOnlyCache(String name, int flags,
			D2CacheConfig config) throws CtxException {
		return new MemOnlyCache(name, flags, config);
	}

	protected static D2Cache memIndexedCache(String name, int flags,
			D2CacheConfig config) throws CtxException {
		return new MemIndCache(name, flags, config);
	}

	protected static D2Cache memStoreIndexedCache(String name, int flags,
			D2CacheConfig config) throws CtxException {
		return new MemStoreIndCache(name, flags, config);
	}

	protected static D2Cache fileStoreCache(String name, int flags,
			D2CacheConfig config) throws CtxException {
		return new FileStoreCache(name, flags, config);
	}

	// TODO Have to remove this method
	public static D2Cache getCache(scheme s, String name, int flags)
			throws CtxException {
		D2CacheConfig conf = new BasicD2CacheConfig(null, "hadoop", "2181",
				"hadoop:60000", false);
		return getCache(s, name, flags, conf);
	}

	public static D2Cache getCache(scheme s, String name, int flags,
			D2CacheConfig cacheConfig) throws CtxException {
		D2Cache ret = null;
		switch (s) {
		case mem:
			ret = memoryOnlyCache(name, flags, cacheConfig);
			break;
		case memind:
			ret = memIndexedCache(name, flags, cacheConfig);
			break;
		case filestore:
			ret = fileStoreCache(name, flags, cacheConfig);
			break;
		case memstoreind:
		default:
			ret = memStoreIndexedCache(name, flags, cacheConfig);
			break;
		}

		return ret;
	}

	// this has to get the appropriate reader based on the flags
	// passed during cache creation
	public static Reader readerFor(D2Cache[] cache) throws CtxException {
		return null;
	}
}
