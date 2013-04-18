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
 * File:                org.anon.smart.smcore.transition.graph.TransitionGraphExecutor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * an executor for all the nodes of a transition
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.graph;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.inbuilt.responses.ErrorResponse;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.cthreads.CtxRunnable;
import org.anon.utilities.cthreads.RuntimeContext;
import org.anon.utilities.cthreads.CThreadContext;
import org.anon.utilities.gconcurrent.GraphRuntimeNode;
import org.anon.utilities.gconcurrent.execute.ExecuteGraph;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.ExecuteGraphNode;
import org.anon.utilities.gconcurrent.execute.ExecuteGraphParms;
import org.anon.utilities.exception.CtxException;

public class TransitionGraphExecutor extends ExecuteGraph implements CtxRunnable
{
    public TransitionGraphExecutor(GraphRuntimeNode nde, ProbeParms parms)
        throws CtxException
    {
        super(nde, parms);
    }

    protected ExecuteGraphNode graphNodeExecutor(GraphRuntimeNode nde, ProbeParms parms)
        throws CtxException
    {
        return new TransitionExecutor(nde, parms);
    }

    public void recordException(Throwable e)
        throws CtxException
    {
        //TODO:
        //for now record it against the prime always?
        TransitionContext ctx = (TransitionContext)_graphContext;
        SmartDataED ed = ctx.primeED();
        ctx.atomicity().recordException(ed);
        //create an ErrorResponse
        ErrorResponse response = new ErrorResponse(ErrorResponse.servererrors.exception, e);
        e.printStackTrace();
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        ExecuteGraphParms p = (ExecuteGraphParms)vars;
        return new TransitionGraphExecutor(p.runtimeNode(), p.parms());
    }
    
    public RuntimeContext startRuntimeContext(String action)
        throws CtxException
    {
        //TODO:
        return null;
    }

    public CThreadContext runContext()
        throws CtxException
    {
        return (CThreadContext)_graphContext;
    }

    public Map<String, Object> contextLocals()
        throws CtxException
    {
        //TODO:
        return new HashMap<String, Object>();
    }

    public CtxRunnable endWith()
        throws CtxException
    {
        return new EndTransitionExecutor((CThreadContext)_graphContext, this);
    }
}

