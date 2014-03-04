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
 * File:                org.anon.smart.smcore.events.internal.SmartMessageObject
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                May 7, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.events.internal;

import static org.anon.smart.base.utils.AnnotationUtils.objectName;
import static org.anon.utilities.services.ServiceLocator.assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.atomicity.TruthData;
import org.anon.smart.smcore.channel.internal.MessageConfig;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.events.SmartEventResponse;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;
import org.anon.smart.smcore.anatomy.CoreContext;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

public class SmartMessageObject implements EmpiricalData, TruthData, AtomicityConstants {

	private SmartEvent _message;
    private boolean _isNew;
    private boolean _flagToSend;
    
    private MessageConfig _mc;

    public SmartMessageObject(SmartEvent event)
        throws CtxException
    {
        assertion().assertNotNull(event, "Cannot add a null message to transaction.");
        _message = event;
        _isNew = true;
        _flagToSend = false;
    }
	@Override
	public UUID truthID() {
		return _message.smart___eventID();
	}

	@Override
	public boolean start(UUID txnid) throws CtxException {
		return true;
	}

	@Override
	public boolean simulate(UUID txnid, EmpiricalData edata)
			throws CtxException {
		return true;
	}

	@Override
	public boolean accept(UUID txnid, EmpiricalData edata) throws CtxException {
		System.out.println("------------------------- ACCEPTING MESSAGE ---------");
		//_mc = new MessageConfig(_message);
        CoreContext ctx = (CoreContext)anatomy().overriddenContext(this.getClass());
        _mc = (MessageConfig)ctx.getMessageConfig(_message);
		System.out.println("------------------------- ACCEPTED MESSAGE ---------"+_mc+"::"+ this);
		
		return (_mc != null);
	}

	@Override
	public void discard(UUID txnid, EmpiricalData edata) throws CtxException {
		// TODO Auto-generated method stub
        System.out.println("Discarded??");
		
	}

	@Override
	public boolean end(UUID txnid) throws CtxException {
		System.out.println("end in mesage obj:"+_mc+"::::"+this);
		if(_mc != null)
			_mc.postMessage();
		else
			System.out.println("---------------------WHY _MC IS NULLL? WHY WHY WHY");
		return (_mc != null);
	}

	@Override
	public void recordEmpiricalData(UUID txnid, EmpiricalData edata)
			throws CtxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNew() {
		_isNew = true;
		
	}

	@Override
	public TruthData truth() throws CtxException {
		return this;
	}

	@Override
	public boolean isErrorData() {
		return false;
	}

	@Override
	public String dataType() throws CtxException {
		return MESSAGE;
	}

	@Override
	public List<String> tags() throws CtxException {
		List<String> lst = new ArrayList<String>();
        lst.add(objectName(_message));
        return lst;
	}

}
