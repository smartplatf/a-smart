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
 * File:                org.anon.smart.base.tenant.TenantMemoryAttributes
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of attributes for tenant - JCS specific
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import org.apache.jcs.engine.control.event.ElementEvent;
import org.apache.jcs.engine.control.event.behavior.IElementEvent;
import org.apache.jcs.engine.control.event.behavior.IElementEventHandler;
import org.apache.jcs.engine.behavior.IElementAttributes;

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.ElementAttributes;

public class TenantMemoryAttributes extends ElementAttributes implements java.io.Serializable
{
    public class CacheObjectCleanup implements IElementEventHandler
    {
        public void handleElementEvent(IElementEvent event)
        {
            try
            {
                //Have to see if this is always only called when removed.
                System.out.println("Handling event: " + event.getElementEvent() + ":" + ((ElementEvent)event).getSource());

                CacheElement elem = (CacheElement)(((ElementEvent)event).getSource());
                if (elem != null)
                {
                    SmartTenant tenant = (SmartTenant)elem.getVal();
                    if ((tenant != null) && (!tenant.isPlatformOwner()))
                        tenant.cleanup();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public TenantMemoryAttributes()
    {
        super();
        System.out.println("TenantMemoryAttributes is instantiated and new handler registered.");
        addElementEventHandler(new CacheObjectCleanup());
    }
}

