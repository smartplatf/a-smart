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

import org.anon.utilities.exception.CtxException;

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
	private Class resultType;
	
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
	
	public void setResultType(Class cls)
	{
		resultType = cls;
	}
	public Class getResultType() 
	{
		return resultType;
	}
	public List<QueryItem> getQuery() { return query;}
}
