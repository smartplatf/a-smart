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
 * File:                org.anon.smart.smcore.test.TestClient
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A post and return from server
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.sf.json.JSONSerializer;
import net.sf.json.JSON;

import static org.junit.Assert.*;

import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.smcore.test.channel.TestPData;
import org.anon.smart.smcore.test.channel.TestDataFactory;

public class TestClient
{
    private SCShell _shell;
    protected String _server;
    private String _tenant;
    private String _flow;
    private String _soa;
    protected int _port;

    public TestClient(int port, String server, String tenant, String flow, String soa)
    {
        _port = port;
        _tenant = tenant;
        _flow = flow;
        _server = server;
        _soa = soa;
        _shell = new SCShell();
    }

    public TestClient()
    {
        _shell = new SCShell();
    }

    public AssertJSONResponse createTenant()
        throws Exception
    {
        return createTenant(_port, _tenant, _flow);
    }

    public AssertJSONResponse deployFromSampleJar()
        throws Exception
    {
        return deployFromSampleJar(_port, _soa);
    }

    public AssertJSONResponse post(String event, String post)
        throws Exception
    {
        return postTo(_port, _tenant, _flow, event, post);
    }

    public AssertJSONResponse createTenant(int port, String tenant, String enable)
        throws Exception
    {
        AssertJSONResponse resp = postTo(port, "localhost", "/SmartOwner/AdminSmartFlow/NewTenant", "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'tenant':'" + tenant + "','enableFlow':'" + enable + "','enableFeatures':['all']}", true);
        assertTrue(resp != null);
        resp.assertStringStartsWith("success", "Done");
        return resp;
    }

    public AssertJSONResponse deployFromSampleJar(int port, String soa)
        throws Exception
    {
        String home = System.getenv("HOME");
        String jar = home + "/.m2/repository/org/anon/sampleapp/sampleapp/1.0-SNAPSHOT/sampleapp-1.0-SNAPSHOT.jar";
        return deployJar(port, jar, soa);
    }

    public AssertJSONResponse deployJar(int port, String jar, String flow)
        throws Exception
    {
        AssertJSONResponse resp = postTo(port, "localhost", 
                "/SmartOwner/AdminSmartFlow/DeployEvent", 
                "{'TenantAdmin':{'___smart_action___':'lookup', '___smart_value___':'SmartOwner'}, 'deployJar':'" + jar + "','flowsoa':'" + flow + "'}", true);
        assertTrue(resp != null);
        resp.assertStringStartsWith("success", "Done");
        return resp;
    }

    public AssertJSONResponse postTo(int port, String event, String post)
        throws Exception
    {
        return postTo(port, _tenant, _flow, event, post);
    }

    public AssertJSONResponse postTo(int port, String tenant, String flow, String event, String post)
        throws Exception
    {
        String uri = "/" + tenant + "/" + flow + "/" + event;
        return postTo(port, _server, uri, post, true);
    }

    protected PData createPData(InputStream istr, String uri)
        throws Exception
    {
        return new TestPData(null, new ContentData(istr));
    }

    public AssertJSONResponse postTo(int port, String server, String uri, String post, boolean wait)
        throws Exception
    {
        String ret = "";
        JSON jret = null;
        ResponseCollector collect = new ResponseCollector(wait);
        Rectifier rr = new Rectifier();
        rr.addStep(collect);
        HTTPConfig ccfg = new HTTPConfig(port, false);
        ccfg.setClient();
        ccfg.setServer(server);
        ccfg.setRectifierInstinct(rr, new TestDataFactory());
        HTTPClientChannel cchnl = (HTTPClientChannel)_shell.addChannel(ccfg);
        cchnl.connect();
        ByteArrayInputStream istr = new ByteArrayInputStream(post.getBytes());
        //PData d = new TestPData(null, new ContentData(istr));
        PData d = createPData(istr, uri);
        cchnl.post(uri, new PData[] { d });
        if (wait)
        {
            collect.waitForResponse();
            ret = collect.getResponse();
            cchnl.disconnect();
            jret = JSONSerializer.toJSON(ret);
            return new AssertJSONResponse(jret);
        }

        return null;
    }
}

