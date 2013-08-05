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
 * File:                org.anon.smart.base.tenant.shell.DeploymentShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                13-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A shell where active deployments for this tenant is stored
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.flow.Link;
import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.base.flow.FlowDeploymentSuite;
import org.anon.smart.base.flow.PrimeTypeFilter;
import org.anon.smart.base.flow.ClassTypeFilter;
import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.deployment.Artefact;
import org.anon.smart.deployment.LicensedDeploymentSuite;

import org.anon.utilities.exception.CtxException;

public class DeploymentShell implements SmartShell, FlowConstants
{
    private SmartTenant _tenant;
    private ClassLoader _loader;
    private LicensedDeploymentSuite<FlowDeployment> _licensed;
    private Map<String, List<FlowDeployment>> _linked;

    public DeploymentShell(SmartTenant tenant, ClassLoader ldr)
        throws CtxException
    {
        _licensed = new LicensedDeploymentSuite<FlowDeployment>();
        _licensed.setHandleDeployment(FlowDeployment.class);
        _loader = ldr;
        _tenant = tenant;
        _linked = new ConcurrentHashMap<String, List<FlowDeployment>>();
    }

    private String getUnique(String flow, String object)
    {
        return flow + "." + object;
    }

    private void cacheLinks(FlowDeployment dep)
        throws CtxException
    {
        List<Link> lnks = dep.getLinks();
        if (lnks != null)
        {
            for (Link l : lnks)
            {
                String unique = getUnique(l.getFromObject().getFlow(), l.getFromObject().getObject());
                List<FlowDeployment> deps = _linked.get(unique);
                if (deps == null)
                    deps = new ArrayList<FlowDeployment>();
                deps.add(dep);
                _linked.put(unique, deps);
            }
        }
    }

    public FlowDeployment enableForMe(String name, String[] features, Map<String, String> linked)
        throws CtxException
    {
        //this has to be run from the same classloader as the tenant which will be
        //the application class loader. Anything else should anyways give error.
        Artefact[] artefacts = FlowDeploymentSuite.getAssistant().enableFor(_licensed, name, features);
        //for (int i = 0; i < artefacts.length; i++)
         //   System.out.println("Added artefact for " + name + ": " + artefacts[i].getName() + ":" + artefacts[i].getClazz());
        FlowDeployment deploy = _licensed.assistant().deploymentFor(name);
        //if there are links needed, then ensure they are there.
        if (deploy.getNeedLinks() > 0)
        {
            assertion().assertTrue(((linked != null) && (linked.size() >= deploy.getNeedLinks())), "The deployment cannot be enabled without links provided");
            for (String n : linked.keySet())
                deploy.setupLinkFor(n, linked.get(n));
        }

        assertion().assertTrue((deploy.getNeedLinks() <= 0), "Not all links are provided. Need links for: " + deploy.getNeedLinkNames());
        List<String> jars = deploy.myJars();
        for (String jar : jars)
        {
            System.out.println("Added jar to classpath: " + jar);
            ((SmartLoader)_loader).addJar(jar);
        }
        Object model = deploy.model(_loader);
        _tenant.enableFlow(model, artefacts, deploy);
        _tenant.registerEnabledFlow(name, features);
        cacheLinks(deploy);
        return deploy;
    }

    public List<FlowDeployment> linkedDeploymentsFor(String flow, String object)
    {
        String unique = getUnique(flow, object);
        return _linked.get(unique);
    }

    public Class deployment(String dep, String name, String type)
        throws CtxException
    {
        ArtefactType atype = ArtefactType.artefactTypeFor(type);
        assertion().assertNotNull(atype, "Cannot recognized artefactType: " + type);
        return _licensed.assistant().clazzFor(dep, name, atype, _loader);
    }

    public Class eventClass(String dep, String name)
        throws CtxException
    {
        return deployment(dep, name, EVENT);
    }

    public Class dataClass(String dep, String name)
        throws CtxException
    {
        Class cls = deployment(dep, name, DATA);
        if (cls == null)
            cls = deployment(dep, name, PRIMEDATA);
        return cls;
    }

    public Class configClass(String dep, String name)
        throws CtxException
    {
        return deployment(dep, name, CONFIG);
    }

    public List<Class> transitionsFor(String dep, String prime, String event, String extra)
        throws CtxException
    {
        ArtefactType atype = ArtefactType.artefactTypeFor(TRANSITION);
        String srch = atype.createKey(".*", prime, event);
        List<Class> cls = _licensed.assistant().clazzezFor(dep, srch, atype, _loader);
        if (cls == null)
            cls = new ArrayList<Class>();
        if ((extra != null) && (extra.length() > 0))
        {
            //Append the specific ones also
            srch = atype.createKey(".*", prime, event, extra);
            List<Class> extrats = _licensed.assistant().clazzezFor(dep, srch, atype, _loader);
            cls.addAll(extrats);
        }

        return cls;
    }

    public Class primeClass(String dep, String name)
        throws CtxException
    {
        return deployment(dep, name, PRIMEDATA);
    }

    public List<Class> searchDeployment(String dep, String wild, String type)
        throws CtxException
    {
        ArtefactType atype = ArtefactType.artefactTypeFor(type);
        assertion().assertNotNull(atype, "Cannot recognized artefactType: " + type);
        return _licensed.assistant().clazzezFor(dep, wild, atype, _loader);
    }

    public FlowDeployment deploymentFor(String flow)
        throws CtxException
    {
        return (FlowDeployment)_licensed.assistant().deploymentFor(flow);
    }

    public FlowDeployment flowForPrimeType(String flow, String name)
        throws CtxException
    {
        if (flow == null)
            flow = "";
        List<FlowDeployment> deps = (List<FlowDeployment>)_licensed.assistant().deploymentFor(new PrimeTypeFilter(name));
        for (FlowDeployment dep : deps)
        {
            if (dep.deployedName().equals(flow))
                return dep;
        }

        if (deps.size() > 0)
            return deps.get(0);

        return null;
    }

    public List<FlowDeployment> flowForType(String name)
        throws CtxException
    {
        return (List<FlowDeployment>)_licensed.assistant().deploymentFor(new ClassTypeFilter(name));
    }

    public void initializeShell()
        throws CtxException
    {
    }

    public void cleanup()
        throws CtxException
    {
    }
}

