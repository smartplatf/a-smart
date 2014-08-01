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

import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.anon.smart.d2cache.QueryObject;
import org.anon.smart.d2cache.QueryObject.QueryItem;
import org.anon.utilities.exception.CtxException;
import static org.anon.utilities.services.ServiceLocator.*;
import org.apache.solr.client.solrj.SolrQuery;

public class SolrQueryConstructor implements Constants{

	public static final String QUERY_DELIM = ":";
	public static final String DEFUALT_OPER = " AND ";
	public static SolrQuery getQuery(String group, QueryObject qo, int size, int pn, int ps, String sby, boolean asc)
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
		System.out.println("SolrQuery:"+q.toString() + ":" + size);

        if (pn < 0) pn = 0;
        if (ps <= 0) ps = -1;

        int start = 0;
        if (ps > 0) start = (pn * ps);

        int rows = size;
        if (ps > 0) rows = ps;
		SolrQuery sq = new SolrQuery(q.toString());
        sq.setStart(start);
        sq.setRows(rows);
        if ((sby != null) && (sby.length() > 0))
        {
            String sort = getSortBy(sby, qo.getResultType(), group);
            if (asc)
                sq.addSortField(sort, SolrQuery.ORDER.asc);
            else
                sq.addSortField(sort, SolrQuery.ORDER.desc);
        }

		return sq;
	}

    private static String getSortBy(String attribute, Class resultType, String group)
    {
        StringBuffer condition = new StringBuffer();
		if(attribute.equals(ID_COLUMN))
			condition.append(attribute);
		else
			condition.append(group+PART_SEPARATOR+attribute);
        String[] path = attribute.split("\\.");
		String fldSuffix = getFieldSuffix(path, resultType, false);
		if(fldSuffix != null)
			condition.append(fldSuffix);

        return condition.toString();
    }

	private static String getQueryCondition(QueryItem queryItem,
			Class resultType, String group) {
		StringBuffer condition = new StringBuffer();
		String attribute = queryItem.attribute();
		if(attribute.equals(ID_COLUMN))
			condition.append(attribute);
		else
			condition.append(group+PART_SEPARATOR+attribute);

        String[] path = attribute.split("\\.");
		String fldSuffix = getFieldSuffix(path, resultType, false);
		if(fldSuffix != null)
			condition.append(fldSuffix);
		
        String add = "";
        //This is just a work around!!! Has to be changed completely to work with lists
        //and other data types. Whoever codes a string into a value!!! THis is a pure hack
        //so I can go forward. Will have to come back and Fix this. TODO:
        if ((queryItem.value() != null) && (queryItem.value().toString().contains(" ")) && (!queryItem.value().toString().startsWith("(")))
            add = "\"";
		condition.append(QUERY_DELIM + add + queryItem.value() + add);
		
		return condition.toString();
	}

	private static String getFieldSuffix(String[] attribute, Class resultType, boolean multiple) {
		
		Field[] flds = resultType.getDeclaredFields();
		for(int i = 0 ; i<flds.length;i++)
		{
			if(flds[i].getName().equals(attribute[0]))
			{
                boolean multi = multiple;
                if (!multiple && (flds[i].getGenericType() instanceof ParameterizedType))
                    multi = true;

                if (attribute.length > 1)
                {
                    //need to go next level and search
                    String[] nxtsrch = new String[attribute.length - 1];
                    for (int j = 1; j < attribute.length; j++)
                        nxtsrch[j - 1] = attribute[j];

                    return getFieldSuffix(nxtsrch, flds[i].getType(), multi);
                }

                if (multiple)
                    return SolrRecord.LIST_SUFFIXES.get(flds[i].getType());
                else
                    return SolrRecord.SUFFIXES.get(flds[i].getType());
			}
		}
		if(resultType.getSuperclass()!= null)
			return getFieldSuffix(attribute, resultType.getSuperclass(), multiple);
		
		return null;
	}
}
