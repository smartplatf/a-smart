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
 * File:                org.anon.smart.smcore.transition.graph.EndTransitionExecutor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An executor that ends the execution of all transitions
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.graph;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.smcore.transition.TransitionContext;

import org.anon.utilities.cthreads.CtxRunnable;
import org.anon.utilities.cthreads.CThreadContext;
import org.anon.utilities.cthreads.RuntimeContext;
import org.anon.utilities.exception.CtxException;

public class EndTransitionExecutor implements CtxRunnable
{
    private CThreadContext _context;
    private CtxRunnable _parent;
    private boolean _hasCompleted;

    public EndTransitionExecutor(CThreadContext ctx, CtxRunnable running)
    {
        _parent = running;
        _context = ctx;
        _hasCompleted = false;
    }

    public void recordException(Throwable e)
        throws CtxException
    {
        //TODO:
        e.printStackTrace();
    }

    public void run()
    {
        try
        {
            if ((_context instanceof TransitionContext) && (_parent.hasCompleted()))
            {
                TransitionContext gctx = (TransitionContext)_context;
                if (gctx.graphDone())
                {
                    gctx.atomicity().finish();
                }
            }
            _hasCompleted = true;
        }
        catch (Exception e)
        {
            //TODO:
            e.printStackTrace();
        }
    }

    public boolean hasCompleted()
        throws CtxException
    {
        return _hasCompleted;
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
        return (CThreadContext)_context;
    }

    public Map<String, Object> contextLocals()
        throws CtxException
    {
        return new HashMap<String, Object>();
    }

    public CtxRunnable endWith()
        throws CtxException
    {
        return null;
    }
}

