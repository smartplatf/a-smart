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
 * File:                org.anon.smart.smcore.test.channel.TestUpdateEvent
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 13, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.channel;

import static org.anon.utilities.services.ServiceLocator.logger;
import static org.anon.utilities.services.ServiceLocator.perf;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import org.anon.sampleapp.register.Registration;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.logger.Logger;
import org.anon.utilities.reflect.DirtyFieldTraversal;
import org.anon.utilities.test.reflect.ComplexTestObject;
import org.anon.utilities.test.reflect.SimpleOT;
import org.junit.Test;

import org.anon.smart.smcore.test.TestClient;
import org.anon.smart.smcore.test.AssertJSONResponse;

public class TestUpdateEvent {

    /*
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
        */

	    @Test
	    public void dummyTest()
	    {
		assertTrue(1==1);
	    }

	    @Test
	    public void testUpdateEvent()
	        throws Exception
	    {

	        int port = 9085;
	        CoreServerUtilities utils = new CoreServerUtilities(port);
	        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
	     //   SCShell shell = new SCShell();
	        //Thread.sleep(3000);
	        String home = System.getenv("HOME");

            TestClient clnt = new TestClient(port, "localhost", "newtenant", "RegistrationFlow", "RegistrationFlow.soa");
            clnt.deployFromSampleJar();
            clnt.createTenant();

	        
            AssertJSONResponse resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'create':'Registration', 'data':{'email':'vjaasti@gmail.com', 'phone':'+919972532247', 'tags':['user','admin'],'profile':{'name':'profileName','workRecord':'bt'}}}");
            assertTrue(resp != null);
            
	        /*System.out.println("Zzzzzzzz 1 Min after Registration----------------------");
	        Thread.sleep(10000);
	        System.out.println("Ended Registration------------------------");
            */
	       
	        System.out.println("Posting update event.................");
	        //update registration
	        /*resp = clnt.post("UpdateRegistration",
	        		"{'Registration':{'___smart_action___':'lookup', '___smart_value___':'vjaasti@gmail.com'}, 'phone':'+918000000000'}");
	        assertTrue(resp != null); 
	        System.out.println("Sleeping for 10 Sec");
	        Thread.sleep(10 * 1000);
	        */resp = clnt.post("UpdatePrime",
			"{'Registration':{'___smart_action___':'lookup', '___smart_value___':'vjaasti@gmail.com'},'update':'Registration','data':{'phone':'32247','tags':['user','manager'],'profile':{'workRecord':'smart'}}}");
            assertTrue(resp != null);
	        
	        //Lookup
	        resp = clnt.post("LookupEvent",
	        		"{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'key':'vjaasti@gmail.com'}");
            assertTrue(resp != null);
	        
	        //System.out.println("");
	        utils.stopServer();
	    }
	    
	   //@Test
	    public void testDirtyFieldTraversal() throws Exception
	    {
//	    	Registration o1 = new Registration("vinay");
//	    	Registration o2 = new Registration("jaasti", "32247");
	    	ComplexTestObject o1 = new ComplexTestObject();
	    	ComplexTestObject o2 = new ComplexTestObject();
	            SimpleOT visit = new SimpleOT();
	            DirtyFieldTraversal dtraverse = new DirtyFieldTraversal(visit, o1, o2, o2, false);
	            dtraverse.traverse();
	    }
}
