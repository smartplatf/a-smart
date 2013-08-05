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
 * File:                org.anon.smart.base.test.ServerUtilities
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of utilities that runs server
 *
 * ************************************************************
 * */

package org.anon.smart.base.test;

import java.net.URL;

import org.anon.smart.base.loader.SmartLoader;

import org.anon.utilities.test.PathHelper;
import org.anon.utilities.anatomy.CrossLinkApplication;

import static org.anon.utilities.services.ServiceLocator.*;

public class ServerUtilities implements ModConstants
{
    protected SmartLoader _loader;
    protected String _runner;
    protected int _port;

    protected URL[] getURLs()
        throws Exception
    {
        URL[] urls = new URL[]
        {
            new URL(PathHelper.getJar(true, UTILITIES)),
            new URL(PathHelper.getJar(true, DEPLOYMENT)),
            new URL(PathHelper.getJar(true, D2CACHE)),
            new URL(PathHelper.getJar(true, CHANNELS)),
            new URL(PathHelper.getJar(true, ATOMICITY)),
            //new URL(PathHelper.getJar(true, SAMPLEAPP)),
            new URL(PathHelper.getProjectTestBuildPath()),
            new URL(PathHelper.getProjectBuildPath()),
            new URL(PathHelper.getDependantPath(true, "org/jboss/netty/netty/3.2.5.Final/netty-3.2.5.Final.jar")),
            new URL(PathHelper.getDependantPath(true, "jcs/jcs/1.3/jcs-1.3.jar")),
            new URL(PathHelper.getDependantPath(true, "org/apache/solr/solr-core/4.0.0/solr-core-4.0.0.jar")),
            new URL(PathHelper.getDependantPath(true, "org/apache/solr/solr-solrj/4.0.0/solr-solrj-4.0.0.jar")),
            new URL(PathHelper.getDependantPath(true, "org/apache/lucene/lucene-analyzers-common/4.0.0/lucene-analyzers-common-4.0.0.jar")),
            new URL(PathHelper.getDependantPath(true, "org/apache/lucene/lucene-core/4.0.0/lucene-core-4.0.0.jar")),
            new URL(PathHelper.getDependantPath(true, "org/apache/hbase/hbase/0.90.6-cdh3u5/hbase-0.90.6-cdh3u5.jar")),
            new URL(PathHelper.getDependantPath(true, "org/apache/hadoop/hadoop-core/0.20.2-cdh3u5/hadoop-core-0.20.2-cdh3u5.jar"))
        };

        return urls;
    }

    protected String[] getModules()
    {
        String[] comps = new String[] { "org.anon.smart.base.test.testanatomy.TestModule" };
        return comps;
    }

    protected SmartLoader createLoader()
        throws Exception
    {
        URL[] urls = getURLs();
        String[] comps = getModules();
        SmartLoader ldr = new SmartLoader(urls, comps);
        CrossLinkApplication.getApplication().setStartLoader(ldr);
        return ldr;
    }

    public void runServer(String runner)
        throws Exception
    {
        SmartLoader ldr = createLoader();
        _loader = ldr;
        _runner = runner;
        Class cls = ldr.loadClass(runner);
        Object run = cls.getDeclaredConstructor(Boolean.TYPE, Integer.TYPE).newInstance(true, _port);
        Thread thrd = new Thread((Runnable)run);
        thrd.setContextClassLoader(ldr);
        thrd.start();
        reflect().getAnyMethod(cls, "waitToStart").invoke(run);
        //for now, wait for server to start up
        //Thread.currentThread().sleep(10000);
    }

    public void stopServer()
        throws Exception
    {
        Class cls = _loader.loadClass(_runner);
        Object run = cls.getDeclaredConstructor(Boolean.TYPE, Integer.TYPE).newInstance(false, _port);
        Thread thrd = new Thread((Runnable)run);
        thrd.setContextClassLoader(_loader);
        thrd.start();
        reflect().getAnyMethod(cls, "waitToStop").invoke(run);
        //for now, wait for server to stop 
        //Thread.currentThread().sleep(10000);
        System.out.println("Stopped.");
    }

    public ServerUtilities(int port)
    {
        _port = port;
    }
}

