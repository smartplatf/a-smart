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
 * File:                org.anon.smart.smcore.transition.graph.TransitionExecutor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An executor for a single transiton
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.graph;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.transition.TransitionContext;

import org.anon.utilities.gconcurrent.GraphRuntimeNode;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.ExecuteGraphNode;
import org.anon.utilities.exception.CtxException;

public class TransitionExecutor extends ExecuteGraphNode
{
    public TransitionExecutor(GraphRuntimeNode nde, ProbeParms parms)
    {
        super(nde, parms);
    }

    protected Object runtimeObject(Class cls)
        throws CtxException
    {
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        RuntimeShell shell = (RuntimeShell)tenant.runtimeShell();
        Object transition = shell.getTransition(cls);
        return transition;
    }

    protected boolean successOrFailure(Object ret)
        throws CtxException
    {
        //for now the assumption is if a false is returned do not transition
        boolean transition = true;
        if ((ret != null) && (ret instanceof Boolean))
            transition = ((Boolean)ret).booleanValue();

        if (transition)
        {
            TransitionNodeDetails det = (TransitionNodeDetails)_grtNode.details();
            TransitionContext ctx = (TransitionContext)_context;
            //TODO: this is under the assumption that the parameter passed is the prime data
            //when a related data is introduced this needs to be changed
            System.out.println("The returned value is true: transitioning to: " + det.to());
            ctx.modifyToState(det.to());
        }
        return true;
    }

    protected void done()
        throws CtxException
    {
        //TODO:
    }
}

