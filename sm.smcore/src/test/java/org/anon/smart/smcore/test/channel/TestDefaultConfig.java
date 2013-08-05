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
 * File:                org.anon.smart.smcore.test.channel.TestDefaultConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Tests the default config for objects
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

public class TestDefaultConfig
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
    public void testTestDefaultConfig()
        throws Exception
    {
        int port = 9083;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        //Thread.sleep(3000);

        String home = System.getenv("HOME");

        String reviewJar = home + "/.m2/repository/org/anon/sampleapp/testreview/1.0/testreview-1.0.jar";
        TestClient clnt = new TestClient(port, "localhost", "reviewtenant", "ReviewFlow2", "ReviewFlow2.soa");
        clnt.deployJar(port,reviewJar, "ReviewFlow2.soa");
        clnt.createTenant();

        AssertJSONResponse resp = clnt.postTo(port, "localhost", "/reviewtenant/AdminSmartFlow/ListDeployments", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'AdminSmartFlow'}, 'dType':'Event','flow':'ReviewFlow2'}", true);
        assertTrue(resp != null);

        resp = clnt.postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/ListDeployments", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'AdminSmartFlow'}}", true);
        assertTrue(resp != null);

        resp = clnt.postTo(port, "localhost", "/reviewtenant/AdminSmartFlow/ListDeployments", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'AdminSmartFlow'}, 'dType':'PrimeData','flow':'ReviewFlow2'}", true);
        assertTrue(resp != null);

        resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ReviewFlow2'}, 'create':'ReviewMe', 'data':{'_name':'ReviewObject1'}}");
        assertTrue(resp != null);

        resp = clnt.post("ReviewEvent", "{'ReviewMe':{'___smart_action___':'lookup', '___smart_value___':'ReviewObject1'}, 'review':'Reviewed','rating':1}");
        assertTrue(resp != null);
        
        resp = clnt.postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/ListEnabledFlows", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}}", true);
        assertTrue(resp != null);
        resp = clnt.postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/ListEnabledFlows", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'reviewtenant'}}", true);
        assertTrue(resp != null);
        /*
        SCShell shell = new SCShell();
        String home = System.getenv("HOME");
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/privategithub/p-sampleapp2/testreview.jar','flowsoa':'ReviewFlow2.soa'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'reviewtenant','enableFlow':'ReviewFlow2','enableFeatures':['all']}", false);
        Thread.sleep(7000);
        postTo(shell, port, "localhost", "/reviewtenant/AdminSmartFlow/ListDeployments", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'AdminSmartFlow'}, 'dType':'Event','flow':'ReviewFlow2'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/ListDeployments", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'AdminSmartFlow'}}", true);
        postTo(shell, port, "localhost", "/reviewtenant/AdminSmartFlow/ListDeployments", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'AdminSmartFlow'}, 'dType':'PrimeData','flow':'ReviewFlow2'}", true);
        postTo(shell, port, "localhost", "/reviewtenant/ReviewFlow2/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ReviewFlow2'}, 'create':'ReviewMe', 'data':{'_name':'ReviewObject1'}}", true);
        postTo(shell, port, "localhost", "/reviewtenant/ReviewFlow2/ReviewEvent", "{'ReviewMe':{'___smart_action___':'lookup', '___smart_value___':'ReviewObject1'}, 'review':'Reviewed','rating':1}", true);
        */
	 utils.stopServer();
        
    }
}

