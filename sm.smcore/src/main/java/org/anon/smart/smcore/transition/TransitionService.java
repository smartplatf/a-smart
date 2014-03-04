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
import org.anon.smart.smcore.transition.parms.TxnDataProbe;
import org.anon.smart.smcore.transition.parms.ConfigProbe;
import org.anon.smart.smcore.transition.parms.LinkedDataProbe;
import org.anon.smart.smcore.transition.parms.SearchDataProbe;
import org.anon.smart.smcore.transition.parms.AllDataProbe;
import org.anon.smart.smcore.transition.parms.SearchConfigProbe;
import org.anon.smart.smcore.transition.parms.NeedsLinkDataProbe;
import org.anon.smart.smcore.channel.server.CrossLinkEventRData;
import org.anon.smart.smcore.transition.graph.TransitionGraphs;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;

import org.anon.utilities.fsm.FiniteState;
import org.anon.utilities.gconcurrent.Graph;
import org.anon.utilities.gconcurrent.execute.ParamType;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

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
        ParamType.registerProbe(CONFIG, new ConfigProbe(), false);
        ParamType.registerProbe(TXN, new TxnDataProbe(), true);
        ParamType.registerProbe(LINK, new LinkedDataProbe(), false);
        ParamType.registerProbe(SEARCH, new SearchDataProbe(), true);
        ParamType.registerProbe(NEEDSLINK, new NeedsLinkDataProbe(), true);
        ParamType.registerProbe(SRCHCFG, new SearchConfigProbe(), true);
        ParamType.registerProbe(ALL, new AllDataProbe(), true);
    }

    public static TransitionContext createContext(Object rdata, MessageSource source)
        throws CtxException
    {
        CrossLinkEventRData clrdata = new CrossLinkEventRData(rdata);
        SmartEvent event = clrdata.event();
        SmartPrimeData data = event.smart___primeData();

        String eventName = event.smart___name();
        String dataName = data.smart___name();
        String extra = event.smart___extratransitionfilter();
        String flow = clrdata.flow();

        FiniteState state = data.utilities___currentState();
        assertion().assertNotNull(state, "Cannot find the state of the object: " + dataName + " to execute event: " + eventName);
        String fromstate = state.stateName();
        Graph graph = TransitionGraphs.getGraph(flow, dataName, eventName, extra, fromstate);
        assertion().assertNotNull(graph, "No transitions found for: " + dataName + ":" + eventName + ":" + fromstate + ":" + flow);
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        RuntimeShell rtshell = (RuntimeShell)tenant.runtimeShell();
        TransitionContext tctx = new TransitionContext(rdata, rtshell.transitionExecutor(), graph, source);
        return tctx;
    }

    public static void cleanup()
        throws CtxException
    {
        TransitionGraphs.cleanup();
    }
}

