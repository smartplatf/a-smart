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
import static org.anon.utilities.services.ServiceLocator.except;
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
            	String sizeColumn = ctx.fieldType().getSimpleName()+PART_SEPARATOR+ctx.fieldpath()+SIZE;
            	if(check.get(sizeColumn) != null)
            	{
            		ret = byteArrayToInt((byte[])check.get(sizeColumn));
            		System.out.println("CollectionSize:"+ret);
            	}
            	else
            	{
            		ret = collectionSizeFromMap(check, fld.getName(), ctx.fieldType());
            	}
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
		throws CtxException
	{
		Map checkIn = getContextMap(ctx);
		String key = ctx.traversingClazz().getSimpleName()+"."+ctx.fieldpath();
   	    if ((ctx.field() != null) && (checkIn != null) && (checkIn.containsKey(key)))
        {
    	   Object val = checkIn.get(key);
           String actualFldType = "";
           actualFldType = ctx.fieldType().getName();
           if(HBaseUtil.isFieldTypeNeeded(ctx.fieldType()))
           {
               if(checkIn.get(key+FIELDTYPE) != null)
               {
                   actualFldType = new String(((byte[])checkIn.get(key+FIELDTYPE)));
                   //System.out.println("Actual Field Type:"+actualFldType + ":" + key + ":" + val);
               }
           }
           if(actualFldType.startsWith("[")) //TODO: arrays and Maps are not supported yet
           {
        	   except().te("DataTypeNotSupported:"+actualFldType);
           }
           Class actualFldClass = null;
           try
           {
               actualFldClass = this.getClass().getClassLoader().loadClass(actualFldType);
               //System.out.println("Actual Field Type:"+actualFldType + ":" + actualFldClass + ":");
               if(Map.class.isAssignableFrom(actualFldClass))
               {
            	   except().te("DataTypeNotSupported:"+actualFldType); //TODO: arrays and Maps are not supported yet
               }
           }
           catch(ClassNotFoundException e)
           {
        	   actualFldClass = ctx.fieldType();
        	   
           }
           catch (Exception e)
           {
               e.printStackTrace();
               return null;
           }
           if ((val != null) && (type().isAssignable(val.getClass(), actualFldClass)))
           {
        	   return val;
           }
           else if((val != null) && (type().checkPrimitive(actualFldClass) ||  type().isWrapperType(actualFldClass) ) )
           {
           		return type().convertToPrimitive(actualFldClass, (byte[])val);     	
           }
           
           else if((val != null) && type().convertableFromString(actualFldClass))
           {
        	   return type().createObjectFromString(actualFldClass, new String((byte[])val));
           }
           
           //System.out.println("Is an object: " + (val instanceof byte[]) + ":" + val);
           /* Adding this and should be changed */
           if((val != null) && val instanceof byte[])
           {
        	   Object values = createMapForMember(checkIn, actualFldClass, ctx.fieldpath());
        	   Object obj = null;
        	  /*String actualFldType = "";
        	   actualFldType = ctx.fieldType().getName();
        	   if(HBaseUtil.isFieldTypeNeeded(ctx.fieldType()))
        	   {
        		   if(checkIn.get(key+FIELDTYPE) != null)
            	   {
            		   actualFldType = new String(((byte[])checkIn.get(key+FIELDTYPE)));
            		   System.out.println("Actual Field Type:"+actualFldType);
                	   
            	   }
        		   
        	   }*/
        	  
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
	public Object handleListItem(ListItemContext lctx) throws CtxException
    {
        Map checkIn = getContextMap(lctx);
        //System.out.println("CheckIn:"+checkIn+"::"+lctx.getCount());
        Object val = null;
        //Return element from Map if Primitive list
        if(type().checkPrimitive(lctx.traversingClazz()))
        {
        	//System.out.println("Primtive List");
        	String type = null;
        	if(lctx.getActualFieldType() != null)
        		type = lctx.getActualFieldType().getSimpleName();
        	if((type == null) && (lctx.listField() != null))
        		type = lctx.listField().getType().getSimpleName();
        	String key = type +"."+lctx.fieldpath();
        	//System.out.println("------_KEY :"+key);
        	if(checkIn.get(key) != null)
        	{
	        	String listAsStr = new String((byte[])checkIn.get(key));
	        	String[] strList = trim(listAsStr);
	        	Object o = type().convertToPrimitive(lctx.traversingClazz(), strList[lctx.getCount()].getBytes());
	        	//System.out.println("ListItem:"+o);
	        	return o;
	        	
        	}
        	
        	return null;
        	
        }
        
        
        if (checkIn != null)
        {
        	//Get Map for ListItem
            val = mapForCollectionItem(checkIn, lctx);
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
	
	private String[] trim(String listAsStr)
	    	throws CtxException
	{
	    	//assuming listAsStr will be in [x, y, z] format
	    	//remove []
			if(listAsStr.length()<3)
			{
				return new String[0];
			}
	    	String[] stringList = listAsStr.substring(1, listAsStr.length()-1).split(", ");
	    	
	    	return stringList;
	}
	public int collectionSizeFromMap(Map check, String name, Class fldType) {
		String keyStr = null;
		int size = 0;
		for(Object key : check.keySet())
		{
			keyStr = (String)key;
			if(keyStr.equals(fldType.getSimpleName()+"."+name) )
			{
				
				String fldVal = new String((byte[]) check.get(keyStr));
				if((fldVal != null) && (fldVal.length() > 2))
				{
					String[] tokens = fldVal.split(",");
				 	size =  tokens.length;
				}
				
				break;
			}
			
				
		}
		System.out.println("LIST SIZE.....:"+size);
		 
		return size;	
	}
	
	public Object mapForCollectionItem(Map checkIn, ListItemContext lctx) {
		Map<String, Object> mapForCollection = new HashMap<String, Object>();
		for(Object key : checkIn.keySet())
		{
			String keyStr = (String)key;
				
			String fieldType = null;
			if( lctx.getActualFieldType() != null) 
				fieldType = lctx.getActualFieldType().getSimpleName();
			if((fieldType == null) && (lctx.listField() != null))
				fieldType = lctx.listField().getType().getSimpleName();
			if((keyStr.startsWith(fieldType+"."+lctx.listField().getName())))
						
			{
				String[] tokens = keyStr.split("\\.", 4);
				if((tokens.length == 4) && (tokens[2].equals(Integer.toString(lctx.getCount()))))
				{
					
					mapForCollection.put(lctx.traversingClazz().getSimpleName()+"."+lctx.listField().getName()+"."+tokens[3],
							checkIn.get(key));
				}
				
			}
		}
		
		return mapForCollection;
	}
	
	public int byteArrayToInt(byte[] b) 
	{
    		int value = 0;
    		String str = new String(b);
    		value = new Integer(str);
    		return value;
	}
}
