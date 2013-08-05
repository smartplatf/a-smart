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
 * File:                org.anon.smart.base.test.testanatomy.TestModule
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test module which is started up
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.testanatomy;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStream;

import org.anon.smart.deployment.Feature;
import org.anon.smart.deployment.Artefact;
import org.anon.smart.deployment.Deployment;
import org.anon.smart.deployment.SuiteAssistant;
import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.base.flow.FlowDeploymentSuite;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantsHosted;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.anatomy.AModule;
import org.anon.utilities.anatomy.StartConfig;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.exception.CtxException;

public class TestModule extends AModule
{
    public TestModule(AModule parent)
        throws CtxException
    {
        super(parent, new TestContext(), false);
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new TestModule(_parent);
    }

    private String[] deploy(String[] deploy, Map<String, SmartTenant[]> enable, String[] jars)
        throws CtxException
    {
        List<String> ret = new ArrayList<String>();
        for (int i = 0; i < deploy.length; i++)
        {
            ClassLoader ldr = FlowDeploymentSuite.newDeploymentLoader(deploy[i], jars);
            InputStream stream = ldr.getResourceAsStream(deploy[i]);
            assertion().assertNotNull(stream, "Cannot find file to deploy. " + deploy[i]);
            FlowDeployment dep = Deployment.deploymentFor(stream, FlowDeployment.class, ldr, jars);
            ret.add(dep.deployedName());
            List<String> deployables = dep.artefacts();
            SuiteAssistant<FlowDeployment> assist = FlowDeploymentSuite.getAssistant();
            assist.addDeployment(dep);
            String fname = dep.deployedName();
            //now deploy all the classes in them.
            for (String cName : deployables)
            {
                try
                {
                    Class cls = this.getClass().getClassLoader().loadClass(cName);
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

            SmartTenant[] tenants = enable.get(deploy[i]);
            System.out.println("Got Tenants: " + dep.deployedName() + ":" + tenants);
            for (int t = 0; (tenants != null) && (t < tenants.length); t++)
            {
                System.out.println("Enabling deployments for: " +  dep.deployedName() + ":" + tenants[t].getName());
                tenants[t].deploymentShell().enableForMe(dep.deployedName(), fnames, new HashMap<String, String>());
            }
        }

        return ret.toArray(new String[0]);
    }

    private Map<String, SmartTenant> createTenants(String[] tenant)
        throws CtxException
    {
        Map<String, SmartTenant> tenants = new HashMap<String, SmartTenant>();
        for (int i = 0; (tenant != null) && (i < tenant.length); i++)
        {
            SmartTenant stenant = TenantsHosted.addTenant(tenant[i]);
            stenant = TenantsHosted.tenantFor(tenant[i]); //ensure it is added
            tenants.put(tenant[i], stenant);
        }
        return tenants;
    }

    public void start(StartConfig cfg)
        throws CtxException
    {
        try
        {
            TestModuleConfig conf = (TestModuleConfig)cfg;
            System.out.println("Creating tenant space...>>>>>>>>>>>>>>>>");
            if (conf.initTenants())
                TenantsHosted.initialize();

            System.out.println("Initialized tenants hosted: >>>>>>>>>>>>>>>>");

            String[] tNames = conf.tenantsToCreate();
            Map<String, SmartTenant> tenants = createTenants(tNames);
            String[] deploy = conf.deploymentFiles();
            Map<String, SmartTenant[]> enable = new HashMap<String, SmartTenant[]>();
            for (int i = 0; (deploy != null) && (i < deploy.length); i++)
            {
                String[] tens = conf.tenantsToEnableFor(deploy[i]);
                List<SmartTenant> stens = new ArrayList<SmartTenant>();
                for (int j = 0; (tens != null) && (j < tens.length); j++)
                {
                    if (tenants.containsKey(tens[j]))
                        stens.add(tenants.get(tens[j]));
                }
                enable.put(deploy[i], stens.toArray(new SmartTenant[0]));
            }
            String[] dNames = deploy(deploy, enable, conf.jarFiles());
            for (int i = 0; i < tNames.length; i++)
            {
                SmartTenant ten = tenants.get(tNames[i]);
                ClassLoader ldr = ten.getRelatedLoader();
                Object tcfg = conf.configFor(tNames[i], ldr);
                Class cls = ldr.loadClass("org.anon.smart.base.test.testanatomy.TenantObjectCreator");
                Object run = cls.getDeclaredConstructor(Object.class, Object.class, String[].class).newInstance(ten, tcfg, dNames);
                Thread thrd = new Thread((Runnable)run);
                thrd.setContextClassLoader(ldr);
                thrd.start();
                thrd.join();
            }

            System.out.println("STARTED TESTANATOMY MODULE... THE SERVER IS STARTED.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().rt(e, new CtxException.Context("Error", "Error"));
        }
    }
}

