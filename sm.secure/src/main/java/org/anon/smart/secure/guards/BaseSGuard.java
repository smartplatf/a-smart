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
 * File:                org.anon.smart.secure.guards.BaseSGuard
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A base implementation for sguard used by others
 *
 * ************************************************************
 * */

package org.anon.smart.secure.guards;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.Accessed;
import org.anon.smart.secure.access.Visitor;
import org.anon.smart.secure.access.AccessRequest;
import org.anon.smart.secure.annot.GuardAnnotate;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public abstract class BaseSGuard implements SGuard
{
    private String[] _guardFor;
    private Access _access;
    private String _guardParm;
    private String _guardName;
    private SGuardType _type;
    protected Class _guardForClass;

    protected BaseSGuard()
    {
    }

    protected BaseSGuard(GuardAnnotate annot, Class cls)
        throws CtxException
    {
        _guardName = annot.name();
        assertion().assertNotNull(_guardName, "Cannot create a guard with null name: ");
        _guardFor = annot.guardFor();
        _guardForClass = cls;
        assertion().assertNotNull(cls, "Guard cannot be added for a null class");
        _type = SGuardType.valueOf(annot.type());
        assertion().assertNotNull(_type, "Cannot find guard of type: " + annot.type() + " for " + annot.name());
        if (annot.access() != null)
        {
            _access = Access.valueOf(annot.access());
            assertion().assertNotNull(_access, "Cannot find access of type " + annot.access() + " for " + annot.name());
        }
        _guardParm = annot.parm();
    }

    protected BaseSGuard(String type, String parms, Class cls)
        throws CtxException
    {
        _guardName = "temporary";
        _guardForClass = cls;
        _type = SGuardType.valueOf(type);
        _guardParm = parms;
    }

    protected BaseSGuard(SGuardType type, Class cls)
        throws CtxException
    {
        _guardName = "default";
        _type = type;
        _guardForClass = cls;
        _access = Access.none;
    }

    public String guardName() 
    {
        return _guardName;
    }

    public String[] guardFor()
    {
        return _guardFor;
    }

    public SGuardType guardType()
    {
        return _type;
    }

    protected abstract Access permitted(Accessed accessed, Visitor visitor, String parms)
        throws CtxException;

    Access crossguardpermitted(Accessed accessed, Visitor visitor, String parms)
        throws CtxException
    {
        return permitted(accessed, visitor, parms);
    }

    public Access authorize(AccessRequest request)
        throws CtxException
    {
        Access access = Access.none;
        if (type().isAssignable(request.getAccessed().getAccessedClass(), _guardForClass))
        {
            access = permitted(request.getAccessed(), request.getVisitor(), _guardParm);
            //if def access is returned then pickup from the setup.
            if (access.equals(Access.defaccess))
                access = _access;

            //if it is still def access, then send as read access.
            if (access.equals(Access.defaccess))
                access = access.read;
        }

        return access;
    }
}

