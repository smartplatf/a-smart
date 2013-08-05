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
 * File:                org.anon.smart.smcore.transition.atomicity.TAtomicity
 * Author:              rsankar
 * Revision:            1.0
 * Date:                27-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An atomicity for transitions
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.atomicity;

import org.anon.smart.atomicity.Atomicity;
import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.data.SmartDataTruth;
import org.anon.smart.smcore.data.ConfigData;
import org.anon.smart.smcore.data.ConfigDataED;
import org.anon.smart.smcore.data.FileItemEd;
import org.anon.smart.smcore.data.FileItem;
import org.anon.smart.smcore.data.SmartFileObject;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.events.SmartEventResponse;
import org.anon.smart.smcore.events.SmartERTxnObject;
import org.anon.smart.smcore.events.internal.SmartMessageObject;
import org.anon.smart.smcore.transition.TransitionContext;

import org.anon.utilities.exception.CtxException;

public class TAtomicity extends Atomicity implements AtomicityConstants
{
    private TransitionContext _context;

    public TAtomicity(TransitionContext ctx)
        throws CtxException
    {
        super();
        addDataType(SMARTDATA);
        addDataType(RESPONSE);
        addDataType(MESSAGE);
        addDataType(CONFIG);
        _context = ctx;
        SmartDataED ed = includeData(_context.primeData());
        ctx.setupPrimeED(ed);
        includeFrom(_context.event());
    }

    private void includeFrom(SmartEvent evt)
        throws CtxException
    {
        //need to traverse object here.
        //TODO:
    }

    protected void starting(boolean outcome)
        throws CtxException
    {
        if (outcome)
        {
            String toState = _context.toState();
            SmartDataED data = (SmartDataED)dataFor(SMARTDATA, _context.primeData().smart___myTruth());
            data.empirical().smart___transition(toState);
        }
    }

    protected void ending(boolean outcome)
        throws CtxException
    {
        if (outcome)
	{
	    /** Metrics **/	
	    _context.eventSuccess();
	    /** Metrics **/	
            _context.transaction().commit();
	}
        else
            _context.transaction().rollback();
    }

    @Override
    public void finish()
        throws CtxException
    {
        super.finish();
        _context.doneWithContext();
    }
    
    public SmartDataED includeData(SmartData data)
        throws CtxException
    {
        System.out.println("Including data: " + data);
        SmartDataTruth truth = data.smart___myTruth();
        SmartDataED ed = new SmartDataED(truth);
        includeEmpiricalData(ed);
        return ed;
    }

    public SmartDataED includeNewData(SmartData data)
        throws CtxException
    {
        System.out.println("Including new data: " + data);
        SmartDataED ed = new SmartDataED(data);
        includeEmpiricalData(ed);
        return ed;
    }

    public ConfigDataED includeNewConfig(ConfigData data)
        throws CtxException
    {
        System.out.println("Including config: " + data);
        ConfigDataED ed = new ConfigDataED(data);
        includeEmpiricalData(ed);
        return ed;
    }

    public void includeResponse(SmartEventResponse resp)
        throws CtxException
    {
        System.out.println("Including response: " + resp);
        SmartERTxnObject rtxn = new SmartERTxnObject(resp);
        includeEmpiricalData(rtxn);
    }
    
    public void includeMessage(SmartEvent event)
    	throws CtxException
    {
    	System.out.println("Including Message: " + event);
    	SmartMessageObject mo = new SmartMessageObject(event);
    	includeEmpiricalData(mo);
    }
    
    public void includeUpload(SmartFileObject data, FlowAdmin flw)
			throws CtxException {
		FileItem i = new FileItem(data._fileSrc, flw.myFlow());
		FileItemEd ed = new FileItemEd(i);
		includeEmpiricalData(ed);
	}
}

