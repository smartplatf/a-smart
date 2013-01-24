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
 * File:                org.anon.smart.smcore.test.testanatomy.TestModule
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

package org.anon.smart.smcore.test.testanatomy;

import java.util.List;
import java.io.InputStream;

import org.anon.smart.deployment.Feature;
import org.anon.smart.deployment.Artefact;
import org.anon.smart.deployment.Deployment;
import org.anon.smart.deployment.SuiteAssistant;
import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.base.flow.FlowDeploymentSuite;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;

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

    public void start(StartConfig cfg)
        throws CtxException
    {
        TestModuleConfig conf = (TestModuleConfig)cfg;
        String[] deploy = conf.deploymentFiles();
        CrossLinkSmartTenant cltenant = CrossLinkSmartTenant.currentTenant();
        SmartTenant tenant = (SmartTenant)cltenant.link();
        for (int i = 0; i < deploy.length; i++)
        {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(deploy[i]);
            assertion().assertNotNull(stream, "Cannot find file to deploy. " + deploy[i]);
            FlowDeployment dep = Deployment.deploymentFor(stream, FlowDeployment.class);
            List<String> deployables = dep.artefacts();
            SuiteAssistant<FlowDeployment> assist = FlowDeploymentSuite.getAssistant();
            assist.addDeployment(dep);
            //now deploy all the classes in them.
            for (String cName : deployables)
            {
                try
                {
                    Class cls = this.getClass().getClassLoader().loadClass(cName);
                    Artefact[] arts = assist.deployClazz(cls);
                    System.out.println("Adding artefacts for: " + cName + ":" + arts.length);
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
            tenant.deploymentShell().enableForMe(dep.deployedName(), fnames);
        }
    }
}

