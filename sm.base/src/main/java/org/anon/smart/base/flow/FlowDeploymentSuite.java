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
 * File:                org.anon.smart.base.flow.FlowDeploymentSuite
 * Author:              rsankar
 * Revision:            1.0
 * Date:                14-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A flow service for flows
 *
 * ************************************************************
 * */

package org.anon.smart.base.flow;

import java.util.List;

import org.anon.smart.deployment.DeploymentSuite;
import org.anon.smart.deployment.SuiteAssistant;
import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.loader.LoaderVars;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class FlowDeploymentSuite extends DeploymentSuite<FlowDeployment>
{
    private static final String FLOWSUITE = "org.anon.smart.base.flow.FlowDeploymentSuite";
    private static FlowDeploymentSuite APP_INSTANCE = null;

    protected FlowDeploymentSuite()
        throws CtxException
    {
        super();
        setHandleDeployment(FlowDeployment.class);
    }

    private static void setSingleInstance(Object obj)
    {
        if (APP_INSTANCE == null)
            APP_INSTANCE = (FlowDeploymentSuite)obj;
    }

    private static Object getSingleInstance()
    {
        return APP_INSTANCE;
    }

    public static SuiteAssistant<FlowDeployment> getAssistant()
        throws CtxException
    {
        FlowDeploymentSuite suite = (FlowDeploymentSuite)getAppInstance(FLOWSUITE);
        return suite.assistant();
    }

    public static ClassLoader newDeploymentLoader(String name, String[] jars)
        throws CtxException
    {
        FlowDeploymentSuite suite = (FlowDeploymentSuite)getAppInstance(FLOWSUITE);
        return suite.deploymentClassLoader(name, jars);
    }

    public ClassLoader deploymentClassLoader(String name, String[] jars)
        throws CtxException
    {
        SmartLoader ldr = (SmartLoader)this.getClass().getClassLoader();
        LoaderVars vars = new LoaderVars(name);
        SmartLoader newldr = ldr.repeatMe(vars);
        for (int i = 0; i < jars.length; i++)
            newldr.addJar(jars[i]);
        return newldr;
    }

    public static List<String> getAllDeployments()
        throws CtxException
    {
        SuiteAssistant<FlowDeployment> assist = getAssistant();
        return assist.allDeployments();
    }

    public static Object getCLAssistant()
        throws CtxException
    {
        Object suite = getAppInstance(FLOWSUITE);
        CrossLinkAny clany = new CrossLinkAny(suite);
        return clany.invoke("assistant");
    }
}

