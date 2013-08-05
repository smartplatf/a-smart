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
 * File:                org.anon.smart.d2cache.segment.CacheObjectTraversal
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 12, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.segment;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.reflect.TVisitor;

public class CacheObjectTraversal implements TVisitor {

	  private Map<String, List<Object>> _traversed;
	  private List<StoreRecord> _recList;
	  
	  public CacheObjectTraversal (List<StoreRecord> recList){
		  _traversed = new HashMap<String, List<Object>>();
		  _recList = recList;
	  }
	public CacheObjectTraversal(StoreRecord rec) {
		_traversed = new HashMap<String, List<Object>>();
		_recList = new ArrayList<StoreRecord>();
		_recList.add(rec);
	}
	@Override
	public Object visit(DataContext ctx)
	        throws CtxException
	{
		
	        Object ret = null;
	        List<Object> lst = null;
	        if (ctx.field() != null)
	        {
	        	int modifiers = ctx.field().getModifiers();
                if (Modifier.isTransient(modifiers) || (Modifier.isStatic(modifiers)))
                    return null; //do not continue further

	            lst = _traversed.get(ctx.field().getName());
	            if (lst == null)
	                lst = new ArrayList<Object>();
	            lst.add(ctx.fieldVal());
	            _traversed.put(ctx.field().getName(), lst);
	            ret = ctx.fieldVal();
	        }
	        else
	        {
	            lst = _traversed.get(ctx.traversingClazz().getName());
	            if (lst == null)
	                lst = new ArrayList<Object>();
	            lst.add(ctx.traversingObject());

	            _traversed.put(ctx.traversingClazz().getName(), lst);
	            ret = ctx.traversingObject();
	        }
	        for(StoreRecord rec : _recList) {
	        	rec.append(ctx);
	        }
	        
	        return ret;
	    }

	    public Map<String, List<Object>> getTraversed()
	    {
	        return _traversed;
	    }

}
