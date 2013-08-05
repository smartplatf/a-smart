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
 * File:                org.anon.smart.secure.guards.SystemGuard
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a system access guard used for setting up during startup
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
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class SystemGuard extends BaseSGuard implements Constants
{
    private String[] _allowedObjects;

    public SystemGuard(GuardAnnotate annot, Class cls)
        throws CtxException
    {
        super(annot, cls);
        init();
    }

    private void init()
    {
        _allowedObjects = new String[3];
        int cnt = 0;
        _allowedObjects[cnt] = "org.anon.smart.secure.inbuilt.data.SmartRole";
        cnt++;
        _allowedObjects[cnt] = "org.anon.smart.secure.inbuilt.data.SmartUser";
        cnt++;
        _allowedObjects[cnt] = "org.anon.smart.secure.inbuilt.data.iden.Identity";
        cnt++;
    }

    public SystemGuard(Class cls)
        throws CtxException
    {
        super(SGuardType.system, cls);
        init();
    }

    SystemGuard()
    {
        super();
    }

    @Override
    protected Access permitted(Accessed accessed, Visitor visitor, String parms)
        throws CtxException
    {
        Class cls = accessed.getAccessedClass();
        String system = (String)threads().contextLocal(SYSTEM_RUNTIME);
        if ((system != null) && (system.equals(STARTUP_DEFAULTS)))
        {
            for (int i = 0; i < _allowedObjects.length; i++)
            {
                if (_allowedObjects[i].equals(cls.getName()))
                    return Access.execute;
            }
        }

        return Access.none;
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        except().te("Cannot use this from external soa files.");
        //SGuardParms parm = (SGuardParms)vars;
        //return new SystemGuard(parm.getAnnotate(), parm.getKlass());
        return null;
    }
}

