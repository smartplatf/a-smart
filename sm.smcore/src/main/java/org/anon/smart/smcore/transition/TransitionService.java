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
 * File:                org.anon.smart.smcore.transition.TransitionService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of service that need to be done for transitions
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition;

import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.transition.parms.EventProbe;
import org.anon.smart.smcore.transition.parms.PrimeDataProbe;
import org.anon.smart.smcore.channel.server.CrossLinkEventRData;
import org.anon.smart.smcore.transition.graph.TransitionGraphs;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;

import org.anon.utilities.fsm.FiniteState;
import org.anon.utilities.gconcurrent.Graph;
import org.anon.utilities.gconcurrent.execute.ParamType;
import org.anon.utilities.exception.CtxException;

public class TransitionService implements TConstants
{
    private TransitionService()
    {
    }

    public static void initialize()
        throws CtxException
    {
        ParamType.registerProbe(EVENT, new EventProbe(), false);
        ParamType.registerProbe(DATA, new PrimeDataProbe(), false);

    }

    public static TransitionContext createContext(Object rdata)
        throws CtxException
    {
        CrossLinkEventRData clrdata = new CrossLinkEventRData(rdata);
        SmartEvent event = clrdata.event();
        SmartPrimeData data = event.smart___primeData();

        String eventName = event.smart___name();
        String dataName = data.smart___name();

        FiniteState state = data.utilities___currentState();
        String fromstate = state.stateName();
        Graph graph = TransitionGraphs.getGraph(dataName, eventName, fromstate);
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        RuntimeShell rtshell = (RuntimeShell)tenant.runtimeShell();
        TransitionContext tctx = new TransitionContext(rdata, rtshell.transitionExecutor(), graph);
        return tctx;
    }
}

