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
 *
 * ************************************************************
 * HEADERS
 * ************************************************************
 * File:                org.anon.smart.secure.test.user.TestUser
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Test cases to test user functions
 *
 * ************************************************************
 * */

package org.anon.smart.secure.test.user;

import org.junit.Test;
import static org.junit.Assert.*;

import org.anon.smart.secure.test.SecureServerUtilities;
import org.anon.smart.secure.test.TestSecureClient;
import org.anon.smart.smcore.test.AssertJSONResponse;

public class TestUser
{
    @Test
    public void testTestUser()
        throws Exception
    {
        int port = 9041;
        SecureServerUtilities sserver = new SecureServerUtilities(port);
        sserver.runServer("org.anon.smart.secure.test.SecureServerRunner");

        TestSecureClient clnt = new TestSecureClient(port, "localhost", "usertenant", "Security", "");
        clnt.authenticateAdmin("smartadmin", "smartadmin");
        clnt.createTenant();

        clnt.authenticate("usertenantadmin", "usertenantadmin");

        AssertJSONResponse resp = clnt.post("CreateUser", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Security'},'id':'rsankarx','name':'Raji Sankar'}");
        assertTrue(resp != null);

        resp = clnt.post("AddIdentity", "{'SmartUser':{'___smart_action___':'lookup','___smart_value___':'rsankarx'},'identity':'rsankarx','credentialKey':'rsankarx','type':'custom'}");
        assertTrue(resp != null);
        resp = clnt.post("AddIdentity", "{'SmartUser':{'___smart_action___':'lookup','___smart_value___':'rsankarx'},'identity':'rsankarz','credentialKey':'rsankarz','type':'custom'}");
        assertTrue(resp != null);


        resp = clnt.post("Authenticate", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Security'},'identity':'rsankarz', 'password':'rsankarz', 'type':'custom'}");
        assertTrue(resp != null);

        clnt.createUserWithIdentity("Vinay Jaasti", "vjaasti", "vjaastiz", "vjaasti");
        clnt.authenticate("vjaastiz", "vjaasti");

        //check if sessionid is present in header
        resp = clnt.post("AddIdentity", "{'SmartUser':{'___smart_action___':'lookup','___smart_value___':'vjaasti'},'identity':'vjaastiy','credentialKey':'vjaasti','type':'custom'}");
        assertTrue(resp != null);

        resp = clnt.post("ChangePassword", "{'SmartUser':{'___smart_action___':'lookup','___smart_value___':'vjaasti'}, 'identity':'vjaasti','credential':'vjaasti123'}");
       assertTrue(resp != null);
        resp = clnt.post("ChangePassword", "{'SmartUser':{'___smart_action___':'lookup','___smart_value___':'rsankarx'}, 'identity':'rsankarz','credential':'rsankar123','oldcredential':'rsankarz'}");
        assertTrue(resp != null);
        resp = clnt.post("Authenticate", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Security'},'identity':'rsankarz', 'password':'rsankar123', 'type':'custom'}");
        assertTrue(resp != null);

        resp = clnt.logout();

        //shd be an error, not authenticated?
        resp = clnt.post("AddIdentity", "{'SmartUser':{'___smart_action___':'lookup','___smart_value___':'vjaasti'},'identity':'vjaastiy','credentialKey':'vjaasti','type':'custom'}");
        assertTrue(resp != null);

        sserver.stopServer();
    }
}

