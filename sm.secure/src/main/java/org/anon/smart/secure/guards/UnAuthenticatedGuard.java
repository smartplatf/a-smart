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
 * File:                org.anon.smart.secure.guards.UnAuthenticatedGuard
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A guard that allows unauthenticated access to objects
 *
 * ************************************************************
 * */

package org.anon.smart.secure.guards;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.Accessed;
import org.anon.smart.secure.access.Visitor;
import org.anon.smart.secure.annot.GuardAnnotate;
import org.anon.smart.smcore.transition.TransitionContext;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class UnAuthenticatedGuard extends BaseSGuard implements Constants
{
    private String[] _allowedEvents;

    public UnAuthenticatedGuard(GuardAnnotate annot, Class cls)
        throws CtxException
    {
        super(annot, cls);
        _allowedEvents = value().listAsString(annot.parm());
    }

    public UnAuthenticatedGuard(String type, String parms, Class cls)
        throws CtxException
    {
        super(type, parms, cls);
        _allowedEvents = value().listAsString(parms);
    }

    UnAuthenticatedGuard()
    {
        super();
    }

    @Override
    protected Access permitted(Accessed accessed, Visitor visitor, String parms)
        throws CtxException
    {
        Object[] p = accessed.parms();
        String evt = "";
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if (ctx != null)
        {
            evt = ctx.event().smart___name();
        }
        else 
        {
            evt = (String)threads().contextLocal(CURRENT_EVENT);
        }

        System.out.println("Got event as: " + evt + ":" + ctx + ":" );

        for (int i = 0; (evt != null) && (evt.length() > 0) && (i < _allowedEvents.length); i++)
        {
            System.out.println("Got event as: " + evt + ":" + _allowedEvents[i]);
            if (_allowedEvents[i].equals(evt))
                return Access.defaccess;
        }

        return Access.none;
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        SGuardParms parm = (SGuardParms)vars;
        if (parm.getAnnotate() != null)
            return new UnAuthenticatedGuard(parm.getAnnotate(), parm.getKlass());
        else
            return new UnAuthenticatedGuard(parm.getType(), parm.getParms(), parm.getKlass());
    }
}

