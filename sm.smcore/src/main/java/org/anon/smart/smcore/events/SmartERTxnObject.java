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
 * File:                org.anon.smart.smcore.events.SmartERTxnObject
 * Author:              rsankar
 * Revision:            1.0
 * Date:                27-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A transaction object for event responses
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.events;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.atomicity.TruthData;
import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.inbuilt.responses.ErrorResponse;
import org.anon.smart.smcore.channel.server.CrossLinkEventRData;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SmartERTxnObject implements EmpiricalData, TruthData, AtomicityConstants
{
    private SmartEventResponse _response;
    private boolean _isNew;
    private boolean _flagToSend;

    public SmartERTxnObject(SmartEventResponse resp)
        throws CtxException
    {
        assertion().assertNotNull(resp, "Cannot add a null response to transaction.");
        _response = resp;
        _isNew = true;
        _flagToSend = false;
    }

    public void setNew()
    {
        _isNew = true;
    }

    public TruthData truth()
        throws CtxException
    {
        return this;
    }

    public boolean isErrorData()
    {
        return (_response instanceof ErrorResponse);
    }

    public String dataType()
        throws CtxException
    {
        return RESPONSE;
    }

    public List<String> tags()
        throws CtxException
    {
        List<String> lst = new ArrayList<String>();
        lst.add(objectName(_response));
        return lst;
    }

    public UUID truthID()
    {
        return _response.smart___eventID();
    }

    public boolean start(UUID txnid)
        throws CtxException
    {
        return true;
    }

    public boolean simulate(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        return true;
    }

    public boolean accept(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        CrossLinkEventRData rdata = ctx.rdata();
        rdata.addResponse(_response);
        _flagToSend = true;
        return true;
    }

    public void discard(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        if (isErrorData())
        {
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            CrossLinkEventRData rdata = ctx.rdata();
            rdata.addResponse(_response);
            _flagToSend = true;
        }
        else
            _flagToSend = false;
    }

    public boolean end(UUID txnid)
        throws CtxException
    {
        if (_flagToSend)
        {
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            CrossLinkEventRData rdata = ctx.rdata();
            rdata.commitResponses();
            _flagToSend = false;
        }

        return true;
    }

    public void recordEmpiricalData(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        //does nothing
    }
}

