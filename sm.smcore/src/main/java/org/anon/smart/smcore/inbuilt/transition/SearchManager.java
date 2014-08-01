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

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.d2cache.ListParams;
import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.inbuilt.events.CheckExistence;
import org.anon.smart.smcore.inbuilt.events.GetListings;
import org.anon.smart.smcore.inbuilt.events.ListAllEvent;
import org.anon.smart.smcore.inbuilt.events.LookupEvent;
import org.anon.smart.smcore.inbuilt.events.SearchEvent;
import org.anon.smart.smcore.inbuilt.responses.CheckExistenceResponse;
import org.anon.smart.smcore.inbuilt.responses.ListAllResponse;
import org.anon.smart.smcore.inbuilt.responses.LookupResponse;
import org.anon.smart.smcore.inbuilt.responses.SearchResponse;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.utilities.exception.CtxException;


import static org.anon.utilities.objservices.ObjectServiceLocator.*;

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
		Class clz = dShell.dataClass(flow, searchEvent.getGroup());
        System.out.println("Retrieved: " + searchEvent.getGroup() + ":" + flow + ":" + clz);
        assertion().assertNotNull(clz, "Cannot find deployment for: " + searchEvent.getGroup() + ":" + flow);
        List<Object> searchResult = new ArrayList<Object>();
		if(clz != null)
        {
            System.out.println("SearchManager:result type is "+clz.getName() );
            searchResult = shell.searchFor(dShell.deploymentFor(flow).deployedName(),
											clz, searchEvent.getQueryMap(), searchEvent.getSize(), 
                                            searchEvent.getPageNum(), searchEvent.getPageSize(), 
                                            searchEvent.getSortBy(), searchEvent.sortAscending());
		
		
            if(searchResult != null)
            {
                System.out.println("ResultSet size:"+searchResult.size());
                for(Object res : searchResult)
                    System.out.println("------------Search Result:"+res);
            }
        }
		
        Map<String, String> val = searchEvent.getQueryMap();
        long l = 0;
        if (val.containsKey("TOTALSIZE"))
            l = Long.parseLong(val.get("TOTALSIZE"));
        System.out.println("***** Setting total Found as: " + l);
		SearchResponse resp = new SearchResponse(searchResult);
        resp.setTotalFound(l);
	}

    public boolean searchService(String flow, String group, Map<String, String> query, List result, Integer pn, Integer ps, String sortby, Boolean asc)
        throws CtxException
    {
		if ((query != null) && (query.size() > 0))
        {
            System.out.println("Searching with this queryMap:" + query);
            CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
            RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
            assertion().assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
            assertion().assertNotNull(result, "Need to initialize the result with an empty list.");
            CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(tenant.deploymentShell());
            Class clz = dShell.dataClass(flow, group);
            System.out.println("Retrieved: " + group + ":" + flow + ":" + clz);
            assertion().assertNotNull(clz, "Cannot find deployment for: " + group + ":" + flow);
            List<Object> searchResult = new ArrayList<Object>();
            if(clz != null)
            {
                int pagenum = -1;
                if (pn != null) pagenum = pn.intValue();
                int pagesize = -1;
                if (ps != null) pagesize = ps.intValue();
                boolean a = true;
                if (asc != null) a = asc.booleanValue();
                System.out.println("SearchManager:result type is "+clz.getName() );
                searchResult = shell.searchFor(dShell.deploymentFor(flow).deployedName(),
                                                clz, query, Integer.MAX_VALUE, pagenum, pagesize, sortby, a);
            
                if (searchResult != null)
                {
                    System.out.println("ResultSet size:"+searchResult.size());
                    TransitionContext ctx = (TransitionContext)threads().threadContext();
                    if (ctx != null)
                    {
                        for(Object res : searchResult)
                        {
                            SmartDataED ed = ctx.atomicity().includeData((SmartData)res);
                            result.add(ed.empirical());
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean searchData(String flow, String group, List result, String byOwner, String status, String srchGroup)
        throws CtxException
    {
        Map<String, String> query = new HashMap<String, String>();
        if ((byOwner != null) && (!byOwner.equals("Any")))
        {
            query.put("___smart_legend___._ownedBy", byOwner);
        }

        if ((status != null) && (!status.equals("Any")))
        {
            String[] stats = status.split("\\|");
            String statsrch = "";
            String add = "";
            for (int i = 0; i < stats.length; i++)
            {
                statsrch = statsrch + add + stats[i];
                add = " OR ";
            }
            query.put("___smart_currentState___._stateName", "(" + statsrch + ")");
        }

        if ((srchGroup != null) && (!srchGroup.equals("Any")))
            query.put("___smart_legend___._group", srchGroup);

        searchService(flow, group, query, result, null, null, null, null);
        return false;
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
        String space = dShell.deploymentFor(flow).deployedName();
        String datatype = null;
        if (listEvent.isConfig())
            space = TenantConstants.CONFIG_SPACE;

        long start = listEvent.getStartTime();
        List res = new ArrayList();
        if (start <= 0)
        {
            res = shell.listAll(space, listEvent.getGroup(), listEvent.getSize(), datatype);
        }
        else
        {
            ListParams parms = new ListParams(listEvent.getGroup(), datatype, listEvent.getSize(), start, listEvent.getEndTime());
            res = shell.listAll(space, parms);
        }

		System.out.println("List All Event returned "+res.size()+" results");
		System.out.println("----------------Lookup Result:"+res);

		ListAllResponse resp = new ListAllResponse(res);
	}
	
	public void getListings(GetListings event)
	    throws CtxException
	{
	    assertion().assertNotNull(event, "SearchManager: GetListings event is NULL");
	    assertion().assertNotNull(event.getGroup(), "SearchManager:GetListings: Group is Not specified");
	    
	    CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
        assertion().assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
        CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(tenant.deploymentShell());
        Object o = event;
        SmartEvent sevt = (SmartEvent)o;
        String flow = sevt.smart___flowname();
        List res = shell.getListings(dShell.deploymentFor(flow).deployedName(),
                event.getGroup(), event.getSortBy(), event.getListingsPerPage(), event.getPageNum());
        System.out.println("List All Event returned "+res.size()+" results");
        System.out.println("----------------Lookup Result:"+res);

        ListAllResponse resp = new ListAllResponse(res);

	}
}
