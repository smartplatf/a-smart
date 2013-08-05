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
 * File:                org.anon.smart.smcore.inbuilt.transition.SearchManager
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 3, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import static org.anon.utilities.services.ServiceLocator.assertion;

import java.util.List;

import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.inbuilt.events.CheckExistence;
import org.anon.smart.smcore.inbuilt.events.ListAllEvent;
import org.anon.smart.smcore.inbuilt.events.LookupEvent;
import org.anon.smart.smcore.inbuilt.events.SearchEvent;
import org.anon.smart.smcore.inbuilt.responses.CheckExistenceResponse;
import org.anon.smart.smcore.inbuilt.responses.ListAllResponse;
import org.anon.smart.smcore.inbuilt.responses.LookupResponse;
import org.anon.smart.smcore.inbuilt.responses.SearchResponse;
import org.anon.utilities.exception.CtxException;

public class SearchManager {

	public void search(SearchEvent searchEvent) 
			throws CtxException
	{
		assertion().assertNotNull(searchEvent, "SearchManager: search event is NULL");
		assertion().assertNotNull(searchEvent.getQueryMap(), "SearchManager: Query cannot be NULL");
		
		System.out.println("Searching with this queryMap:"+searchEvent.getQueryMap());
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
		assertion().assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
		CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(tenant.deploymentShell());
		Object o = searchEvent;
        	SmartEvent sevt = (SmartEvent)o;
        	String flow = sevt.smart___flowname();
		Class clz = dShell.deployment(flow, searchEvent.getGroup(), FlowConstants.PRIMEDATA);
		if(clz != null)System.out.println("SearchManager:result type is "+clz.getName() );
		List<Object> searchResult = shell.searchFor(dShell.deploymentFor(flow).deployedName(),
											clz, searchEvent.getQueryMap());
		
		
		if(searchResult != null)
		{
			System.out.println("ResultSet size:"+searchResult.size());
			for(Object res : searchResult)
				System.out.println("------------Search Result:"+res);
		}
		
		SearchResponse resp = new SearchResponse(searchResult);
	}
	
	public void lookup(LookupEvent lookupEvent) 
			throws CtxException
	{
		assertion().assertNotNull(lookupEvent, "SearchManager: lookup event is NULL");
		assertion().assertNotNull(lookupEvent.getKey(), "SearchManager: Key cannot be NULL");
		
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
		assertion().assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
		CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(tenant.deploymentShell());
		Object o = lookupEvent;
        SmartEvent sevt = (SmartEvent)o;
        String flow = sevt.smart___flowname();
		Object res = shell.lookupFor(dShell.deploymentFor(flow).deployedName(),
				lookupEvent.getGroup(), lookupEvent.getKey());
		System.out.println("----------------Lookup Result:"+res);

		LookupResponse resp = new LookupResponse(res);
		
	}
	
	public void exists(CheckExistence event) 
            throws CtxException
    {
        assertion().assertNotNull(event, "SearchManager: checkExistence event is NULL");
        assertion().assertNotNull(event.getKey(), "SearchManager: Key cannot be NULL for CheckExistence");
        
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
        assertion().assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
        CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(tenant.deploymentShell());
        Object o = event;
        SmartEvent sevt = (SmartEvent)o;
        String flow = sevt.smart___flowname();
        boolean res = shell.exists(dShell.deploymentFor(flow).deployedName(),
                event.getGroup(), event.getKey());
        System.out.println("----------------CHECK EXISTENCE Result:"+res);

        CheckExistenceResponse resp = new CheckExistenceResponse(res);
        
    }
	
	public void listAll(ListAllEvent listEvent)
			throws CtxException
	{
		assertion().assertNotNull(listEvent, "SearchManager: ListAllEvent is NULL");
		assertion().assertNotNull(listEvent.getGroup(), "SearchManager: Group cannot be NULL for ListAllEvent");
		
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
		assertion().assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
		CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(tenant.deploymentShell());
		Object o = listEvent;
        SmartEvent sevt = (SmartEvent)o;
        String flow = sevt.smart___flowname();
		List res = shell.listAll(dShell.deploymentFor(flow).deployedName(),
				listEvent.getGroup(), listEvent.getSize());
		System.out.println("List All Event returned "+res.size()+" results");
		System.out.println("----------------Lookup Result:"+res);

		ListAllResponse resp = new ListAllResponse(res);

		
	}
}
