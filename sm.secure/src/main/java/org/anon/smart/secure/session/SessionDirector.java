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
 * File:                org.anon.smart.secure.session.SessionDirector
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A session manager for sessions
 *
 * ************************************************************
 * */

package org.anon.smart.secure.session;

import java.util.UUID;

import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.secure.inbuilt.data.auth.AuthDetails;
import org.anon.smart.secure.sdomain.SmartSecureData;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class SessionDirector
{
    private static final String SESSION_STORE_NAME = "CURRENT_SESSION";

    private SessionDirector()
    {
    }

    public static Session createSession(AuthDetails auth)
        throws CtxException
    {
        Session sess = new Session(auth, auth.smartUser(), auth.identity());
        //For now no session space is created. Will when a requirement comes
        //set into context local so hence forth it can be used if required.
        threads().addToContextLocals(SESSION_STORE_NAME, sess);
        return sess;
    }

    public static Session sessionFor(String sessId)
        throws CtxException
    {
        UUID osessid = UUID.fromString(sessId);
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String flow = flowFor(Session.class);
        String group = className(Session.class);
        Object sess = rshell.lookupFor(flow, group, osessid);
        return (Session)sess;
    }

    public static void removeSession()
        throws CtxException
    {
        threads().addToContextLocals(SESSION_STORE_NAME, null);
    }

    public static void setupContext(Object session)
        throws CtxException
    {
        threads().addToContextLocals(SESSION_STORE_NAME, session);
    }

    public static Object crosslinkSessionIn(String sessId, CrossLinkSmartTenant tenant)
        throws CtxException
    {
        CrossLinkAny cany = new CrossLinkAny(SessionDirector.class.getName(), tenant.getRelatedLoader());
        Object obj = cany.invoke("sessionFor", sessId);
        if (obj != null)
            cany.invoke("setupContext", new Class[] { Object.class }, new Object[] { obj });
        return obj;
    }

    public static void removeSessionFrom(CrossLinkSmartTenant tenant)
        throws CtxException
    {
        CrossLinkAny cany = new CrossLinkAny(SessionDirector.class.getName(), tenant.getRelatedLoader());
        cany.invoke("removeSession");
    }

    public static Session currentSession()
        throws CtxException
    {
        Session sess = (Session)threads().contextLocal(SESSION_STORE_NAME);
        if (sess == null)
        {
            //if not present in the locals, try to get it from the context.
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            if (ctx != null)
            {
                Object evt = ctx.event();
                if ((evt != null) && (evt instanceof SmartSecureData))
                {
                    SmartSecureData sdata = (SmartSecureData)evt;
                    sess = sdata.smart___session();
                }
            }
        }

        return sess;
    }

}

