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
 * File:                org.anon.smart.smcore.stt.MessageSTT
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                May 3, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt;

import static org.anon.smart.base.utils.AnnotationUtils.objectName;
import static org.anon.utilities.objservices.ObjectServiceLocator.threads;

import java.util.UUID;

import org.anon.smart.base.flow.FlowObject;
import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.events.EventLegend;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.utilities.exception.CtxException;

public class MessageSTT implements SmartEvent {

	  private String ___smart_flow_name___;
	  private EventLegend ___smart_legend___;
	  private FlowObject ___smart_flow___;
	  private SmartPrimeData ___smart_primeData___;
	  private String ___smart_name___;

	
	@Override
	@MethodExit("constructor")
	public void smarteventstt____init() throws CtxException {
		___smart_name___ = objectName(this);
		___smart_legend___ = new EventLegend();
		
		//include into txn
        if (threads().threadContext() instanceof TransitionContext)
        {
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            if (ctx != null)
                ctx.atomicity().includeMessage(this);
        }
        
		
	}

	@Override
	public String smart___name()  { return ___smart_name___; }

	@Override
	public UUID smart___eventID() { return ___smart_legend___.eventID(); }

	@Override
	public FlowObject smart___forFlow() { return ___smart_flow___; }
	
	@Override
	public EventLegend smart___legend() { return ___smart_legend___; }

	@Override
	public SmartPrimeData smart___primeData() { return ___smart_primeData___; }

	@Override
	public String smart___flowname() { return ___smart_flow_name___; }

	@Override
	public String smart___extratransitionfilter() throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}
}
