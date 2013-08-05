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
 * File:                org.anon.smart.secure.guards.SGuardType
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of supported guards
 *
 * ************************************************************
 * */

package org.anon.smart.secure.guards;

import org.anon.smart.secure.annot.GuardAnnotate;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public enum SGuardType
{
    smartrole(new SmartRoleGuard()),
    system(new SystemGuard()),
    unauthenticated(new UnAuthenticatedGuard());

    private SGuard _guard;

    private SGuardType(SGuard g)
    {
        _guard = g;
    }

    public static SGuard guardFor(GuardAnnotate annon, Class cls)
        throws CtxException
    {
        SGuardType gt = SGuardType.valueOf(annon.type());
        assertion().assertNotNull(gt, "Cannot find a guard of type: " + annon.type());
        SGuardParms parm = new SGuardParms(annon, cls);
        SGuard ret = (SGuard)gt._guard.repeatMe(parm);
        return ret;
    }
}

