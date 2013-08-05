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
 * File:                org.anon.smart.smcore.events.internal.MessagePData
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                May 27, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.internal;

import java.util.UUID;

import org.anon.smart.channels.data.CData;
import org.anon.smart.channels.data.DScope;
import org.anon.smart.channels.data.PData;
import org.anon.smart.smcore.events.SmartEvent;

public class MessagePData  extends PData{

	private String _tenant;
    private String _flow;
    private String _eventName;
    private UUID _sessionId;
    
	public MessagePData(DScope scope, CData data) {
		
		super(scope, data);
		
		InternalMessageDScope dscope = (InternalMessageDScope)scope;
        _tenant = dscope.tenant();
        _flow = dscope.flow();
        _eventName = dscope.eventName();
        _sessionId = dscope.sessionId();
	}
	
	public String tenant() { return _tenant; }
    public String flow() { return _flow; }
    public String eventName() { return _eventName; }
    public UUID sessionId() { return _sessionId; }
    public SmartEvent event() { return ((InternalMessageDScope)dscope()).event(); }

}
