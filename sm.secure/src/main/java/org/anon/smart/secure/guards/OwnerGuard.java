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
 * File:                org.anon.smart.secure.guards.OwnerGuard
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A guard which only allows access to owners
 *
 * ************************************************************
 * */

package org.anon.smart.secure.guards;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.Accessed;
import org.anon.smart.secure.access.Visitor;
import org.anon.smart.secure.annot.GuardAnnotate;
import org.anon.smart.secure.inbuilt.data.SmartUser;
import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.transition.TransitionContext;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class OwnerGuard extends BaseSGuard implements Constants
{
    private String[] _gatedEvents;

    public OwnerGuard(GuardAnnotate annot, Class cls)
        throws CtxException
    {
        super(annot, cls);
        _gatedEvents = value().listAsString(annot.parm());
    }

    public OwnerGuard(String type, String parms, Class cls)
        throws CtxException
    {
        super(type, parms, cls);
        _gatedEvents = value().listAsString(parms);
    }

    OwnerGuard()
    {
        super();
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        SGuardParms parm = (SGuardParms)vars;
        if (parm.getAnnotate() != null)
            return new OwnerGuard(parm.getAnnotate(), parm.getKlass());
        else
            return new OwnerGuard(parm.getType(), parm.getParms(), parm.getKlass());
    }

    @Override
    protected Access permitted(Accessed accessed, Visitor visitor, String parms)
        throws CtxException
    {
        String system = (String)threads().contextLocal(SYSTEM_RUNTIME);

        if ((system != null) && (system.equals("TimedEvent")))
            return Access.execute;


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

        boolean isgated = false;
        for (int i = 0; (evt != null) && (evt.length() > 0) && (i < _gatedEvents.length); i++)
        {
            System.out.println("Owner Access: Got event as: " + evt + ":" + _gatedEvents[i]);
            if (_gatedEvents[i].equals(evt))
            {
                isgated = true;
                break;
            }
        }

        if (!isgated)
            return Access.execute;

        if (visitor.associatedSession() == null)
            return Access.none;

        System.out.println("Got event as: " + evt + ":" + ctx + ":" );
        Session sess = visitor.associatedSession();
        SmartUser usr = sess.getUser();
        //if I am the same user, then allow
        if (accessed.getAccessed() instanceof SmartUser)
        {
            usr = (SmartUser)accessed.getAccessed();
            if (sess.getUserId().equals(usr.getID()))
            {
                sess.setUser(usr);
                return Access.execute; //a logged in session with the user, hence has access
            }
        }

        Object obj = accessed.getAccessed();
        if (obj instanceof SmartData)
        {
            SmartData sdata = (SmartData)obj;
            if ((sdata.smart___owner() != null) && (sdata.smart___owner().equals(usr.getID())))
                return Access.defaccess;
        }

        return Access.none;
    }
}

