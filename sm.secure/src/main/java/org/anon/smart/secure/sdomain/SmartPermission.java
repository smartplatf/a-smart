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
 * File:                org.anon.smart.secure.sdomain.SmartPermission
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A permission implementation that checks for security
 *
 * ************************************************************
 * */

package org.anon.smart.secure.sdomain;

import java.security.Permission;
import java.security.BasicPermission;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.ALOrganizer;
import org.anon.smart.secure.access.AccessLimits;
import org.anon.smart.secure.shield.SmartShield;
import org.anon.smart.secure.shield.BaseSShield;

import org.anon.utilities.exception.CtxException;

public class SmartPermission extends BasicPermission
{
    private SmartShield[] _protectedBy;
    private Class _cls;

    public SmartPermission(Class cls)
        throws CtxException
    {
        super("none");
        _protectedBy = BaseSShield.shieldsFor(cls);
        _cls = cls;
    }

    public boolean implies(Permission request)
    {
        boolean ret = true;

        System.out.println("Checking for Permission: " + request + ":" + (request instanceof RequestPermission) + ":" + _cls);
        if (request instanceof RequestPermission)
        {
            try
            {
                System.out.println("Protected by: " + _protectedBy.length);
                RequestPermission permit = (RequestPermission)request;
                AccessLimits[] limits = new AccessLimits[_protectedBy.length];
                for (int i = 0; i < _protectedBy.length; i++)
                    limits[i] = _protectedBy[i].permitted(permit.getRequest());

                Access permitted = ALOrganizer.lowest(limits);
                Access requested = permit.getRequest().getRequestedAccess();
                ret = ALOrganizer.implies(permitted, requested);
                System.out.println("Checking: " + permitted + ":" + requested + ":" + ret);
            }
            catch (Exception e)
            {
                e.printStackTrace(); //log and return false;
                ret = false;
            }
        }

        System.out.println("Returning: " + ret + ":" + _cls + ":" + request);
        return ret;
    }
}

