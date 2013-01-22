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

import org.anon.utilities.test.PathHelper;
import org.anon.smart.smcore.test.ModConstants;
import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.SmartChannel;
import org.anon.smart.channels.http.HTTPClientChannel;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.http.HTTPConfig;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.data.PData;

import org.anon.utilities.anatomy.CrossLinkApplication;

public class TestSmartChannel implements ModConstants
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
        Thread.sleep(3000); //response shd have come within 3s
        cchnl.disconnect();
    }

    private SmartLoader createLoader()
        throws Exception
    {
        URL[] urls = new URL[]
                        {
                            new URL(PathHelper.getJar(true, BASE)),
                            new URL(PathHelper.getJar(true, D2CACHE)),
                            new URL(PathHelper.getJar(true, DEPLOYMENT)),
                            new URL(PathHelper.getJar(true, UTILITIES)),
                            new URL(PathHelper.getJar(true, CHANNELS)),
                            new URL(PathHelper.getJar(true, ATOMICITY)),
                            new URL(PathHelper.getProjectTestBuildPath()),
                            new URL(PathHelper.getProjectBuildPath()),
                            new URL(PathHelper.getDependantPath(true, "org/jboss/netty/netty/3.2.5.Final/netty-3.2.5.Final.jar")),
                            new URL(PathHelper.getDependantPath(true, "jcs/jcs/1.3/jcs-1.3.jar"))
                        };

        String[] comps = new String[] { "org.anon.smart.smcore.anatomy.SMCoreModule" };

        SmartLoader ldr = new SmartLoader(urls, comps);
        CrossLinkApplication.getApplication().setStartLoader(ldr);
        return ldr;
    }

    private void runServer()
        throws Exception
    {
        SmartLoader ldr = createLoader();
        Class cls = ldr.loadClass("org.anon.smart.smcore.test.channel.RunSmartServer");
        Object run = cls.newInstance();
        Thread thrd = new Thread((Runnable)run);
        thrd.setContextClassLoader(ldr);
        thrd.start();
        //for now, wait for server to start up
        Thread.currentThread().sleep(2000);
    }

    @Test
    public void testTestSmartChannel()
        throws Exception
    {
        runServer();
        SCShell shell = new SCShell();
        postTo(shell, 9080, "localhost", "/invalidtenant/invalidflow/invalidevent", "{'order':'testorder'}");
        postTo(shell, 9080, "localhost", "/SmartOwner/invalidflow/", "{'order':'testorder'}");
    }
}

