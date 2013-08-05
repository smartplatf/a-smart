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
 * File:                org.anon.smart.smcore.test.relations.TestRelation
 * Author:              rsankar
 * Revision:            1.0
 * Date:                17-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test case to test relations
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.relations;

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

import org.anon.smart.smcore.test.channel.TestDataFactory;
import org.anon.smart.smcore.test.channel.TestDistillation;
import org.anon.smart.smcore.test.channel.TestDScope;
import org.anon.smart.smcore.test.channel.TestPData;
import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.smart.smcore.test.TestClient;
import org.anon.smart.smcore.test.AssertJSONResponse;

import org.anon.utilities.anatomy.CrossLinkApplication;

public class TestRelation
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
    public void testTestRelation()
        throws Exception
    {
        int port = 9090;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.config.ConfigServer");
        //Thread.sleep(3000);

        TestClient clnt = new TestClient(port, "localhost", "shopping", "ShoppingCart", "ShoppingCart.soa");
        clnt.deployFromSampleJar();
        clnt.createTenant();

        AssertJSONResponse resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ShoppingCart'}, 'create':'ShoppingCart', 'data':{'cartName':'raji1shopping','totalCost':0.0,'noItems':0}}");
        assertTrue(resp != null);

        resp = clnt.post("AddCartItem", "{'ShoppingCart':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}, 'skuId':'book1','cost':250.10}");
        assertTrue(resp != null);

        resp = clnt.post("GetShoppingCart", "{'ShoppingCart':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}}");
        assertTrue(resp != null);

        /*
        SCShell shell = new SCShell();
        String home = System.getenv("HOME");

        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'ShoppingCart.soa'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'shopping','enableFlow':'ShoppingCart','enableFeatures':['all']}", false);
        Thread.sleep(7000);

        postTo(shell, port, "localhost", "/shopping/ShoppingCart/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'ShoppingCart'}, 'create':'ShoppingCart', 'data':{'cartName':'raji1shopping','totalCost':0.0,'noItems':0}}", true);
        postTo(shell, port, "localhost", "/shopping/ShoppingCart/AddCartItem", "{'ShoppingCart':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}, 'skuId':'book1','cost':250.10}", true);
        postTo(shell, port, "localhost", "/shopping/ShoppingCart/GetShoppingCart", "{'ShoppingCart':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}}", true);
        Thread.sleep(3000);
        */
        utils.stopServer();
    }

    @Test
    public void testTestMashup()
        throws Exception
    {
        int port = 9091;
        CoreServerUtilities utils = new CoreServerUtilities(port);
        utils.runServer("org.anon.smart.smcore.test.config.ConfigServer");
        //Thread.sleep(3000);

        TestClient clnt = new TestClient(port, "localhost", "mshop", "MShop", "MShop.soa");
        TestClient clnt1 = new TestClient(port, "localhost", "mshop", "Catalogue", "Catalogue.soa");
        clnt.deployFromSampleJar();
        clnt1.deployFromSampleJar();
        clnt1.createTenant();

        AssertJSONResponse resp = clnt.postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/EnableFlow", "{'TenantAdmin':{'___smart_action___':'lookup','___smart_value___':'SmartOwner'}, 'tenant':'mshop','enableFeatures':[ 'all' ],'enableFlow':'MShop', 'links':[{'name':'itemlink', 'flow':'Catalogue', 'object':'CatalogueItem','attribute':'skuID'}]}", true);
        assertTrue(resp != null);

        resp = clnt1.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Catalogue'}, 'create':'CatalogueItem', 'data':{'skuID':'raji1sku','itemName':'RajiItem','itemDescription':'TestingItem','itemCost':10.0,'itemQuantity':5}}");
        assertTrue(resp != null);

        resp = clnt.post("CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'MShop'}, 'create':'MShop', 'data':{'cartName':'raji1shopping','totalCost':0.0,'noItems':0}}");
        assertTrue(resp != null);

        resp = clnt.post("AddShopItem", "{'MShop':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}, 'shopItem':'raji1sku','cost':250.10}");
        assertTrue(resp != null);

        /*
        SCShell shell = new SCShell();
        String home = System.getenv("HOME");

        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'MShop.soa'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/DeployEvent", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar','flowsoa':'Catalogue.soa'}", true);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'mshop','enableFlow':'Catalogue','enableFeatures':['all']}", false);
        Thread.sleep(7000);
        postTo(shell, port, "localhost", "/SmartOwner/AdminSmartFlow/EnableFlow", "{'TenantAdmin':{'___smart_action___':'lookup','___smart_value___':'SmartOwner'}, 'tenant':'mshop','enableFeatures':[ 'all' ],'enableFlow':'MShop', 'links':[{'name':'itemlink', 'flow':'Catalogue', 'object':'CatalogueItem','attribute':'skuID'}]}", false);

        postTo(shell, port, "localhost", "/mshop/Catalogue/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'Catalogue'}, 'create':'CatalogueItem', 'data':{'skuID':'raji1sku','itemName':'RajiItem','itemDescription':'TestingItem','itemCost':10.0,'itemQuantity':5}}", true);
        postTo(shell, port, "localhost", "/mshop/MShop/CreatePrime", "{'FlowAdmin':{'___smart_action___':'lookup', '___smart_value___':'MShop'}, 'create':'MShop', 'data':{'cartName':'raji1shopping','totalCost':0.0,'noItems':0}}", true);
        //postTo(shell, port, "localhost", "/mshop/MShop/AddShopItem", "{'MShop':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}, 'shopItem':{'___smart_action___':'lookup','___smart_value___':'raji1sku','___smart_flow___':'Catalogue','___key_type___':'CatalogueItem'},'cost':250.10}", true);
        postTo(shell, port, "localhost", "/mshop/MShop/AddShopItem", "{'MShop':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}, 'shopItem':'raji1sku','cost':250.10}", true);
        //postTo(shell, port, "localhost", "/shopping/ShoppingCart/GetShoppingCart", "{'ShoppingCart':{'___smart_action___':'lookup', '___smart_value___':'raji1shopping'}}", true);
        Thread.sleep(3000);
        */
        utils.stopServer();
    }
}

