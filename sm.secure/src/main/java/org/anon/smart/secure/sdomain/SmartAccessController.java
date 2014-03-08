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
 * File:                org.anon.smart.secure.sdomain.SmartAccessController
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A controller for accessing objects
 *
 * ************************************************************
 * */

package org.anon.smart.secure.sdomain;

import java.security.AccessController;
import java.security.ProtectionDomain;

import org.anon.smart.secure.access.Access;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SmartAccessController
{
    private static final int ACCESS_DENIED_CODE = 1000001;

    private SmartAccessController()
    {
    }

    public static void checkPermission(Access access, Object accessed, Object ... parms)
        throws CtxException
    {
        try
        {
            RequestPermission perm = new RequestPermission(access, accessed, parms);
            AccessController.checkPermission(perm);
        }
        catch (Exception e)
        {
            except().rt(e, ACCESS_DENIED_CODE, new CtxException.Context("No Access", "Not Allowed to access"));
        }
    }

    public static boolean permitted(Access access, Object accessed, Object ... parms)
        throws CtxException
    {
        try
        {
            RequestPermission perm = new RequestPermission(access, accessed, parms);
            ProtectionDomain domain = accessed.getClass().getProtectionDomain();
            System.out.println("Got domain as: " + domain);
            if (domain != null)
                return domain.implies(perm);
            else
                return true;
        }
        catch (Exception e)
        {
            except().rt(e, ACCESS_DENIED_CODE, new CtxException.Context("No Access", "Not Allowed to access"));
        }

        return false;
    }
}

