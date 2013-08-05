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
 * File:                org.anon.smart.smcore.test.channel.TestSmartChannel
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test for smart channels
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

public class TestSmartChannel
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
            Thread.sleep(3000); //response shd have come within 3s
            cchnl.disconnect();
        }
        return cchnl;
    }
    */

    @Test
    public void testTestSmartChannel()
        throws Exception
    {
        int port = 9080;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");

        TestClient clnt = new TestClient();
        AssertJSONResponse resp = clnt.postTo(port, "localhost", "/invalidtenant", "", false);
        //assertTrue(resp != null);
        //resp.assertHasErrors();
        resp = clnt.postTo(port, "localhost", "/invalidtenant/invalidflow/invalidevent", "{'order':'testorder'}", true);
        assertTrue(resp != null);
        resp.assertHasErrors();
        resp = clnt.postTo(port, "localhost", "/invalidtenant/invalidflow/invalidevent", "{'order':'testorder'}", true);
        assertTrue(resp != null);
        resp.assertHasErrors();
        resp = clnt.postTo(port, "localhost", "/SmartOwner/invalidflow/", "{'order':'testorder'}", true);
        assertTrue(resp != null);
        resp.assertHasErrors();

        clnt = new TestClient(port, "localhost", "coreanon", "ReviewFlow", "ReviewFlow.soa");
        resp = clnt.post("WriteReview", "{'ReviewObject':{'___smart_action___':'lookup', '___smart_value___':'Not present'}, 'review':'Reviewed','rating':1}");
        assertTrue(resp != null);
        resp.assertHasErrors();

        resp = clnt.post("WriteReview", "{'ReviewObject':'Object1', 'review':'Reviewed','rating':1}");
        assertTrue(resp != null);
        resp.assertHasErrors();

        resp = clnt.post("WriteReview", "{'ReviewObject':{'___smart_action___':'lookup', '___smart_value___':'Object1'}, 'review':'Reviewed','rating':1}");
        assertTrue(resp != null);

        clnt = new TestClient(port, "localhost", "newtenant", "RegistrationFlow", "RegistrationFlow.soa");
        clnt.deployFromSampleJar();
        clnt.createTenant();

        /*
        SCShell shell = new SCShell();
        postTo(shell, port, "localhost", "/invalidtenant", "", true);
        postTo(shell, port, "localhost", "/invalidtenant/invalidflow/invalidevent", "{'order':'testorder'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/invalidflow/", "{'order':'testorder'}", true);
        postTo(shell, port, "localhost", "/coreanon/ReviewFlow/WriteReview", "{'ReviewObject':{'___smart_action___':'lookup', '___smart_value___':'Not present'}, 'review':'Reviewed','rating':1}", true);
        postTo(shell, port, "localhost", "/coreanon/ReviewFlow/WriteReview", "{'ReviewObject':'Object1', 'review':'Reviewed','rating':1}", true); //error
        postTo(shell, port, "localhost", "/coreanon/ReviewFlow/WriteReview", "{'ReviewObject':{'___smart_action___':'lookup', '___smart_value___':'Object1'}, 'review':'Reviewed','rating':1}", true);
        String home = System.getenv("HOME");
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'RegistrationFlow.soa'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'newtenant','enableFlow':'RegistrationFlow','enableFeatures':['all']}", false);
        Thread.sleep(6000);//tenant creation takes time
        */
        for (int i = 0; i < 10; i++)
        {
            clnt.postTo(port, "localhost", "/newtenant/RegistrationFlow/RegisterEvent", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'email':'rsankarx" + i + "@gmail.com'}", false);
        }
        Thread.sleep(10000);
        utils.stopServer();
    }
}

