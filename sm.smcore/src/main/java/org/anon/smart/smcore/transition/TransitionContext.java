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
 * File:                org.anon.smart.smcore.transition.TransitionContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context used for transition execution
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.anon.smart.atomicity.Atomicity;
import org.anon.smart.base.dspace.DSpaceService;
import org.anon.smart.base.dspace.TransactDSpace;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.channel.server.CrossLinkEventRData;
import org.anon.smart.smcore.monitor.MetricsManager;
import org.anon.smart.smcore.monitor.MonitorAction;
import org.anon.smart.smcore.transition.graph.TransitionGraphExecutor;
import org.anon.smart.smcore.transition.parms.TransitionProbeParms;
import org.anon.smart.smcore.transition.atomicity.TAtomicity;

import org.anon.utilities.cthreads.CThreadContext;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.gconcurrent.Graph;
import org.anon.utilities.gconcurrent.GraphRuntimeNode;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.ExecuteGraph;
import org.anon.utilities.gconcurrent.execute.AbstractGraphContext;
import org.anon.utilities.exception.CtxException;

public class TransitionContext extends AbstractGraphContext implements CThreadContext
{
    private SmartEvent _event;
    private SmartPrimeData _prime;
    private CrossLinkEventRData _rData;
    private TAtomicity _atomicity;
    private String _toState;
    private TTransaction _transaction;
    private MessageSource _source;
    private SmartDataED _primeED;
    private String _flow;

    public TransitionContext(Object data, ExecutorService service, Graph graph, MessageSource src)
        throws CtxException
    {
        super(graph, service);
        _rData = new CrossLinkEventRData(data);
        _event = _rData.event();
        _prime = _event.smart___primeData();
        _atomicity = new TAtomicity(this);
        _transaction = new TTransaction(_atomicity.atomicID());
        _source = src;
        _flow = _rData.flow();
    }

    public SmartDataED primeED() { return _primeED; }
    public void setupPrimeED(SmartDataED ed) { _primeED = ed; }
    public TAtomicity atomicity() { return _atomicity; }
    public SmartPrimeData primeData() { return _prime; }
    public SmartEvent event() { return _event; }
    public CrossLinkEventRData rdata() { return _rData; }
    public String flow() { return _flow; }
    public void doneWithContext()
        throws CtxException
    {
    	_source.doneMessage();
    }
    
    /** Metrics 
     * @throws CtxException **/
    public void eventSuccess() throws CtxException
    {
    	MonitorAction action = MonitorAction.EVENTEXECUTED;
        MetricsManager.handleMetricsfor(transaction().getTransaction(TenantConstants.MONITOR_SPACE),
        		_event, action);
    	
    }
    /** Metrics **/
    
    protected ExecuteGraph executorFor(GraphRuntimeNode rtnde, ProbeParms parms)
        throws CtxException
    {
        return new TransitionGraphExecutor(rtnde, parms);
    }

    protected ProbeParms myParms()
        throws CtxException
    {
        List<Object> parms = new ArrayList<Object>();
        parms.add(_event);
        parms.add(_primeED.empirical());
        return new TransitionProbeParms(this, parms);
    }

    public UUID id()
    {
        return _atomicity.atomicID();
    }

    public String extras()
    {
        return "null";
    }

    public void modifyToState(String state)
    {
        _toState = state;
    }

    public String toState()
    {
        return _toState;
    }

    public TTransaction transaction() { return _transaction; }
}

