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
 * File:                org.anon.smart.d2cache.FileStoreCache.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 4, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.d2cache;

import java.util.UUID;

import org.anon.smart.d2cache.segment.CSegment;
import org.anon.smart.d2cache.segment.FileStoreSegment;
import org.anon.smart.d2cache.segment.ReplicationWriter;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.fileStore.FileStoreReader;
import org.anon.smart.d2cache.store.fileStore.FileStoreTransactionImpl;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

/**
 * @author raooll
 * 
 */
public class FileStoreCache extends AbstractD2Cache {

	public FileStoreCache(String name, int flags) throws CtxException {
		this(name, flags, null);
	}

	public FileStoreCache(String name, int flags, D2CacheConfig config)
			throws CtxException {
		super(name, flags, config);
		_segments = new CSegment[1];
		_segments[0] = createFileSegment();
	}

	protected CSegment createFileSegment() throws CtxException {
		// Check and create the appropriate file segment. Right now create
		// hadoopFileSegment directly

		CSegment hSegment = new FileStoreSegment();
		boolean isDevel = convert().stringToBoolean(System.getProperty("Smart.Development.Mode", "false")); 
		if(isDevel)
		{	
			hSegment.setupSegment(_name, _config.getDiskStoreConfig());
		}
        else
		{
            if ((_flags & D2CacheScheme.DISK_FILESTORE) == D2CacheScheme.DISK_FILESTORE)
                hSegment.setupSegment(_name, _config.getDiskStoreConfig());
            else
                hSegment.setupSegment(_name, _config.getHadoopStoreConfig());
		}
		
		return hSegment;

	}

	@Override
	public D2CacheTransaction startTransaction(UUID txnid) throws CtxException {
		StoreConnection[] connections = new StoreConnection[_segments.length];
		int i = 0;
		for (CSegment seg : _segments) {
			connections[i++] = seg.getStore().getConnection();
		}
		return new FileStoreTransactionImpl(txnid, connections,
				new ReplicationWriter());
	}

    @Override
    public Reader myReader() throws CtxException {
        return new FileStoreReader(_segments[0].getStore(), null);
    }

}
