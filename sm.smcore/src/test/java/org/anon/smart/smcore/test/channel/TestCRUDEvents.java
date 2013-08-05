/**
 * Utilities - Utilities used by anon
 *
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of Utilities.
 *
 * Utilities is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Utilities is distributed in the hope that it will be useful,
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
 * File:                org.anon.smart.smcore.test.channel.TestCRUDEvents
 * Author:              rsankar
 * Revision:            1.0
 * Date:                03-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test case to test crud events
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.channel;

import java.io.ByteArrayInputStream;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.data.PData;

import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.smart.smcore.test.TestClient;
import org.anon.smart.smcore.test.AssertJSONResponse;

import org.anon.utilities.anatomy.CrossLinkApplication;

public class TestCRUDEvents
{
    /*
    private HTTPClientChannel postTo(SCShell shell, int port, String server, String uri, String post, boolean wait)
        throws Exception
    {
        Rectifier rr = new Rectifier();
        rr.addStep(new TestDistillation());
        HTTPConfig ccfg = new HTTPConfig(port, false);
        ccfg.setClient();
        ccfg.setServer(server);
        ccfg.setRectifierInstinct(rr, new TestDataFactory());
        HTTPClientChannel cchnl = (HTTPClientChannel)shell.addChannel(ccfg);
        cchnl.connect();
        ByteArrayInputStream istr = new ByteArrayInputStream(post.getBytes());
        PData d = new TestPData(null, new ContentData(istr));
        cchnl.post(uri, new PData[] { d });
        if (wait)
        {
            Thread.sleep(3000);
            cchnl.disconnect();
        }

        return cchnl;
    }
    */

    @Test
    public void testTestCRUDEvents()
        throws Exception
    {
        int port = 9081;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        //Thread.sleep(3000);
        //SCShell shell = new SCShell();
        TestClient clnt = new TestClient(port, "localhost", "errortenant", "ErrorCases", "ErrorCases.soa");
        clnt.deployFromSampleJar();
        clnt.createTenant();

        AssertJSONResponse resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ErrorCases'}, 'create':'ErrorObject', 'data':{'name':'ohGod','embed':{'_start':'04/04/2013 10:30'}}}");
        assertTrue(resp != null);

        //check error
        resp = clnt.post("ExceptionEvent", "{'ErrorObject':{'___smart_action___':'lookup', '___smart_value___':'ohGod'}, 'test':'ErrorObject1'}");
        assertTrue(resp != null);

        resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ErrorCases'}, 'create':'AdditionalError', 'data':{'name':'addedsome'}}");
        assertTrue(resp != null);

        //for throwexception
        resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ErrorCases'}, 'create':'ErrorObject', 'data':{'name':'throwexception','embed':{'_start':'04/04/2013 10:00'}}}");
        assertTrue(resp != null);

        resp = clnt.post("ExceptionEvent", "{'ErrorObject':{'___smart_action___':'lookup', '___smart_value___':'throwexception'}, 'test':'ErrorObject2'}");
        assertTrue(resp != null);

        /*
        String home = System.getenv("HOME");
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'ErrorCases.soa'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'errortenant','enableFlow':'ErrorCases','enableFeatures':['all']}", false);
        Thread.sleep(6000);

        postTo(shell, port, "localhost", "/errortenant/ErrorCases/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ErrorCases'}, 'create':'ErrorObject', 'data':{'name':'ohGod','embed':{'_start':'04/04/2013 10:30'}}}", true);
        //check error
        postTo(shell, port, "localhost", "/errortenant/ErrorCases/ExceptionEvent", "{'ErrorObject':{'___smart_action___':'lookup', '___smart_value___':'ohGod'}, 'test':'ErrorObject1'}", true);
        postTo(shell, port, "localhost", "/errortenant/ErrorCases/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ErrorCases'}, 'create':'AdditionalError', 'data':{'name':'addedsome'}}", true);
        //for throwexception
        postTo(shell, port, "localhost", "/errortenant/ErrorCases/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ErrorCases'}, 'create':'ErrorObject', 'data':{'name':'throwexception','embed':{'_start':'04/04/2013 10:00'}}}", true);
        postTo(shell, port, "localhost", "/errortenant/ErrorCases/ExceptionEvent", "{'ErrorObject':{'___smart_action___':'lookup', '___smart_value___':'throwexception'}, 'test':'ErrorObject2'}", true);
        */
        utils.stopServer();
    }
}

