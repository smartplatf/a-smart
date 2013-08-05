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
 * File:                org.anon.smart.d2cache.store.fileStore.FileItem.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                May 30, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.data;

import java.util.ArrayList;
import java.util.List;

import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 * 
 */
public class FileItem implements DSpaceObject {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FileItem [_srcFile=" + _srcFile + ", _flowName=" + _flowName
				+ ", ___smartKeys=" + ___smartKeys + ", ___smartObjectGroup="
				+ ___smartObjectGroup + "]";
	}

	private String _srcFile;
	private String _flowName;
	private List<Object> ___smartKeys;
	private String ___smartObjectGroup;

	public FileItem(String srcFlName,String flw) {
		_srcFile = srcFlName;
		_flowName = flw;
		___smartKeys = new ArrayList<Object>();
		___smartKeys.add(_flowName);

	}

	public String getSrcFile(){
		return _srcFile;
	}
	@Override
	public void smart___initOnLoad() throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> smart___keys() throws CtxException {
		// TODO Auto-generated method stub
		return ___smartKeys;
	}

	@Override
	public String smart___objectGroup() throws CtxException {
		// TODO Auto-generated method stub
		return ___smartObjectGroup;
	}

	/**
	 * @param ___smartKeys the ___smartKeys to set
	 */
	public void set___smartKeys(List<Object> ___smartKeys) {
		this.___smartKeys = ___smartKeys;
	}

	/**
	 * @param ___smartObjectGroup the ___smartObjectGroup to set
	 */
	public void set___smartObjectGroup(String ___smartObjectGroup) {
		this.___smartObjectGroup = ___smartObjectGroup;
	}

	public String getFlowName() {
		// TODO Auto-generated method stub
		return _flowName;
	}

}
