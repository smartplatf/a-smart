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
 * File:                org.anon.smart.secure.test.TestSecureClient
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A testing client for security
 *
 * ************************************************************
 * */

package org.anon.smart.secure.test;

import java.io.InputStream;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import static org.junit.Assert.*;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.http.HTTPRequestPData;
import org.anon.smart.smcore.test.TestClient;
import org.anon.smart.smcore.test.AssertJSONResponse;

public class TestSecureClient extends TestClient
{
    private String _sessionId;
    private String _adminSessionId;

    public TestSecureClient()
    {
        super();
    }

    public TestSecureClient(int port, String server, String tenant, String flow, String soa)
    {
        super(port, server, tenant, flow, soa);
    }

    @Override
    protected PData createPData(InputStream istr, String uri)
        throws Exception
    {
        HTTPRequestPData data = (HTTPRequestPData)super.createPData(istr, uri);
        boolean useAdmin = (uri.contains("AdminSmartFlow") && uri.contains("SmartOwner")); //all smart admin flows are on owner
        if ((useAdmin) && (_adminSessionId != null) && (_adminSessionId.length() > 0))
            data.addHeader("Session-Id", _adminSessionId);

        if ((!useAdmin) && (_sessionId != null) && (_sessionId.length() > 0))
            data.addHeader("Session-Id", _sessionId);
        return data;
    }

    public AssertJSONResponse createUserWithIdentity(String username, String user, String identity, String password)
        throws Exception
    {
        AssertJSONResponse resp = post("CreateUser", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Security'},'id':'" + user + "','name':'" + username + "'}");
        assertTrue(resp != null);
        JSONArray arr = resp.getAllResponses();
        assertTrue(arr.size() > 0);
        JSONObject o = (JSONObject)arr.get(0);
        String v = (String)o.get("message");
        assertTrue(v != null);
        assertTrue(v.startsWith("Created a user for") || v.startsWith("A user already exists for:"));

        if (v.startsWith("Created a user for"))
        {
            resp = post("AddIdentity", "{'SmartUser':{'___smart_action___':'lookup','___smart_value___':'rsankarx'},'identity':'" + identity + "','credentialKey':'" + password + "','type':'custom'}");
            assertTrue(resp != null);
            resp.assertStringValue("message", "Added identity: " + identity);
        }

        return resp;
    }

    public AssertJSONResponse authenticate(String identity, String password)
        throws Exception
    {
        return authenticate(identity, password, false);
    }

    public AssertJSONResponse authenticate(String identity, String password, boolean admin)
        throws Exception
    {
        AssertJSONResponse resp = post("Authenticate", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Security'},'identity':'" +identity + "', 'password':'" + password + "', 'type':'custom'}");
        assertTrue(resp != null);

        JSONArray resparr = resp.getAllResponses();
        assertTrue(resparr != null);
        assertTrue(resparr.size() > 0);
        JSONObject o = (JSONObject)resparr.get(0);
        String v = (String)o.get("sessionId");
        assertTrue(v != null);

        if (admin)
            _adminSessionId = v;
        else
            _sessionId = v;

        return resp;
    }

    public AssertJSONResponse authenticateAdmin(String identity, String password)
        throws Exception
    {
        AssertJSONResponse resp = postTo(_port, _server, "/SmartOwner/Security/Authenticate", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Security'},'identity':'" + identity + "', 'password':'" + password + "', 'type':'custom'}", true);
        assertTrue(resp != null);

        JSONArray resparr = resp.getAllResponses();
        assertTrue(resparr != null);
        assertTrue(resparr.size() > 0);
        JSONObject o = (JSONObject)resparr.get(0);
        String v = (String)o.get("sessionId");
        assertTrue(v != null);
        _adminSessionId = v;
        return resp;
    }

    public AssertJSONResponse logout()
        throws Exception
    {
        AssertJSONResponse resp = post("Logout", "{'Session':{'___smart_action___':'lookup', '___smart_value___':'" + _sessionId + "','___key_type___':'java.util.UUID'}}");
        assertTrue(resp != null);
        return resp;
    }
}

