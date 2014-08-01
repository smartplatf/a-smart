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
 * File:                org.anon.smart.deployment.MacroDeployer
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A deployer for macro deployments
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.concurrent.ConcurrentHashMap;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class MacroDeployer
{
    private static Map<String, Class<? extends Deployment>> REGISTERED = new ConcurrentHashMap<String, Class<? extends Deployment>>();
    private static Map<String, Class<? extends DeploymentSuite>> REGISTEREDSUITE = new ConcurrentHashMap<String, Class<? extends DeploymentSuite>>();

    private static String CONFIG_DIR;

    public MacroDeployer()
    {
    }

    public static void setConfigDir(String dir)
    {
        CONFIG_DIR = dir;
    }

    public static String getConfigDir()
    {
        return CONFIG_DIR;
    }

    public static void registerDeploymentClazz(String type, Class<? extends Deployment> cls, Class<? extends DeploymentSuite> suite)
    {
        if (!REGISTERED.containsKey(type))
        {
            REGISTERED.put(type, cls);
            REGISTEREDSUITE.put(type, suite);
        }
    }

    private static String persistName()
    {
        return CONFIG_DIR + "/deployed";
    }

    private static void persistDeploy(String type, String flowsoa, String[] jarfiles)
        throws CtxException
    {
        try
        {
            if ((CONFIG_DIR != null) && (jarfiles != null)) //do no store the standard deployments, they will get deployed
            {
                FileWriter write = new FileWriter(persistName(), true);
                PrintWriter writer = new PrintWriter(write, true);
                String swrite = type + "|" + flowsoa + "|";
                for (int i = 0; i < jarfiles.length; i++)
                    swrite += jarfiles[i] + "|";
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

    public static void deployPersistedJars()
        throws CtxException
    {
        try
        {
            if (CONFIG_DIR != null)
            {
                File f = new File(persistName());
                if (f.exists())
                {
                    FileReader rdr = new FileReader(persistName());
                    BufferedReader brdr = new BufferedReader(rdr);
                    String line = "";
                    while ((line = brdr.readLine()) != null)
                    {
                        String[] dep = line.split("\\|");
                        assertion().assertTrue((dep.length >= 3), "The format is not correct");
                        String type = dep[0];
                        String soa = dep[1];
                        List<String> jars = new ArrayList<String>();
                        for (int i = 2; i < dep.length; i++)
                        {
                            if (dep[i].trim().length() > 0)
                                jars.add(dep[i].trim());
                        }

                        assertion().assertTrue((jars.size() >= 1), "The format is not correct");
                        System.out.println("Deploying: " + type + ":" + soa + ":" + jars);
                        deployFile(type, soa, jars.toArray(new String[0]), false);
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

    public static Map<String, String[]> deployFile(String type, String flowsoa, String[] jarfiles)
        throws CtxException
    {
        return deployFile(type, flowsoa, jarfiles, true);
    }

    public static Map<String, String[]> deployFile(String type, String flowsoa, String[] jarfiles, boolean persist)
        throws CtxException
    {
        Map<String, String[]> ret = new HashMap<String, String[]>();
        CrossLinkAny clany = new CrossLinkAny(REGISTEREDSUITE.get(type).getName());
        ClassLoader ldr = null;
        if (jarfiles != null)
            ldr = (ClassLoader)clany.invoke("newDeploymentLoader", flowsoa, jarfiles);
        else
            ldr = MacroDeployer.class.getClassLoader(); //use this classloader since they are present in the classpath.
        InputStream stream = ldr.getResourceAsStream(flowsoa);
        assertion().assertNotNull(stream, "Cannot find file to deploy. " + flowsoa);
        assertion().assertTrue(REGISTERED.containsKey(type), "Deployment type not setup correctly for " + type);
        Deployment dep = Deployment.deploymentFor(stream, REGISTERED.get(type), ldr, jarfiles);
        dep.setup();
        List<String> deployables = dep.artefacts();
        SuiteAssistant assist = (SuiteAssistant)clany.invoke("getAssistant");
        assist.addDeployment(dep);
        String fname = dep.deployedName();
        //now deploy all the classes in them.
        for (String cName : deployables)
        {
            try
            {
                //TODO: need to change this
                CrossLinkAny cldeprt = new CrossLinkAny(DeployRuntime.class.getName(), ldr);
                cldeprt.invoke("setupDeploying", new Class[] { Object.class }, new Object[] { dep });
                Class cls = ldr.loadClass(cName);
                cldeprt.invoke("setupDeploying", new Class[] { Object.class }, new Object[] { null });
                Artefact[] arts = assist.deployClazz(fname, cls);
                //System.out.println("Adding artefacts for: " + cName + ":" + arts.length);
                dep.addArtefacts(arts);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                except().rt(e, new CtxException.Context("Error deploy " + cName, "Exception"));
            }
        }

        //for now testing
        Feature[] features = dep.getFeatures();
        String[] fnames = new String[features.length];
        for (int j = 0; j < features.length; j++)
            fnames[j] = features[j].getName();

        ret.put(dep.deployedName(), fnames);
        if (persist)
            persistDeploy(type, flowsoa, jarfiles);
        return ret;
    }
}

