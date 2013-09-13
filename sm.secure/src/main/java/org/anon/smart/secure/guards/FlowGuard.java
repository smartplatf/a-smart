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
 * File:                org.anon.smart.secure.guards.FlowGuard
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A guard that gets configuration from the flow
 *
 * ************************************************************
 * */

package org.anon.smart.secure.guards;

import java.util.List;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.Accessed;
import org.anon.smart.secure.access.Visitor;
import org.anon.smart.secure.annot.GuardAnnotate;
import org.anon.smart.secure.flow.CrossLinkSecureFlowDeployment;
import org.anon.smart.secure.flow.CrossLinkSecureConfig;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class FlowGuard extends BaseSGuard implements Constants
{
    public FlowGuard(GuardAnnotate annot, Class cls)
        throws CtxException
    {
        super(annot, cls);
    }

    public FlowGuard(String type, String parms, Class cls)
        throws CtxException
    {
        super(type, parms, cls);
    }

    FlowGuard()
    {
        super();
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        SGuardParms parm = (SGuardParms)vars;
        if (parm.getAnnotate() != null)
            return new FlowGuard(parm.getAnnotate(), parm.getKlass());
        else
            return new FlowGuard(parm.getType(), parm.getParms(), parm.getKlass());
    }

    @Override
    protected Access permitted(Accessed accessed, Visitor visitor, String parms)
        throws CtxException
    {
        Object[] p = accessed.parms();
        String flow = "";
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if (ctx != null)
        {
            flow = ctx.flow();
        }
        else 
        {
            flow = (String)threads().contextLocal(CURRENT_FLOW);
        }

        if ((flow == null) || (flow.length() <= 0))
            return Access.none;

        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkDeploymentShell shell = tenant.deploymentShell();
        CrossLinkFlowDeployment dep = shell.deploymentFor(flow);
        if ((dep == null) || (dep.link() == null))
            return Access.none;

        CrossLinkSecureFlowDeployment sdep = new CrossLinkSecureFlowDeployment(dep.link());
        List<CrossLinkSecureConfig> sconfig = sdep.getSecurity();

        if ((sconfig == null) || (sconfig.size() <= 0))
            return Access.none;

        for (int i = 0; i < sconfig.size(); i++)
        {
            CrossLinkSecureConfig clscfg = sconfig.get(i);
            String guard = clscfg.getGuardType();

            SGuard flowguard = SGuardType.guardFor(guard, clscfg.getParms(), _guardForClass);

            if ((flowguard instanceof BaseSGuard) && (((BaseSGuard)flowguard).crossguardpermitted(accessed, visitor, parms) != Access.none))
                return Access.defaccess;
        }

        return Access.none;
    }
}

