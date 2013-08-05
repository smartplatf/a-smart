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
 * File:                org.anon.smart.smcore.flow.SmartFlow
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A flow that works with smartdata and smartevents
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.flow;

import org.anon.smart.base.flow.BasicFlow;
import org.anon.smart.base.flow.FlowModel;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.smcore.flow.jitq.FlowEventListener;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.jitq.JITProcessQueue;
import org.anon.utilities.exception.CtxException;

public class SmartFlow extends BasicFlow
{
    static
    {
        try
        {
            String tenant = CrossLinkSmartTenant.currentTenant().getName();
            jitq().registerProcessor(tenant);
        }
        catch (Exception e)
        {
            //TODO: log an error
            e.printStackTrace();
        }
    }

    private transient JITProcessQueue _externalQueue;
    private transient JITProcessQueue _internalQueue;

    public SmartFlow(FlowModel model)
        throws CtxException
    {
        super(model);
        String tenant = CrossLinkSmartTenant.currentTenant().getName();
        FlowEventListener eelsnr = new FlowEventListener();
        _externalQueue = anatomy().jvmEnv().jitQueueFor(tenant, model.name() + "-External", this, eelsnr);
        eelsnr.setQueue(_externalQueue);
        FlowEventListener ielsnr = new FlowEventListener();
        _internalQueue = anatomy().jvmEnv().jitQueueFor(tenant, model.name() + "-Internal", this, ielsnr);
        ielsnr.setQueue(_internalQueue);
    }

    public void postExternal(Object event)
        throws CtxException
    {
    	
	//Adding this check: TODO
    	if(_externalQueue == null)
    	{
    	    String tenant = CrossLinkSmartTenant.currentTenant().getName();
            FlowEventListener eelsnr = new FlowEventListener();
            _externalQueue = anatomy().jvmEnv().jitQueueFor(tenant, model().name() + "-External", this, eelsnr);
            eelsnr.setQueue(_externalQueue);
    	}
        _externalQueue.add(event);
    }

    public void postInternal(Object event)
        throws CtxException
    {
    	if(_internalQueue == null)
    	{
    		String tenant = CrossLinkSmartTenant.currentTenant().getName();
    		 FlowEventListener ielsnr = new FlowEventListener();
    	    _internalQueue = anatomy().jvmEnv().jitQueueFor(tenant, model().name() + "-Internal", this, ielsnr);
            ielsnr.setQueue(_internalQueue);
            
    	}
        _internalQueue.add(event);
    }
}

