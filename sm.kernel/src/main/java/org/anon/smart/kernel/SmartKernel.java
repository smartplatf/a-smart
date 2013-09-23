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
 * File:                org.anon.smart.kernel.SmartKernel
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A starting file for smart
 *
 * ************************************************************
 * */

package org.anon.smart.kernel;

import org.anon.smart.kernel.config.SmartConfig;
import org.anon.smart.kernel.config.SmartConfigReader;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.anatomy.CrossLinkApplication;
import org.anon.utilities.exception.CtxException;

public class SmartKernel
{
    private String _serverType;
    private SmartConfig _config;
    private SmartActiveModules _active;
    private ClassLoader _startLoader;
    private String[] _startOrder;

    public SmartKernel(String svr)
        throws CtxException
    {
        _serverType = svr;
        _config = SmartConfigReader.readConfigForServer(svr);
        _active = new SmartActiveModules(_config);
        _startLoader = _active.activeLoader();
        _startOrder = _active.startOrder();
        CrossLinkApplication.getApplication().setStartLoader(_startLoader);
        System.out.println("Created Kernel" + _startLoader);
    }

    public void startServer(boolean master)
        throws CtxException
    {
        try
        {
            Object cfg = serial().cloneIn(_config, _startLoader);
            CrossLinkSmartStarter starter = new CrossLinkSmartStarter(cfg, master, _startOrder, false, _startLoader);
            Object run = starter.link();
            Thread thrd = new Thread((Runnable)run);
            thrd.setContextClassLoader(_startLoader);
            thrd.start();
            thrd.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().rt(e, new CtxException.Context("Error starting server: " + _serverType, "Exception"));
        }
    }

    public void stopServer()
        throws CtxException
    {
        try
        {
            Object cfg = serial().cloneIn(_config, _startLoader);
            CrossLinkSmartStarter starter = new CrossLinkSmartStarter(cfg, false, _startOrder, true, _startLoader);
            Object run = starter.link();
            Thread thrd = new Thread((Runnable)run);
            thrd.setContextClassLoader(_startLoader);
            thrd.start();
            thrd.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().rt(e, new CtxException.Context("Error starting server: " + _serverType, "Exception"));
        }
    }

    public static class ShutdownRun implements Runnable
    {
        SmartKernel kernel;

        ShutdownRun(SmartKernel k)
        {
            kernel = k;
        }

        public void run()
        {
            try
            {
                kernel.stopServer();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Usage java SmartKernel <servertype> <master true/false>");
            return;
        }

        try
        {
            boolean master = convert().stringToBoolean(args[1]);
            SmartKernel kernel = new SmartKernel(args[0]);
            kernel.startServer(master);
            System.out.println("Added shutdownhook.");
            Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownRun(kernel)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

