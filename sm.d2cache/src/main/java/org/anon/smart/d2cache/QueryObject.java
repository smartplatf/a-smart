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
 * File:                org.anon.smart.d2cache.QueryObject
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A query used to search objects
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.anon.utilities.exception.CtxException;
import org.anon.utilities.services.ServiceLocator;
import org.omg.PortableServer.ServantLocator;

public class QueryObject
{
	public class QueryItem 
    {
		private String attribute;
		private String value;
		//private boolean mandatory;
		
		public QueryItem(String attr, String val) 
        {
			this(attr, val, true);
		}
		public QueryItem(String q)
		{
			String[] tokens = q.split(":", 2);
			attribute = tokens[0];
			value = tokens[1];
			
		}
		
		public QueryItem(String attr, String val, boolean mandatory) 
        {
			attribute = attr;
			value = val;
		}

		public String attribute()
        {
			return attribute;
		}

        public String value()
        {
            return value;
        }
	}

	private List<QueryItem> query;
	private Class _resultType;
    private long totalFound;
	
	public QueryObject() 
    {
		query = new ArrayList<QueryItem>();
	}
	
	public void addCondition(String attr, String val, boolean mandatory)
        throws CtxException 
    {
		query.add(new QueryItem(attr, val, mandatory));
	}

	public void addCondition(String cond, String val) 
        throws CtxException 
    {
		addCondition(cond, val, true);
	}
	public void addConditions(Map<String, String> queryMap) throws CtxException
	{
		for(String s : queryMap.keySet())
		{
			addCondition(s, queryMap.get(s), true);
		}
	}
	public void setResultType(Class cls)
	{
		_resultType = cls;
	}
	public Class getResultType() 
	{
		return _resultType;
	}
	public List<QueryItem> getQuery() { return query;}

	public void setResultType(String group) throws CtxException {
		try {
			_resultType = Class.forName(group);
		} catch (ClassNotFoundException e) {
			ServiceLocator.except().rt(e, "Class"+ group + " not found", null);
		}
	}

	public void addCondition(String q) {
		query.add(new QueryItem(q));
		
	}

    public void setNumFound(long tot)
    {
        totalFound = tot;
    }

    public long getTotalFound()
    {
        return totalFound;
    }
}


