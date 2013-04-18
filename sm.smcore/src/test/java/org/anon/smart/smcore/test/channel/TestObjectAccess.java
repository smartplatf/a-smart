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
 * File:                org.anon.smart.smcore.test.channel.TestSearchEvent
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 3, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
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

import org.anon.utilities.anatomy.CrossLinkApplication;

public class TestObjectAccess
{
    private void postTo(SCShell shell, int port, String server, String uri, String post)
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
        cchnl.disconnect();
    }

    @Test
    public void dummyTest()
    {
	assertTrue(1==1);
    }

    //@Test
    public void testTestSearchEvent()
        throws Exception
    {
        int port = 9084;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        SCShell shell = new SCShell();
        Thread.sleep(10000);
        String home = System.getenv("HOME");
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", 
        		"{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, " +
        		"'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'RegistrationFlow.soa'}");
        System.out.println("Zzzzzzzz 1 Min after deploy------------------------");
        Thread.sleep(10000); //response shd have come within 3s
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'newtenant','enableFlow':'RegistrationFlow','enableFeatures':['all']}");
        System.out.println("Zzzzzzzz 1 Min after Tenant Creation-----------------------");
        Thread.sleep(10000);
        
        for (int i = 0; i < 5; i++)
        {
            postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/RegisterEvent", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'email':'rsankarx" + i + "@gmail.com'}");
        }
        System.out.println("Zzzzzzzz 1 Min after Registration----------------------");
        Thread.sleep(10000);
        /*
        System.out.println("Ended Registration------------------------");
       
        System.out.println("Running lookup event.................");
        //Lookup
        postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/LookupEvent",
        		"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'key':'rsankarx0@gmail.com'}");
        System.out.println("Zzzzzzzz 1 Min after lookup----------------------");
        Thread.sleep(10000);
        */
        
        /*
        System.out.println("Running search event.................");
        //Search
        postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/SearchEvent",
        		"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'queryMap':{'email':'rsankarx0@gmail.com'}}");
        System.out.println("Zzzzzzzz 1 Min after Search----------------------");
        
        Thread.sleep(60000);
        */
        /*System.out.println("Running listall event.................");
        //Search
        postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/ListAllEvent",
        		"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'size':4}");
        System.out.println("Zzzzzzzz 1 Min after List All----------------------");
        
        Thread.sleep(10000);
        
        System.out.println("Running listall event AGAIN.................");
        //Search
        postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/ListAllEvent",
        		"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'size':4}");
        System.out.println("Zzzzzzzz 1 Min after List All----------------------");
        
        Thread.sleep(10000);*/
        
        System.out.println("GETTING COUNTERS.................");
        //Search
        postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/MetricAccess",
        		"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'objName':'Registration'}");
        System.out.println("Zzzzzzzz 1 Min after List All----------------------");
        
        Thread.sleep(10000);
        utils.stopServer();
    }
    
    @Test
    public void testSurveyDemo()
        throws Exception
    {
        int port = 9084;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        SCShell shell = new SCShell();
        Thread.sleep(6000);
        String home = System.getenv("HOME");
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", 
        		"{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, " +
        		"'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'RegistrationFlow.soa'}");
        System.out.println("Zzzzzzzz 1 Min after deploy------------------------");
        Thread.sleep(10000); //response shd have come within 3s
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'newtenant','enableFlow':'RegistrationFlow','enableFeatures':['all']}");
        System.out.println("Zzzzzzzz 1 Min after Tenant Creation-----------------------");
        Thread.sleep(10000);
        
        for (int i = 0; i < 5; i++)
        {
            postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/RegisterEvent", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'email':'rsankarx" + i + "@gmail.com'}");
        }
        System.out.println("Zzzzzzzz 1 Min after Registration----------------------");
        Thread.sleep(10000);
        System.out.println("Ended Registration------------------------");
       
        System.out.println("Running lookup event.................");
        
        System.out.println("GETTING COUNTERS.................");
        //Search
        postTo(shell, port, "localhost", "/newtenant/RegistrationFlow/MetricAccess",
        		"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'objName':'Registration'}");
        System.out.println("Zzzzzzzz 1 Min after List All----------------------");
        
        Thread.sleep(10000);
        utils.stopServer();
    }

}
