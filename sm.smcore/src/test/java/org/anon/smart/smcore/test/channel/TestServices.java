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
 * File:                org.anon.smart.smcore.test.channel.TestServices
 * Author:              rsankar
 * Revision:            1.0
 * Date:                20-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test to test transition services
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.channel;

import org.junit.Test;
import static org.junit.Assert.*;

import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.smart.smcore.test.TestClient;
import org.anon.smart.smcore.test.AssertJSONResponse;

public class TestServices
{
    @Test
    public void testTestServices()
        throws Exception
    {
        int port = 9099;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        TestClient clnt = new TestClient(port, "localhost", "svctenant", "Services", "Services.soa");
        clnt.deployFromSampleJar();
        AssertJSONResponse resp = clnt.postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'svctenant','enableFlow':'Services','enableFeatures':['all'], 'controlsAdmin':'YES'}", true);
        assertTrue(resp != null);
        resp.assertStringStartsWith("success", "Done");
        //clnt.createTenant();
        resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Services'}, 'create':'SvcTenant', 'data':{'name':'convert2','domain':'', 'clientOf':'raji'}}");
        assertTrue(resp != null);

        resp = clnt.post("ConvertTenant", "{'SvcTenant':{'___smart_action___':'lookup', '___smart_value___':'convert2'}, 'enableFlow':'Services', 'features':['all']}");
        assertTrue(resp != null);

        Thread.currentThread().sleep(10000); //wait to see that the tenant has been created

        utils.stopServer();
    }
}

