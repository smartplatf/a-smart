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
 * File:                org.anon.smart.smcore.uploadchannel.distill.CrossLinkFSReader.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 10, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.uploadchannel.crosslinks;

import java.io.InputStream;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 * 
 */
public class CrossLinkFSReader extends CrossLinker {

	public CrossLinkFSReader(Object link) {
		super(link);
		// TODO Auto-generated constructor stub
	}

	public InputStream getFileAsStream(String flName, String group,
			ClassLoader cl) throws CtxException {
		return (InputStream) linkMethod("getFileAsStream", flName, group, cl);
	}
}
