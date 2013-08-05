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
 * File:                org.anon.smart.smcore.test.AssertJSONResponse
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A response that has assert functions for json
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import static org.junit.Assert.*;

public class AssertJSONResponse
{
    private JSONObject _jobject;
    private JSONArray _responses;
    private JSONArray _errors;

    public AssertJSONResponse(JSON j)
    {
        assertTrue(j != null);
        assertTrue(j instanceof JSONObject);
        _jobject = (JSONObject)j;
        _responses = (JSONArray)_jobject.get("responses");
        _errors = (JSONArray)_jobject.get("errors");
    }

    public JSONObject getjson() { return _jobject; }
    public JSONArray getAllResponses() { return _responses; }

    public void assertStringValue(String fld, String val)
        throws Exception
    {
        assertStringValue(0, fld, val);
    }

    public void assertStringValue(int ind, String fld, String val)
        throws Exception
    {
        assertTrue(_responses != null);
        assertTrue(_responses.size() > ind);
        JSONObject o = (JSONObject)_responses.get(ind);
        String v = (String)o.get(fld);
        assertTrue(v != null);
        assertTrue(v.equals(val));
    }

    public void assertStringStartsWith(String fld, String val)
        throws Exception
    {
        assertStringStartsWith(0, fld, val);
    }

    public void assertStringStartsWith(int ind, String fld, String val)
        throws Exception
    {
        assertTrue(_responses != null);
        assertTrue(_responses.size() > ind);
        JSONObject o = (JSONObject)_responses.get(ind);
        String v = (String)o.get(fld);
        assertTrue(v != null);
        assertTrue(v.startsWith(val));
    }

    public void assertHasResponses()
        throws Exception
    {
        assertTrue(_responses != null);
        assertTrue(_responses.size() > 0);
    }

    public void assertHasErrors()
        throws Exception
    {
        assertTrue(_errors != null);
        assertTrue(_errors.size() > 0);
    }
}

