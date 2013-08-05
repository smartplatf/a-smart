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
 * File:                org.anon.smart.secure.inbuilt.transition.AuthenticateUser
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An authentication transition
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.transition;

import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.secure.inbuilt.data.iden.IdentityType;
import org.anon.smart.secure.inbuilt.data.iden.SCredential;
import org.anon.smart.secure.inbuilt.data.auth.AuthDetails;
import org.anon.smart.secure.inbuilt.data.auth.SAuthenticator;
import org.anon.smart.secure.inbuilt.events.Authenticate;
import org.anon.smart.secure.inbuilt.responses.SessionDetails;
import org.anon.smart.secure.session.SessionDirector;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class AuthenticateUser
{
    public AuthenticateUser()
    {
    }

    public void authenticateUser(Authenticate authen)
        throws CtxException
    {
        SCredential cred = IdentityType.getCredential(authen.getType(), authen.getPassword());
        assertion().assertNotNull(cred, "The type is not recognized: " + authen.getType());

        SAuthenticator authenticator = cred.authenticator();
        AuthDetails det = authenticator.authenticate(authen.getIdentity(), cred);
        assertion().assertNotNull(det, "Cannot authenticate user: " + authen.getIdentity());
        assertion().assertTrue(det.isVerified(), "Invalid credentials for: " + authen.getIdentity());
        Session sess = SessionDirector.createSession(det);
        SessionDetails details = new SessionDetails(sess.getSessionId());
    }
}

