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
 * File:                org.anon.smart.d2cache.store.repository.hbase.ObjectCreatorFromMap
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 24, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import java.lang.reflect.Field;

import static org.anon.utilities.objservices.ObjectServiceLocator.convert;
import static org.anon.utilities.services.ServiceLocator.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.anon.utilities.exception.CtxException;
import org.anon.utilities.reflect.CreatorFromMap;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.reflect.ListItemContext;

public class ObjectCreatorFromMap extends CreatorFromMap implements Constants{

	public ObjectCreatorFromMap(Map values) {
		super(values);
	}

    @Override
    public int collectionSize(DataContext ctx)
    {
        int ret = 0;
        
        if (ctx.getCustom() instanceof Map)
        {
        	
            Map check = (Map)ctx.getCustom();
            Field fld = ctx.field();
            
            if(fld != null)
            {
            	ret = convert().collectionSizeFromMap(check, fld.getName(), ctx.fieldType());
            }
        }
        else if (ctx instanceof ListItemContext)
        {
            //this is a listitemcontext
            Collection coll = convert().objectToCollection(ctx.getCustom(), false);
            if (coll != null)
                ret = coll.size();
        }

        return ret;
    }
	
	@Override
	public Object handleDefault(DataContext ctx)
	{
		Map checkIn = getContextMap(ctx);
		String key = ctx.traversingClazz().getSimpleName()+"."+ctx.fieldpath();
   	 	//System.out.println("HandleDefault:"+key+":::::"+checkIn);
       if ((ctx.field() != null) && (checkIn != null) && (checkIn.containsKey(key)))
       {
    	   Object val = checkIn.get(key);
           if ((val != null) && (type().isAssignable(val.getClass(), ctx.fieldType())))
           {
        	   return val;
           }
           else if((val != null) && (type().checkPrimitive(ctx.fieldType()) ||  type().isWrapperType(ctx.fieldType()) ))
           {
           		return type().convertToPrimitive(ctx.fieldType(), (byte[])val);     	
           }
           
           else if((val != null) && type().convertableFromString(ctx.fieldType()))
           {
        	   return type().createObjectFromString(ctx.fieldType(), new String((byte[])val));
           }
           
           /* Adding this and should be changed */
           if((val != null) && val instanceof byte[])
           {
        	   Object values = createMapForMember(checkIn, ctx.fieldType(), ctx.field().getName());
        	   Object obj = null;
        	   String actualFldType = "";
        	   actualFldType = ctx.fieldType().getName();
        	   if(HBaseUtil.isFieldTypeNeeded(ctx.fieldType()))
        	   {
        		   if(checkIn.get(key+FIELDTYPE) != null)
            	   {
            		   actualFldType = new String(((byte[])checkIn.get(key+FIELDTYPE)));
            		   System.out.println("Actual Field Type:"+actualFldType);
                	   
            	   }
        		   
        	   }
        	  
        	   //System.out.println("Setting Field PaRAM:"+ctx.fieldType()+":::"+checkIn.get(key)+":::"+key+":::"+actualFldType+"::CTX:"+ctx);
          	  
        	   //ctx.setParamTypeForField(paramType);
        	   /* Create the fld Obj in here and set it on ctx */
        	   
			try {
				obj = SilentObjectCreator.create(actualFldType);
				//System.out.println("Silent Creation of Obj:"+obj);
			} catch (ClassNotFoundException e1) {
				//e1.printStackTrace();
			} catch (CtxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   try {
				ctx.modify(obj);
			} catch (CtxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   ctx.setCustom(values);
        	   return values;
           }

           if ((val != null) && (val instanceof Map))
           {
               ctx.setCustom(val);
               return val;
           }

           if ((val != null) && ((val instanceof Collection) || (val.getClass().isArray())))
           {
               ctx.setCustom(val);
               return val;
           }
       }

       return null;
   }

	@Override
	public Object handleListItem(ListItemContext lctx)
    {
        Map checkIn = getContextMap(lctx);
       // System.out.println("CheckIn:"+checkIn+"::"+lctx.getCount());
        Object val = null;
        if (checkIn != null)
        {
        	//Get Map for ListItem
            val = convert().mapForCollectionItem(checkIn, lctx);
        }
        else if (lctx.getCustom() instanceof List)
        {
            val = ((List)lctx.getCustom()).get(lctx.getCount());
        }
        
       // System.out.println("Val:"+val+":COUNT:"+lctx.getCount());
        
            lctx.setCustom(val);
            return val;
        
    }
	private Object createMapForMember(Map checkIn, Class fieldType, String fldName) {
		Map<String, Object> memberMap = new HashMap<String, Object>();
		
		for(Object key : checkIn.keySet())
		{
			
			String[] tokens = ((String)key).split("\\.", 2);
			if((tokens.length > 1) && ( tokens[1].startsWith(fldName)))
			{
				StringBuffer modKey = new StringBuffer(fieldType.getSimpleName());
				modKey.append(".");
			
				modKey.append(tokens[1]);
			
				memberMap.put(modKey.toString(), checkIn.get(key));
			}
		}
		
		return memberMap;
	}
	}
