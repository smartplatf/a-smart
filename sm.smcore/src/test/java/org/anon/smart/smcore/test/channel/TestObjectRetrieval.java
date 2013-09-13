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
 * File:                org.anon.smart.smcore.test.channel.TestObjectRetrieval
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Aug 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.channel;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.smcore.test.AssertJSONResponse;
import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.smart.smcore.test.TestClient;
import org.junit.Test;

public class TestObjectRetrieval
{
    @Test
    public void testListings()
        throws Exception
    {
        int port = 9084;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        SCShell shell = new SCShell();
        String home = System.getenv("HOME");
        String tenant = "ListTenant";//UUID.randomUUID().toString();
        TestClient clnt = new TestClient(port, "localhost", tenant, "RegistrationFlow", "RegistrationFlow.soa");
        clnt.deployFromSampleJar();
        clnt.createTenant();
        
        for (int i = 0; i < 6; i++)
        {
            clnt.post("RegisterEvent", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'email':'vjaasti" + i + "@gmail.com',  'password':[\"91\", \"92\"], 'salary':'20000'}");
        }
        
        System.out.println("Ended Registration------------------------");

        
        System.out.println("Running GetListings event.................");
        
        AssertJSONResponse r =  clnt.post("GetListings",  "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'listingsPerPage':5, 'pageNum':1}");
        assertTrue(r != null);
        List responses = ((List)r.getjson().get("responses"));
        Map sr = (Map)responses.get(0);
        List resultSet = (List)sr.get("resultSet");
//        assertTrue(resultSet.size() == 5);
        
        r =  clnt.post("GetListings",  "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'listingsPerPage':5 , 'pageNum':2}");
        assertTrue(r != null);
        responses = ((List)r.getjson().get("responses"));
        sr = (Map)responses.get(0);
        resultSet = (List)sr.get("resultSet");
 //       assertTrue(resultSet.size() == 1);
        
        r =  clnt.post("GetListings",  "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'listingsPerPage':5, 'pageNum':3}");
        assertTrue(r != null);
        responses = ((List)r.getjson().get("responses"));
        sr = (Map)responses.get(0);
        resultSet = (List)sr.get("resultSet");
  //      assertTrue(resultSet.size() == 0);
        
        clnt.post("RegisterEvent", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'email':'vjaasti6@gmail.com',  'password':[\"91\", \"92\"], 'salary':'20000'}");        
        
        r =  clnt.post("GetListings",  "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'listingsPerPage':7, 'pageNum':1}");
        assertTrue(r != null);
        responses = ((List)r.getjson().get("responses"));
        sr = (Map)responses.get(0);
        resultSet = (List)sr.get("resultSet");
   //     assertTrue(resultSet.size() == 7);
        
    }

    //@Test
    public void testPaginationPerformace() throws Exception
    {
        int port = 9084;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.channel.RunSmartServer");
        SCShell shell = new SCShell();
        String home = System.getenv("HOME");
        String tenant = "PerfTenant";//UUID.randomUUID().toString();
        TestClient clnt = new TestClient(port, "localhost", tenant, "RegistrationFlow", "RegistrationFlow.soa");
        clnt.deployFromSampleJar();
        clnt.createTenant();
        Long t1 = System.currentTimeMillis();
        /*for (int i = 0; i < 400; i++)
        {
            clnt.post("RegisterEvent", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'email':'vjaasti" + i + "@gmail.com',  'password':[\"91\", \"92\"], 'salary':'20000'}");
        }*/
        Long t2 = System.currentTimeMillis();
        
        System.out.println("Ended Registration------------------------");
        System.out.println("600 objects creation took :"+(t2 - t1)+ " ms");
        
        System.out.println("Running GetListings event.................");
        
        t1 = System.currentTimeMillis();
        AssertJSONResponse r =  clnt.post("GetListings",  "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'listingsPerPage':25, 'pageNum':1,'freshListing':'true'}");
        t2 = System.currentTimeMillis();
        System.out.println("pagination with 600 obj took :"+(t2 - t1)+ " ms");
        assertTrue(r != null);
        List responses = ((List)r.getjson().get("responses"));
        Map sr = (Map)responses.get(0);
        List resultSet = (List)sr.get("resultSet");
        //assertTrue(resultSet.size() == 25);

        t1 = System.currentTimeMillis();
        r =  clnt.post("GetListings",  "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'listingsPerPage':25, 'pageNum':5, 'freshListing':'false'}");
        t2 = System.currentTimeMillis();
        assertTrue(r != null);
        responses = ((List)r.getjson().get("responses"));
        sr = (Map)responses.get(0);
        resultSet = (List)sr.get("resultSet");
        //assertTrue(resultSet.size() == 1);
        System.out.println("pagination with 600 obj took :"+(t2 - t1)+ " ms");
        
        
        t1 = System.currentTimeMillis();
        r =  clnt.post("GetListings",  "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'RegistrationFlow'}, 'group':'Registration', 'listingsPerPage':25, 'pageNum':5, 'freshListing':'false'}");
        t2 = System.currentTimeMillis();
        assertTrue(r != null);
        responses = ((List)r.getjson().get("responses"));
        sr = (Map)responses.get(0);
        resultSet = (List)sr.get("resultSet");
        //assertTrue(resultSet.size() == 1);
        System.out.println("pagination with 600 obj took :"+(t2 - t1)+ " ms");
    }

}
