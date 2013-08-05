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
 * File:                org.anon.smart.secure.inbuilt.data.auth.CustomAuthenticator
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An authenticator that verifies customer user pwd
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data.auth;

import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.secure.inbuilt.data.iden.SCredential;
import org.anon.smart.secure.inbuilt.data.iden.Password;
import org.anon.smart.secure.inbuilt.data.iden.Identity;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class CustomAuthenticator implements SAuthenticator
{
    public CustomAuthenticator()
    {
    }

    public AuthDetails authenticate(String identity, SCredential entered)
        throws CtxException
    {
        AuthDetails ret = null;
        boolean bret = (entered instanceof Password);
        if (bret)
        {
            RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
            String flow = flowFor(Identity.class);
            String name = className(Identity.class);
            Identity ident = (Identity)rshell.lookupFor(flow, name, identity);
            assertion().assertNotNull(ident, "Cannot find identity for identity: " + identity);
            SCredential registered = ident.getCredential();
            Password preg = (Password)registered;
            Password pent = (Password)entered;
            ret = new EmptyDetails(preg.equals(pent), ident.getSmartUser(), ident.getIdentity());
        }

        return ret;
    }
}

