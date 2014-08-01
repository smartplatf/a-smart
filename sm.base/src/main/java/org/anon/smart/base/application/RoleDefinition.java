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
 * File:                org.anon.smart.base.application.RoleDefinition
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of definitions for roles
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.util.List;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class RoleDefinition implements java.io.Serializable, VerifiableObject
{
    public class PermitDef implements java.io.Serializable
    {
        private String url;
        private String permission;

        public String getURL() { return url; }
        public String getPermission() { return permission; }

        public boolean verify()
            throws CtxException
        {
            assertion().assertNotNull(url, "Please provide a URL.");
            assertion().assertNotNull(permission, "Please provide a permission.");
            return true;
        }
    }

    private String name;
    private List<PermitDef> permits;

    public RoleDefinition()
    {
    }

    public boolean verify()
        throws CtxException
    {
        assertion().assertNotNull(name, "Need a role name.");
        assertion().assertNotNull(permits, "Need atleast on permit for a role");
        assertion().assertTrue(permits.size() > 0, "Please provide atleast one permit definition.");

        for (PermitDef pdef : permits)
            pdef.verify();

        return true;
    }

    public String getName() { return name; }
    public List<PermitDef> getPermits() { return permits; }
    public boolean isVerified() { return true; }
}

