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
 * File:                org.anon.smart.secure.transition.parms.SessionProbe
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A probe that returns session related values
 *
 * ************************************************************
 * */

package org.anon.smart.secure.transition.parms;

import java.lang.reflect.Type;

import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.secure.session.SessionDirector;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;

import org.anon.utilities.gconcurrent.execute.ParamType;
import org.anon.utilities.gconcurrent.execute.PDescriptor;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.PProbe;
import org.anon.utilities.exception.CtxException;

public class SessionProbe implements PProbe, AtomicityConstants
{
    public SessionProbe()
    {
    }

    public Object valueFor(Class cls, Type t, ProbeParms parms, PDescriptor desc)
        throws CtxException
    {
        if (desc != null)
        {
            String attr = desc.attribute();
            Session sess = SessionDirector.currentSession();
            System.out.println("Getting the session details: " + sess + ":" + attr);
            if (sess != null)
            {
                //only these are handled now
                if (attr.equals("identity"))
                {
                    System.out.println("UserId is: " + sess.getIdentity());
                    return sess.getIdentity();
                }
                else if (attr.equals("user"))
                {
                    System.out.println("UserId is: " + sess.getUserId());
                    return sess.getUserId();
                }
            }
        }
        return null;
    }

    public Object valueFor(Class cls, Type type, ProbeParms parms)
        throws CtxException
    {
        //if descriptor is not given then we do not know what to search.
        return null;
    }

    public Object valueFor(ProbeParms parms, Type type, PDescriptor desc)
        throws CtxException
    {
        return valueFor(null, type, parms, desc);
    }

    public void releaseValues(Object[] val)
        throws CtxException
    {
    }
}

