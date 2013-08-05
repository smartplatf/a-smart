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
 * File:                org.anon.smart.d2cache.store.index.solr.SolrQueryConstructor
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 14, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

import java.lang.reflect.Field;
import java.util.List;

import org.anon.smart.d2cache.QueryObject;
import org.anon.smart.d2cache.QueryObject.QueryItem;
import org.anon.utilities.exception.CtxException;
import static org.anon.utilities.services.ServiceLocator.*;
import org.apache.solr.client.solrj.SolrQuery;

public class SolrQueryConstructor implements Constants{

	public static final String QUERY_DELIM = ":";
	public static final String DEFUALT_OPER = " AND ";
	public static SolrQuery getQuery(String group, QueryObject qo)
		throws CtxException
	{
		List<QueryItem> condList = qo.getQuery();
		StringBuffer q = new StringBuffer();
		
		assertion().assertTrue((qo.getResultType() != null), "ResultType is NOT set on QueryObject");
		
		String condition = getQueryCondition(condList.get(0), qo.getResultType(), group);
		q.append(condition);
		for(int i = 1;i< condList.size();i++)
		{
			QueryItem cond = condList.get(i);
			condition = getQueryCondition(cond,  qo.getResultType(), group);
			q.append(DEFUALT_OPER+condition);
		}
		System.out.println("SolrQuery:"+q.toString());
		return new SolrQuery(q.toString());
		
	}

	private static String getQueryCondition(QueryItem queryItem,
			Class resultType, String group) {
		StringBuffer condition = new StringBuffer();
		String attribute = queryItem.attribute();
		if(attribute.equals(ID_COLUMN))
			condition.append(attribute);
		else
			condition.append(group+PART_SEPARATOR+attribute);
		String fldSuffix = getFieldSuffix(attribute, resultType);
		if(fldSuffix != null)
			condition.append(fldSuffix);
		
		condition.append(QUERY_DELIM+queryItem.value());
		
		return condition.toString();
	}

	private static String getFieldSuffix(String attribute, Class resultType) {
		
		Field[] flds = resultType.getDeclaredFields();
		for(int i = 0 ; i<flds.length;i++)
		{
			if(flds[i].getName().equals(attribute))
			{
				return SolrRecord.SUFFIXES.get(flds[i].getType());
			}
		}
		if(resultType.getSuperclass()!= null)
			return getFieldSuffix(attribute, resultType.getSuperclass());
		
		return null;
	}
}
