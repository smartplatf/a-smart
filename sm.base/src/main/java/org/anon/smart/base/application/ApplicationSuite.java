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
 * File:                org.anon.smart.base.application.ApplicationSuite
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of applications that is deployed in this instance of SMART
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.deployment.MacroDeployer;

import org.anon.utilities.config.Format;
import org.anon.utilities.utils.ApplicationSingleton;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

public class ApplicationSuite extends ApplicationSingleton
{
    private static final String APPSUITE = "org.anon.smart.base.application.ApplicationSuite";
    private static ApplicationSuite APPSUITE_INSTANCE = null;

    private Map<String, ApplicationDefinition> _applications;
    private List<ApplicationAction> _actions;

    private ApplicationSuite()
    {
        _applications = new ConcurrentHashMap<String, ApplicationDefinition>();
        //shd be registered once and done. So no need to synchronize?
        _actions = new ArrayList<ApplicationAction>();
    }

    private static String getConfigDir()
    {
        return MacroDeployer.getConfigDir();
    }

    private static String persistName()
    {
        return getConfigDir() + "/applicationsdeployed";
    }

    private static void setSingleInstance(Object obj)
    {
        if (APPSUITE_INSTANCE == null)
            APPSUITE_INSTANCE = (ApplicationSuite)obj;
    }

    private static Object getSingleInstance()
    {
        return APPSUITE_INSTANCE;
    }

    public static ApplicationDefinition getApplication(String name)
        throws CtxException
    {
        ApplicationSuite appdef = (ApplicationSuite)getAppInstance(APPSUITE);
        ApplicationDefinition adef = appdef._applications.get(name);
        return adef;
    }

    public static void registerAction(ApplicationAction action)
        throws CtxException
    {
        ApplicationSuite appdef = (ApplicationSuite)getAppInstance(APPSUITE);
        appdef._actions.add(action);
    }

    public static void doAction(ApplicationAction.appactions action, ApplicationDefinition adef, Map parms)
        throws CtxException
    {
        doAction(action, adef, parms, false);
    }

    public static void doAction(ApplicationAction.appactions act, ApplicationDefinition adef, Map parms, boolean readback)
        throws CtxException
    {
        ApplicationSuite appdef = (ApplicationSuite)getAppInstance(APPSUITE);
        for (int i = 0; i < appdef._actions.size(); i++)
        {
            ApplicationAction action = appdef._actions.get(i);
            action.doAction(act, adef, parms, readback);
        }
    }

    public static void enableApplication(String app, String pkg, SmartTenant tenant, TenantAdmin admin, boolean readback)
        throws CtxException
    {
        Map parms = new HashMap();
        parms.put(EnablePackageAction.PACKAGE, pkg);
        parms.put(EnablePackageAction.TENANT, tenant.getName());
        parms.put(EnablePackageAction.TENANTOBJ, tenant);
        parms.put(EnablePackageAction.TENANTADMIN, admin);
        parms.put(EnablePackageAction.DEPLOYMENTSHELL, tenant.deploymentShell());
        ApplicationDefinition adef = getApplication(app);
        doAction(ApplicationAction.appactions.enable, adef, parms, readback);
        tenant.enabledApplication(app, pkg);
    }

    public static void deployApplication(String soafile, Map<String, String> vars, boolean readback)
        throws CtxException
    {
        try
        {
            //assumption is that the soafile exists on the server
            File f = new File(soafile);
            assertion().assertTrue(f.exists(), "Cannot find the application soa file " + soafile);
            InputStream str = new FileInputStream(f);
            Format fmt = config().readYMLConfig(str);
            Map vals = fmt.allValues();
            ApplicationDefinition adef = (ApplicationDefinition)convert().mapToVerifiedObject(ApplicationDefinition.class, vals);
            adef.setupVariables(vars);
            ApplicationSuite appdef = (ApplicationSuite)getAppInstance(APPSUITE);
            assertion().assertFalse(appdef._applications.containsKey(adef.getName()), "The same application has been defined earlier.");
            if (!readback)
                persistApplication(soafile, vars);
            appdef._applications.put(adef.getName(), adef);
            doAction(ApplicationAction.appactions.deploy, adef, new HashMap(), readback);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("AppDeployException", e.getMessage()));
        }
    }

    private static void persistApplication(String soafile, Map<String, String> vars)
        throws CtxException
    {
        try
        {
            if (getConfigDir() != null) //do no store the standard deployments, they will get deployed
            {
                FileWriter write = new FileWriter(persistName(), true);
                PrintWriter writer = new PrintWriter(write, true);
                String swrite = soafile;
                if (vars != null)
                {
                    String add = "";
                    String varstr = "";
                    for (String k : vars.keySet())
                    {
                        //obvious assumption is that the values do not contain | or ,
                        varstr += add + k + "=" + vars.get(k);
                        add = "|";
                    }
                    swrite += "|" + varstr;
                }
                writer.println(swrite);
                writer.close();
                write.close();
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Cannot store deployment", e.getMessage()));
        }
    }

    public static void deployPersistedApplications()
        throws CtxException
    {
        try
        {
            if (getConfigDir() != null)
            {
                File f = new File(persistName());
                if (f.exists())
                {
                    FileReader rdr = new FileReader(persistName());
                    BufferedReader brdr = new BufferedReader(rdr);
                    String line = "";
                    while ((line = brdr.readLine()) != null)
                    {
                        System.out.println("Adding Application: " + line);
                        String[] vals = line.split("\\|");
                        assertion().assertTrue((vals.length >= 1), "The data is not stored correctly");
                        String soafile = vals[0];
                        Map<String, String> vars = new HashMap<String, String>();
                        for (int i = 1; i < vals.length; i++)
                        {
                            String[] varstr = vals[i].split("=");
                            vars.put(varstr[0], varstr[1]);
                        }

                        deployApplication(soafile, vars, true);
                    }
                    brdr.close();
                    rdr.close();
                }
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("cannot re-deploy.", e.getMessage()));
        }
    }
}

