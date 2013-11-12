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
 * File:                org.anon.smart.smcore.inbuilt.transition.CRUDManager
 * Author:              rsankar
 * Revision:            1.0
 * Date:                03-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A manager to handle creation of data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import java.util.Map;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;

import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.inbuilt.events.CreatePrime;
import org.anon.smart.smcore.inbuilt.events.UpdatePrime;
import org.anon.smart.smcore.inbuilt.responses.SuccessCreated;
import org.anon.smart.smcore.inbuilt.responses.SuccessUpdated;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class CRUDManager
{
    public CRUDManager()
    {
    }

    private void printMap(Map<String, Object> m)
    {
        for (String k : m.keySet())
        {
            System.out.println("Key: " + k + ":" + m.get(k) + ":" + m.get(k).getClass());
            if (m.get(k) instanceof Map)
            {
                printMap((Map<String, Object>)m.get(k));
            }
        }
    }

    public void createPrimeData(CreatePrime event)
        throws CtxException
    {
        String type = event.getPrimeType();
        assertion().assertNotNull(type, "Cannot create for a null PrimeClass. Please specify the prime object to create.");
        Object o = event;
        SmartEvent sevt = (SmartEvent)o;
        String flow = sevt.smart___flowname();
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkDeploymentShell shell = tenant.deploymentShell();
        CrossLinkFlowDeployment dep = shell.deploymentFor(flow);
        assertion().assertNotNull(dep, "Cannot find the deployment for " + flow);
        String clsname = dep.classFor(type);
        assertion().assertNotNull(clsname, "Cannot find the deployment class for: " + type + " in " + flow);
        Class cls = shell.primeClass(flow, type); 
        String nm = cls.getName().replaceAll("\\.", "/") + ".class";
        System.out.println("Got the flow for: " + type + ":" + flow + ":" + cls + ":" + 
                nm + ":" + cls.getClassLoader().getResource(nm));
        assertion().assertNotNull(cls, "Cannot find the class for: " + type + ":" + clsname);
        
        Map<String, Object> values = event.getData();
        Object create = convert().mapToObject(cls, values);
        CrossLinkAny clany = new CrossLinkAny(create);
        clany.invoke("smartdatastt___init");
        clany.invoke("smart___initPrime");

        SuccessCreated created = new SuccessCreated("Object for: " + type + " created.");
    }

    public void updatePrimeData(SmartPrimeData obj, UpdatePrime event)
    	throws CtxException
  	{
    	String type = event.getPrimeType();
        assertion().assertNotNull(type, "Cannot update for a null PrimeClass. Please specify the prime object to update.");
        Object o = event;
        SmartEvent sevt = (SmartEvent)o;
        // Object obj = sevt.smart___primeData();
        System.out.println("------------------:"+obj);
        
        String flow = sevt.smart___flowname();
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkDeploymentShell dShell = tenant.deploymentShell();
        RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
        CrossLinkFlowDeployment dep = dShell.deploymentFor(flow);
        assertion().assertNotNull(dep, "Cannot find the deployment for " + flow);
        String clsname = dep.classFor(type);
        assertion().assertNotNull(clsname, "Cannot find the deployment class for: " + type + " in " + flow);
        Class cls = dShell.primeClass(flow, type); 
        String nm = cls.getName().replaceAll("\\.", "/") + ".class";
        System.out.println("Got the flow for: " + type + ":" + flow + ":" + cls + ":" + 
                nm + ":" + cls.getClassLoader().getResource(nm));
        assertion().assertNotNull(cls, "Cannot find the class for: " + type + ":" + clsname);
        
        Map<String, Object> values = event.getData();
        convert().updateObject(obj, cls, values);
        SuccessUpdated response = new SuccessUpdated("Object for: " + type + " and for obj :" + obj + " updated.");
  	}
    	
    	
}

