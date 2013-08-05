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
 * File:                org.anon.smart.smcore.data.UploadDataDescrptrED.java
 * Author:              raooll
 * Revision:            1.0
 * Date:                Jun 3, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context of modules used in Smart
 *
 * ************************************************************
 * */
package org.anon.smart.smcore.data;

import static org.anon.smart.base.utils.AnnotationUtils.objectName;
import static org.anon.utilities.objservices.ObjectServiceLocator.threads;
import static org.anon.utilities.services.ServiceLocator.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.atomicity.TruthData;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;
import org.anon.utilities.exception.CtxException;

/**
 * @author raooll
 * 
 */
public class FileItemEd implements EmpiricalData, TruthData,
		AtomicityConstants {

	private FileItem _data;
	private boolean _isNew;

	public FileItemEd(FileItem upld) throws CtxException {
		assertion().assertNotNull(upld,
				"Cannot add a null config to transaction.");
		_data = upld;
		_isNew = true;
	}

	public void setNew() {
		_isNew = true;
	}

	public TruthData truth() throws CtxException {
		return this;
	}

	public boolean isErrorData() {
		return false;
	}

	public String dataType() throws CtxException {
		return SMARTDATA;
	}

	public List<String> tags() throws CtxException {
		List<String> lst = new ArrayList<String>();
		lst.add(objectName(_data));
		return lst;
	}

	public UUID truthID() {
		return null;
	}

	public boolean start(UUID txnid) throws CtxException {
		TransitionContext ctx = (TransitionContext) threads().threadContext();
		ctx.transaction().startTransaction(_data);
		return true;
	}

	public boolean simulate(UUID txnid, EmpiricalData edata)
			throws CtxException {
		return true;
	}

	public boolean accept(UUID txnid, EmpiricalData edata) throws CtxException {
		TransitionContext ctx = (TransitionContext) threads().threadContext();
		assertion().assertNotNull(ctx,
				"The object is not in an transition context to be accepted.");
		ctx.transaction().addToTransaction(_data);
		return true;
	}

	public void discard(UUID txnid, EmpiricalData edata) throws CtxException {
		return;
	}

	public boolean end(UUID txnid) throws CtxException {
		return true;
	}

	public void recordEmpiricalData(UUID txnid, EmpiricalData edata)
			throws CtxException {
		// does nothing. There is no way the same truth config is used across.
		// it just creates
		// new config objects.
	}
	
}
