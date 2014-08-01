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
 * File:                org.anon.smart.smcore.events.internal.InternalMessageDScope
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                May 9, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.internal;

import static org.anon.utilities.objservices.ObjectServiceLocator.anatomy;

import java.util.UUID;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantsHosted;
import org.anon.smart.base.utils.AnnotationUtils;
import org.anon.smart.channels.Route;
import org.anon.smart.channels.data.BaseResponder;
import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.Responder;
import org.anon.smart.channels.data.Source;
import org.anon.smart.smcore.events.CrossLinkEventLegend;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.utilities.atomic.AtomicCounter;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class InternalMessageDScope implements DScope {

	private UUID _requestID;
	private PData _pdata;
    private boolean _keepAlive;
    private AtomicCounter _dataCount;
    
    private String _tenant;
    private String _flow;
    private String _eventName;
    private UUID _sessionId;
    private SmartEvent _event;
    private String _origin;
    private Responder _responder;
    
    

    
	public InternalMessageDScope(SmartEvent event, InternalMessageDataFactory fact)
		throws CtxException
	{
		_requestID = UUID.randomUUID();
		_dataCount = anatomy().jvmEnv().createAtomicCounter(_requestID.toString() + "-EventCounter", 0);
		
		//_flow = event.smart___flowname();
		_eventName = event.smart___name();
		_sessionId =  UUID.randomUUID();//event.smart___legend().sessionID(); //TODO
        CrossLinkSmartTenant t = CrossLinkSmartTenant.currentTenant();
		_tenant = t.getName();
        //RS: This is specifically done only for admin events and messages. Need to see if we want to standardize this.
        //THis is cross posting into another tenant.
        if ((event.getClass().getName().equals("org.anon.smart.smcore.inbuilt.events.NewInternalTenant") || 
                event.getClass().getName().equals("org.anon.smart.smcore.inbuilt.events.InternalEnableFlow") || 
                event.getClass().getName().equals("org.anon.smart.smcore.inbuilt.events.InternalDeployEvent")) && 
                t.controlsAdmin())
        {
            CrossLinkSmartTenant ptenant = TenantsHosted.crosslinkedPlatformOwner();
            _tenant = ptenant.getName();
        }
        else if (t.isPlatformOwner())
        {
            //platform owner can post into other tenants.
            String crosspost = AnnotationUtils.crosspostFor(event.getClass());
            if (crosspost != null)
            {
                Object val = reflect().getAnyFieldValue(event.getClass(), event, crosspost);
                if ((val != null) && (val.toString().length() > 0))
                {
                    CrossLinkSmartTenant ctenant = TenantsHosted.crosslinkedTenantFor(val.toString());
                    assertion().assertNotNull(ctenant, "Cannot find tenant " + val + " to post to.");
                    _tenant = ctenant.getName();
                }
            }
        }
		_event = event;
		_responder = new BaseResponder(_requestID);
		
		_pdata = fact.createPrimal(this, null);
		
	}
	

	@Override
	public void processingData(UUID did) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processedData() throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean processedAll() throws CtxException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Source source() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID channelID() {
		return null;
	}

	@Override
	public UUID requestID() {
		return _requestID;
	}

	@Override
	public PData primary() {
		return _pdata;
	}

	@Override
	public void transmit(PData[] resp) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws CtxException {
		// TODO Auto-generated method stub

	}


	@Override
	public Responder responder() {
		return _responder;
	}
	
	public String origin()
    {
        return _origin;
    }
	

    public String tenant() { return _tenant; }
    public String flow() { return _event.smart___flowname(); }
    String eventName() { return _eventName; }
    UUID sessionId() { return _sessionId; }
    public SmartEvent event() { return _event; }
    
    public Object eventLegend(ClassLoader ldr)
            throws CtxException
    {
            CrossLinkEventLegend legend = new CrossLinkEventLegend(_sessionId, _origin, ldr);
            legend.stampReceived(primary().receivedTime());
            return legend.link();
    }

    public boolean isKeepAlive()
    {
        return false;
    }

}
