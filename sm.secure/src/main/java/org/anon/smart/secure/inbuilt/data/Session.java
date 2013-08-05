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
 * File:                org.anon.smart.secure.inbuilt.data.Session
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A session object generated for an authenticated user
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data;

import java.util.UUID;

import org.anon.smart.secure.inbuilt.data.auth.AuthDetails;

public class Session implements java.io.Serializable
{
    private String _smartUser;
    private String _identity;
    private UUID _sessionId;
    private boolean _valid;
    private AuthDetails _auth;
    private transient SmartUser _user;

    public Session(AuthDetails det, String user, String identity)
    {
        _sessionId = UUID.randomUUID();
        _auth = det;
        _valid = (det != null);
        _smartUser = user;
        _identity = identity;
    }

    public UUID getSessionId() { return _sessionId; }
    public AuthDetails getAuthDetails() { return _auth; }
    public String getUserId() { return _smartUser; }
    public String getIdentity() { return _identity; }
    public SmartUser getUser() { return _user; } //don't store but if present use it.
    public void setUser(SmartUser usr)
    {
        _user = usr;
    }
}

