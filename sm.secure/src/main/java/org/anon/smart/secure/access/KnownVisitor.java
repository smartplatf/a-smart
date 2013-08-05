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
 * File:                org.anon.smart.secure.access.KnownVisitor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A visitor who has session and user
 *
 * ************************************************************
 * */

package org.anon.smart.secure.access;

import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class KnownVisitor implements Visitor
{
    private CrossLinkSmartTenant _tenant;
    private Session _session;
    private String _user;

    public KnownVisitor(Session sess)
        throws CtxException
    {
        assertion().assertNotNull(sess, "Cannot create a known user with no session.");
        _tenant = CrossLinkSmartTenant.currentTenant();
        _session = sess;
        //DO NOT USE THIS. It goes into a infinite loop
        //_user = SmartUser.getUser(_session.getUserId());
        _user = _session.getUserId();
    }

    public String associatedUser()
    {
        return _user;
    }

    public CrossLinkSmartTenant associatedTenant()
    {
        return _tenant;
    }

    public Session associatedSession()
    {
        return _session;
    }
}

