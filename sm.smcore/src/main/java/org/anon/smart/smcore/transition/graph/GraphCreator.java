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
 * File:                org.anon.smart.smcore.transition.graph.GraphCreator
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A creator for graphs from method annotations
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.graph;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Method;

import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.smcore.transition.TConstants;
import org.anon.smart.smcore.annot.MethodAnnotate;
import org.anon.smart.smcore.annot.ServiceAnnotate;
import org.anon.smart.smcore.annot.ServicesAnnotate;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.gconcurrent.Graph;
import org.anon.utilities.gconcurrent.GraphNode;
import org.anon.utilities.exception.CtxException;

public class GraphCreator implements TConstants
{
	public static final String ANY = "Any|";
    GraphCreator()
    {
    }

    public Map<String, Graph> graphFor(String flow, String prime, String event, String extra)
        throws CtxException
    {
        String keyextra = null;
        String key = ArtefactType.createKey(prime, event);
        if ((extra != null) && (extra.length() > 0))
            keyextra = ArtefactType.createKey(prime, event, extra);
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        assertion().assertNotNull(tenant, "Not running in tenant context. Error.");
        CrossLinkDeploymentShell shell = tenant.deploymentShell();
        assertion().assertNotNull(shell, "Not a Valid deployment shell.");
        List<Class> transitions = shell.transitionsFor(flow, prime, event, extra);
        List<Class> atransitions = shell.transitionsFor("AllFlows", prime, event, extra);
        List<Class> Anytransitions = shell.transitionsFor("AllFlows", "*Any", event, extra);
        transitions.addAll(atransitions); //that which runs for all flows.
        transitions.addAll(Anytransitions); //that which runs for all flows.
        Map<String, Graph> ret = new ConcurrentHashMap<String, Graph>();
        try
        {
            for (int i = 0; i < transitions.size(); i++)
            {
                Class cls = transitions.get(i);
                Method[] methods = cls.getDeclaredMethods();
                addMethods(key, keyextra, cls, methods, ret, shell, flow);
                addServices(key, keyextra, cls, ret, shell, flow);
            }

            //now add dependencies
            for (String from : ret.keySet())
            {
                Graph g = ret.get(from);
                setupDependencies(g);
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Exception creating graph: " + prime + ":" + event, "Exception"));
        }

        return ret;
    }

    private void setupparent(String parent, String child, Map<String, List<String>> pmap)
    {
        List<String> lst = pmap.get(parent);
        if (lst == null)
            lst = new ArrayList<String>();
        lst.add(child);
        pmap.put(parent, lst);
    }

    private void setupDependencies(Graph graph)
        throws CtxException
    {
        List<GraphNode> ndes = graph.nodes();
        Map<String, GraphNode> nmap = new HashMap<String, GraphNode>();
        Map<String, List<String>> pmap = new HashMap<String, List<String>>();
        for (GraphNode nde : ndes)
        {
            TransitionNodeDetails det = (TransitionNodeDetails)nde.details();
            nmap.put(det.name(), nde);
            if ((det.after() != null) && (det.after().length() > 0))
                setupparent(det.after(), det.name(), pmap);

            if ((det.before() != null) && (det.before().length() > 0))
                setupparent(det.name(), det.before(), pmap);
        }

        for (String parent : pmap.keySet())
        {
            List<String> children = pmap.get(parent);
            GraphNode pnode = nmap.get(parent);
            assertion().assertNotNull(pnode, "An error in transition soa file. Please ensure " + parent + " is present. ");
            for (String child : children)
            {
                GraphNode relateto = nmap.get(child);
                if ((relateto != null) && (pnode != null))
                    graph.addDependency(pnode, relateto);
            }
        }
    }

    private GraphNode createNode(String key, String keyextra, Class cls, Method mthd, ServiceAnnotate annot, String parms)
        throws CtxException
    {
        if (annot != null)
        {
            String foreach = annot.foreach();
            String[] per = value().listAsString(foreach);
            boolean isfor = false;
            for (int i = 0; (!isfor) && (i < per.length); i++)
            {
            	if (key.equals(per[i]) || ((keyextra != null) && (keyextra.equals(per[i]))) || 
            			((per[i].startsWith(ANY)) && (per[i].endsWith(key.substring(key.indexOf('|')+1, key.length())))))
                    isfor = true;
            }

            if (isfor)
            {
                TransitionNodeDetails det = new TransitionNodeDetails(cls, mthd, annot, parms);
                return new GraphNode(det.name(), cls, mthd, det);
            }
        }

        return null;
    }

    private GraphNode createNode(String key, String keyextra, Class cls, Method mthd, CrossLinkDeploymentShell shell, String flow)
        throws CtxException
    {
        MethodAnnotate annot = (MethodAnnotate)mthd.getAnnotation(MethodAnnotate.class);
        if (annot != null)
        {
            String foreach = annot.foreach();
            String[] per = value().listAsString(foreach);
            boolean isfor = false;
            for (int i = 0; (!isfor) && (i < per.length); i++)
            {
                List<String> check = new ArrayList<String>();
                check.add(per[i]);
                if (per[i].indexOf("needslink") >= 0)
                {
                    String[] links = shell.linksFor(flow, cls, per[i]);
                    for (int j = 0; (links != null) && (j < links.length); j++)
                        check.add(links[j]);
                }

                System.out.println("Creating a method for: " + check);

                for (int k = 0; k < check.size(); k++)
                {
                    String one = check.get(k);
                    if (key.equals(one) || ((keyextra != null) && (keyextra.equals(one))) || 
                            ((one.startsWith(ANY)) && (one.endsWith(key.substring(key.indexOf('|')+1, key.length())))))
                        isfor = true;
                    System.out.println("one: " + one + ":" + key + ":" + isfor + ":" + keyextra + ":" + key.substring(key.indexOf('|')+1, key.length()));
                }
            }

            if (isfor)
            {
                TransitionNodeDetails det = new TransitionNodeDetails(cls, mthd, annot);
                return new GraphNode(det.name(), cls, mthd, det);
            }
        }

        return null;
    }

    private void addServices(String key, String keyextra, Class cls, Map<String, Graph> into, CrossLinkDeploymentShell shell, String flow)
        throws CtxException
    {
        ServicesAnnotate sannot = (ServicesAnnotate)reflect().getAnyAnnotation(cls, ServicesAnnotate.class.getName());
        if (sannot == null)
            return;

        CrossLinkFlowDeployment dep = shell.deploymentFor(flow);
        ServiceAnnotate[] sannots = sannot.callservices();
        for (int i = 0; i < sannots.length; i++)
        {
            String svc = sannots[i].service();
            String parms = sannots[i].parms();
            if (sannots[i].service().equals(NEEDSSERVICE))
            {
                svc = dep.getServiceMash(sannots[i].name());
                System.out.println("Unparsed is: " + svc);
                if (svc == null)
                    svc = NOTMAPPED;

                String[] vals = svc.split("\\(");
                svc = vals[0];
                if (vals.length > 1)
                {
                    parms = "(" + vals[1];
                }
                System.out.println("Got svc as: " + svc + ":" + parms);
            }

            if (!svc.equals(NEEDSSERVICE) && !svc.equals(NOTMAPPED))
            {
                Object[] det = shell.getServiceFor(svc);
                assertion().assertNotNull(det, "Cannot find service for: " + svc);
                Class svccls = (Class)det[0];
                Method mthd = (Method)det[1];
                GraphNode nde = createNode(key, keyextra, svccls, mthd, sannots[i], parms);
                if (nde == null)
                    continue;
                addNode(nde, into);
            }
        }
    }

    private void addMethods(String key, String keyextra, Class cls, Method[] methods, Map<String, Graph> into, CrossLinkDeploymentShell shell,
            String flow)
        throws CtxException
    {
        for (int i = 0; i < methods.length; i++)
        {
            GraphNode nde = createNode(key, keyextra, cls, methods[i], shell, flow);
            if (nde == null)
                continue;
            addNode(nde, into);
            /*TransitionNodeDetails det = (TransitionNodeDetails)nde.details();
            String from = det.from();
            String[] farray = value().listAsString(from);
            for (int f = 0; f < farray.length; f++)
            {
                Graph graph = into.get(farray[f]);
                if (graph == null)
                    graph = new Graph();

                graph.addGraphNode(nde);
                into.put(farray[f], graph);
            }*/
        }
    }

    private void addNode(GraphNode nde, Map<String, Graph> into)
        throws CtxException
    {
        TransitionNodeDetails det = (TransitionNodeDetails)nde.details();
        String from = det.from();
        String[] farray = value().listAsString(from);
        for (int f = 0; f < farray.length; f++)
        {
            Graph graph = into.get(farray[f]);
            if (graph == null)
                graph = new Graph();

            graph.addGraphNode(nde);
            into.put(farray[f], graph);
        }
    }
}

