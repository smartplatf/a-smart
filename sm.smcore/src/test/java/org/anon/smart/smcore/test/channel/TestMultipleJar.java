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
 * File:                org.anon.smart.smcore.test.channel.TestMultipleJar
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of test cases to ensure that multiple jars for multiple tenants work
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

public class TestMultipleJar
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
    public void testTestMultipleJar()
        throws Exception
    {
        int port = 9082;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        //Thread.sleep(3000);

        String home = System.getenv("HOME");
        String jarFile1 = home + "/.m2/repository/org/anon/sampleapp/testapp/1.0/testapp-1.0.jar";
        String jarFile2 = home + "/.m2/repository/org/anon/sampleapp/testapp2/1.0/testapp2-1.0.jar";

        TestClient clnt = new TestClient(port, "localhost", "testapptenant", "TestApp", "TestApp.soa");
        clnt.deployJar(port,jarFile1, "TestApp.soa");
        clnt.deployJar(port,jarFile2, "TestApp2.soa");
        clnt.createTenant();
        TestClient clnt1 = new TestClient(port, "localhost", "testapptenant2", "TestApp2", "TestApp2.soa");
        clnt1.createTenant();

        AssertJSONResponse resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'TestApp'}, 'create':'TestData', 'data':{'myKey':'testapp1','something':5}}");
        assertTrue(resp != null);

        resp = clnt1.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'TestApp2'}, 'create':'TestData', 'data':{'myKey':'testapp2','something':5,'another':'trying'}}");
        assertTrue(resp != null);

        utils.stopServer();

        /*SCShell shell = new SCShell();
        String home = System.getenv("HOME");
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/privategithub/p-sampleapp2/testapp.jar','flowsoa':'TestApp.soa'}", true);

        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/privategithub/p-sampleapp2/testapp2.jar','flowsoa':'TestApp2.soa'}", true);

        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'testapptenant','enableFlow':'TestApp','enableFeatures':['all']}", false);
        Thread.sleep(6000);

        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'testapptenant2','enableFlow':'TestApp2','enableFeatures':['all']}", false);
        Thread.sleep(6000);

        postTo(shell, port, "localhost", "/testapptenant/TestApp/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'TestApp'}, 'create':'TestData', 'data':{'myKey':'testapp1','something':5}}", true);

        postTo(shell, port, "localhost", "/testapptenant2/TestApp2/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'TestApp2'}, 'create':'TestData', 'data':{'myKey':'testapp2','something':5,'another':'trying'}}", true);
        */
    }
}

