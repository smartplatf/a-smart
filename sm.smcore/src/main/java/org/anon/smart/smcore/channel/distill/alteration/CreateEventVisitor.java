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
 * File:                org.anon.smart.smcore.channel.distill.alteration.CreateEventVisitor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A visitor that creates event data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.alteration;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.channel.distill.sanitization.SearchedData;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.reflect.CreatorFromMap;
import org.anon.utilities.exception.CtxException;

public class CreateEventVisitor extends CreatorFromMap
{
    private SearchedData _searched;

    public CreateEventVisitor(SearchedData data, SearchedData.PrimeFlow prime) throws CtxException
    {
        super(data.mappedData());
        _searched = data;
        _searched.setupSearchMap(prime);
    }

    @Override
    public Object visit(DataContext ctx)
        throws CtxException
    {
        if ((ctx.field() != null) && (_searched.isSearched(ctx.field().getName())))
        {
            Object val = _searched.searchedValue(ctx.field().getName());
            if (val == null)
                return val; //have to set to null.
            
            if (type().isAssignable(val.getClass(), ctx.fieldType()))
            {
                //directly set here, so we do not create a new one
                ctx.modify(val);
                return null; //directly set, so don't traverse inside
            }
            
            if ((val instanceof List) && (((List)val).size() > 0))
            {
                List<Object> coll = (List<Object>)val;
                Object collval = coll.get(0);
                if (type().isAssignable(collval.getClass(), ctx.fieldType()))
                {
                    ctx.modify(collval);
                    return collval;
                }
            }
        }
        else if ((ctx.field() != null) && (type().isAssignable(ctx.fieldType(), SmartData.class)))
        {
            //this means we did not search because of various reasons. just return null
            return null;
        }
        else if ((ctx.field() != null) && (type().isAssignable(ctx.fieldType(), Map.class))) //if Map, then it means we r assigning directly
        {
            //need to sanitize the map?
            Map map = getContextMap(ctx);
            Object val = map.get(ctx.field().getName());
            Map m = refactorJSONArray((Map)val);
            if (m != null)
            {
                ctx.modify(m);
                return null;
            }
        }

        return super.visit(ctx);
    }

    private Map refactorJSONArray(Map vals)
    {
        if (vals == null)
            return vals;

        Map ret = new HashMap();
        for (Object k : vals.keySet())
        {
            System.out.println("refactor: " + vals.get(k) + ":" + (vals.get(k) instanceof JSONArray) + ":" + k + ":" + vals.get(k).getClass().getName());
            if (vals.get(k) instanceof JSONArray)
            {
                ArrayList lst = new ArrayList();
                lst.addAll((JSONArray)vals.get(k));
                ret.put(k, lst);
                System.out.println("Changing the values for: " + k + ":" + lst + ":"+ ret.get(k).getClass().getName() + ":" + lst.getClass().getName());
            }
            else if (vals.get(k) instanceof JSONObject)
            {
                Map m = new HashMap();
                m.putAll((JSONObject)vals.get(k));
                ret.put(k, m);
                System.out.println("Changing the values for: " + k + ":" + m + ":"+ ret.get(k).getClass().getName());
                refactorJSONArray((Map)vals.get(k));
            }
            else
                ret.put(k, vals.get(k));
        }

        return ret;
    }
}

