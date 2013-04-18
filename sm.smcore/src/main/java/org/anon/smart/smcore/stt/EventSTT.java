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
 * File:                org.anon.smart.smcore.stt.EventSTT
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a stereotype for events
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt;

import java.util.UUID;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.base.flow.FlowObject;
import org.anon.smart.smcore.flow.SmartFlow;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.events.EventLegend;

import org.anon.utilities.exception.CtxException;

public class EventSTT implements SmartEvent
{
    private String ___smart_flow_name___;
    private EventLegend ___smart_legend___;
    private FlowObject ___smart_flow___;
    private SmartPrimeData ___smart_primeData___;
    private String ___smart_name___;

    public EventSTT()
    {
    }

    //@MethodExit("constructor")
    //Please note, calling this in the constructor is of no use since the object
    //is created with a silent creator which does not call the constructor.
    //So this will be called directly where created, since we are creating it.
    public void smarteventstt____init()
        throws CtxException
    {
        ___smart_name___ = objectName(this);
    }

    public String smart___name() { return ___smart_name___; }
    public EventLegend smart___legend() { return ___smart_legend___; }
    public FlowObject smart___forFlow() { return ___smart_flow___; }
    public SmartPrimeData smart___primeData() { return ___smart_primeData___; }
    public UUID smart___eventID() { return ___smart_legend___.eventID(); }
    public String smart___flowname() { return ___smart_flow_name___; }

    public String smart___extratransitionfilter()
        throws CtxException
    {
        String val = filterFor(this.getClass());
        if ((val == null) || (val.length() <= 0))
            return "";

        Object filter = reflect().getAnyFieldValue(this.getClass(), this, val);
        if (filter != null)
            return filter.toString();

        return "";
    }
}

