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
 * File:                org.anon.smart.base.dspace.HadoopFileDSpaceImpl.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 2, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.base.dspace;

import org.anon.smart.d2cache.D2CacheScheme;
import org.anon.smart.d2cache.D2CacheScheme.scheme;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 *
 */
public class FileDSpaceImpl extends TransactDSpaceImpl {

	public FileDSpaceImpl(String name, String file) throws CtxException {
		super(name, file);
		//Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.anon.smart.base.dspace.AbstractDSpace#getScheme()
	 */
	public D2CacheScheme.scheme[] getScheme() {
		// TODO Auto-generated method stub
		return new D2CacheScheme.scheme[] {D2CacheScheme.scheme.filestore};
	}

}
