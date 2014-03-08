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
 * File:                org.anon.smart.secure.stt.SecureDataSTT
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An stt for secure data stereotype
 *
 * ************************************************************
 * */

package org.anon.smart.secure.stt;

import java.util.List;
import java.util.ArrayList;
import java.security.AccessController;

import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.sdomain.SmartPAction;
import org.anon.smart.secure.sdomain.SmartSecureData;
import org.anon.smart.secure.sdomain.SmartAccessController;
import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.transition.TransitionContext;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SecureDataSTT implements SmartSecureData
{
    //for events this gets set when the securesanitization is run.
    //will not be setup for any other object type and will be null.
    private transient Session ___smart_session___;

    public SecureDataSTT()
    {
    }

    @MethodExit("constructor")
    private void smart___initializeSecure()
        throws CtxException
    {
        //this will setup the owner if it is smart data
        Object obj = this;
        if (obj instanceof SmartData)
        {
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            if (ctx == null)
                return;

            Object evt = ctx.event();
            if (evt instanceof SmartSecureData)
            {
                SmartSecureData data = (SmartSecureData)evt;
                SmartData sdata = (SmartData)obj;
                Session sess = data.smart___session();
                if ((sess != null) && (sess.getUserId() != null))
                    sdata.smart___setOwner(sess.getUserId());
            }
        }
    }

    public List<Object> ___smart_permitted___(Access access, Object ... parms)
        throws CtxException
    {
        System.out.println("Checking for access for: " + access + ":" + this);
        SmartAccessController.checkPermission(access, this, parms);
        return new ArrayList<Object>();
    }

    public List<Object> ___smart_checkPermission___(Access access)
        throws CtxException
    {
        try
        {
            SmartPAction action = new SmartPAction(access, this);
            List<Object> perm = (List<Object>)AccessController.doPrivileged(action);
            return perm;
        }
        catch (java.security.PrivilegedActionException pe)
        {
            Throwable c = pe.getCause();
            Throwable e = pe;
            String msg = pe.getMessage();
            if (c != null)
            {
                msg = c.getMessage();
                e = c;
            }
            except().rt(e, new CtxException.Context("Error", msg));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().rt(e, new CtxException.Context("Error", e.getMessage()));
        }
        return null;
    }

    public Session smart___session() { return ___smart_session___; }
}

