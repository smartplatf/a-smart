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
 * File:                org.anon.smart.smcore.test.config.TestConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of test cases for config
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.config;

import java.io.ByteArrayInputStream;
import java.net.URL;
import org.junit.Test;

import net.sf.json.JSON;
import static org.junit.Assert.*;

import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.data.PData;

import org.anon.smart.smcore.test.TestClient;
import org.anon.smart.smcore.test.channel.TestDataFactory;
import org.anon.smart.smcore.test.channel.TestDistillation;
import org.anon.smart.smcore.test.channel.TestDScope;
import org.anon.smart.smcore.test.channel.TestPData;
import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.smart.smcore.test.AssertJSONResponse;

import org.anon.utilities.anatomy.CrossLinkApplication;

public class TestConfig
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
    public void testTestConfig()
        throws Exception
    {
        int port = 9090;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.config.ConfigServer");
        //Thread.sleep(3000);
        SCShell shell = new SCShell();
        String home = System.getenv("HOME");

        TestClient clnt = new TestClient(port, "localhost", "configtenant", "ConfigCases", "ConfigCases.soa");
        clnt.deployFromSampleJar();
        clnt.createTenant();

        AssertJSONResponse resp = clnt.post("ConfigFlow", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ConfigCases'}, 'configName':'ConfigObject', 'configValues':{'oneConfig':1,'twoConfig':2,'oneString':'configstr'}}");
        assertTrue(resp != null);

        resp = clnt.post("ConfigFlow", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ConfigCases'}, 'configName':'ConfigObject', 'configFor':'DataObject', 'configValues':{'oneConfig':1,'twoConfig':2,'oneString':'configstr'}}");
        assertTrue(resp != null);

        resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ConfigCases'}, 'create':'DataObject', 'data':{'dataKey':'ohGod'}}");
        assertTrue(resp != null);

        resp = clnt.post("ConfigParmEvent", "{'DataObject':{'___smart_action___':'lookup', '___smart_value___':'ohGod'}, 'send':'something'}");
        assertTrue(resp != null);
        

        /*
        AssertJSONResponse resp = clnt.postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'ConfigCases.soa'}", true);
        assertTrue(resp != null);
        resp.assertStringStartsWith("success", "Done");

        resp = clnt.postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'configtenant','enableFlow':'ConfigCases','enableFeatures':['all']}", true);
        assertTrue(resp != null);
        resp.assertStringStartsWith("success", "Done");

        resp = clnt.postTo(port, "localhost", "/configtenant/ConfigCases/ConfigFlow", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ConfigCases'}, 'configName':'ConfigObject', 'configValues':{'oneConfig':1,'twoConfig':2,'oneString':'configstr'}}", true);
        assertTrue(resp != null);

        resp = clnt.postTo(port, "localhost", "/configtenant/ConfigCases/ConfigFlow", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ConfigCases'}, 'configName':'ConfigObject', 'configFor':'DataObject', 'configValues':{'oneConfig':1,'twoConfig':2,'oneString':'configstr'}}", true);
        assertTrue(resp != null);

        resp = clnt.postTo(port, "localhost", "/configtenant/ConfigCases/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ConfigCases'}, 'create':'DataObject', 'data':{'dataKey':'ohGod'}}", true);
        assertTrue(resp != null);

        resp = clnt.postTo(port, "localhost", "/configtenant/ConfigCases/ConfigParmEvent", "{'DataObject':{'___smart_action___':'lookup', '___smart_value___':'ohGod'}, 'send':'something'}", true);
        assertTrue(resp != null);
        */

        utils.stopServer();
    }
}

