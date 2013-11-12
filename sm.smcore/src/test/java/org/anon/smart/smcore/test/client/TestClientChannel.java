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
 * File:                org.anon.smart.smcore.test.client.TestClientChannel
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test to test client channel for get
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.client;

import org.junit.Test;
import static org.junit.Assert.*;

import org.anon.smart.smcore.test.TestClient;

public class TestClientChannel
{
    @Test
    public void testTestClientChannel()
        throws Exception
    {
        TestClient clnt = new TestClient(80, "netty.io", "errortenant", "ErrorCases", "ErrorCases.soa");
        //clnt.get(80, "dndopen.dove-sms.com", "/TransSMS/SMSAPI.jsp?username=RENTONGO&password=RentalSMS&sendername=INFOIN&mobileno=919902530998&message=Hello", true);
    }
}

