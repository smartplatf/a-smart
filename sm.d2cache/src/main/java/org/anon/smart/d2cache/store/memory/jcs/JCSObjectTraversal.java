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
 * File:                org.anon.smart.d2cache.store.memory.jcs.JCSObjectTraversal
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 7, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.memory.jcs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.reflect.TVisitor;

public class JCSObjectTraversal implements TVisitor{

	  private Map<String, List<Object>> _traversed;
	  private StoreRecord _rec;

	public JCSObjectTraversal(StoreRecord rec)
	{
		_traversed = new HashMap<String, List<Object>>();
        _rec = rec;
	}
	@Override
	public Object visit(DataContext ctx) throws CtxException {
		Object ret = null;
        List<Object> lst = null;
        if (ctx.field() != null)
        {
            ret = ctx.fieldVal();
        }
        else
        {
        	ret = ctx.traversingObject();
        }
        _rec.append(ctx);
        
        return ret;

	}

}
