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
 * File:                org.anon.smart.secure.shield.BaseSShield
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A basic shield implementation that works with a list of guards
 *
 * ************************************************************
 * */

package org.anon.smart.secure.shield;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.anon.smart.secure.guards.SGuard;
import org.anon.smart.secure.guards.SGuardType;
import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.AccessScope;
import org.anon.smart.secure.access.AccessLimits;
import org.anon.smart.secure.access.AccessRequest;
import org.anon.smart.secure.access.ALOrganizer;
import org.anon.smart.secure.annot.SecurityAnnotate;
import org.anon.smart.secure.annot.GuardAnnotate;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public abstract class BaseSShield implements SmartShield
{
    protected List<SGuard> _guards;
    private AccessScope _forScope;
    private Class _klass;

    protected BaseSShield(AccessScope scope, Class cls)
    {
        _forScope = scope;
        _guards = new ArrayList<SGuard>();
        _klass = cls;
    }

    protected BaseSShield()
    {
        //purely for factory. do not use this anywhere else.
    }

    public void addGuard(SGuard g)
    {
        _guards.add(g);
    }

    protected List<SGuard> defaultGuards()
        throws CtxException
    {
        //there is no default
        return new ArrayList<SGuard>();
    }

    protected List<SGuard> getGuards()
        throws CtxException
    {
        List<SGuard> useguards = _guards;
        if ((_guards == null) || (_guards.size() <= 0))
            useguards = defaultGuards();
        return useguards;
    }

    public AccessLimits permitted(AccessRequest request)
        throws CtxException
    {
        Access access = Access.none;
        List<SGuard> useguards = getGuards();

        System.out.println("Got guards as: " + useguards);

        if ((useguards == null) || (useguards.size() <= 0))
            return new AccessLimits(ALOrganizer.highest());

        for (SGuard guard : useguards)
        {
            Access a = guard.authorize(request);
            if (access == Access.none)
                access = a;

            access = ALOrganizer.highestOf(access, a);
            System.out.println("Checked using: " + guard + ":" + access + ":" + a);
        }


        return new AccessLimits(access);
    }

    private static enum shields
    {
        //for now this name shd match the AccessScope name
        klass(new KlassShield(), SGuardType.smartrole, SGuardType.unauthenticated, SGuardType.system), 
        instance(new InstanceShield()), 
        attribute(new AttributeShield());

        private SmartShield _shield;
        private SGuardType[] _guards;

        private shields(SmartShield shield, SGuardType ... guards)
        {
            _shield = shield;
            _guards = guards;
        }

        private boolean handles(SGuardType gtype)
        {
            boolean ret = false;
            for (int i = 0; (!ret) && (_guards != null) && (i < _guards.length); i++)
            {
                if (_guards[i].equals(gtype))
                    ret = true;
            }

            return ret;
        }

        public static SmartShield createShield(AccessScope scope, Class klass)
            throws CtxException
        {
            String nm = scope.name();
            shields s = shields.valueOf(nm);
            assertion().assertNotNull(s, "Internal error. Developer please fix as per comment.");
            SShieldParms p = new SShieldParms(scope, klass);
            return (SmartShield)s._shield.repeatMe(p);
        }

        public static AccessScope scopeFor(SGuardType gt)
        {
            AccessScope scope = null;
            for (shields s : shields.values())
            {
                if (s.handles(gt))
                {
                    scope = AccessScope.valueOf(s.name());
                    break;
                }
            }

            return scope;
        }
    }


    public static SmartShield[] shieldsFor(Class klass)
        throws CtxException
    {
        AccessScope[] scopes = ALOrganizer.orderedScope();
        Map<AccessScope, BaseSShield> map = new HashMap<AccessScope, BaseSShield>();
        SmartShield[] ret = new SmartShield[scopes.length];
        for (int i = 0; i < scopes.length; i++)
        {
            ret[i] = shields.createShield(scopes[i], klass);
            map.put(scopes[i], (BaseSShield)ret[i]);
        }

        SecurityAnnotate sannot = (SecurityAnnotate)reflect().getAnnotation(klass, SecurityAnnotate.class); 
        if (sannot != null)
        {
            GuardAnnotate[] g = sannot.security();
            for (int i = 0; i < g.length; i++)
            {
                SGuard guard = SGuardType.guardFor(g[i], klass);
                assertion().assertNotNull(guard, "Cannot find guard of type: " + g[i].type());
                AccessScope shieldtype = shields.scopeFor(guard.guardType());
                assertion().assertNotNull(shieldtype, "Cannot find a shield to handled guard type: " + guard.guardType().name());
                map.get(shieldtype).addGuard(guard);
            }
        }

        return ret;
    }
}

