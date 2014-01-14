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
 * File:                org.anon.smart.smcore.channel.distill.translation.TranslationStage
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A stage where data gets translated to or from commn data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.translation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.utils.AnnotationUtils;
import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.RData;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.distill.Distillation;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.smcore.channel.server.EventPData;
import org.anon.smart.smcore.channel.distill.alteration.AlteredData;
import org.anon.smart.smcore.channel.internal.MessagePData;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.CrossLinkSmartPrimeData;
import org.anon.smart.smcore.data.SmartPrimeData;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.objservices.ConvertService.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;


public class TranslationStage implements Distillation
{
    private translator _type;
    private Rectifier _myRectifier;

    public TranslationStage(translator t)
    {
        _type = t;
    }

    TranslationStage()
    {
        _type = translator.json; //custom send back is always json, need to check if this has to change
    }

    public void setRectifier(Rectifier parent)
    {
        _myRectifier = parent;
    }

    public Distillate distill(Distillate prev)
        throws CtxException
    {
    	if(prev.current() instanceof MessagePData)
    	{
    		
    		MessagePData data = (MessagePData)prev.current();
    		
    		Map<String, Object> convert = objectToMapForInternalMessage(data.event());  
    		/*//TEMP
    		Map<String, Object> intMap = new HashMap<String, Object>();
    		
    		Map dest = new HashMap();
    		dest.put("___smart_action___", "lookup");
    		dest.put("___smart_value___", "vjaasti@gmail.com");
    		
    		intMap.put("Registration", dest);
    		//TEMP END    		
*/    		Isotope translated = new MapData(data, convert);
    		return new Distillate(prev, translated);
    		
    	}
    	
        PData data = (PData)prev.current();
        InputStream str = data.cdata().data();
        Object convert = convert().readObject(str, Map.class, _type);
        System.out.println("Converted: " + str + ":" + convert);
        Isotope translated = null;
        if (convert instanceof Map)
            translated = new MapData(data, (Map<String, Object>)convert);
        else
            translated = new ObjectData(data, convert);

        return new Distillate(prev, translated);
    }

    public Distillate condense(Distillate prev)
        throws CtxException
    {
        MapData map = (MapData)prev.current();
        //Map<String, Object> send = map.translated();
        //Trying. instead of map
        AlteredData adata = (AlteredData)prev.from().from().current();
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        List<AlteredData.FlowEvent> evts = adata.events();
        for (AlteredData.FlowEvent evt : evts)
        {
            Object resp = evt.event();
            System.out.println("Converting: " + resp.getClass() + ":" + _type);
            convert().writeObject(resp, ostr, _type);
        }
        byte[] bytes = ostr.toByteArray();
        System.out.println("Sending across: " + new String(bytes));
        try
        {
            ostr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ByteArrayInputStream istr = new ByteArrayInputStream(bytes);
        ContentData cdata = new ContentData(istr);

        RData rdata = null;
        Distillate start = prev.from();
        while((start != null) && (start.from() != null))
            start = start.from();

        rdata = (RData)start.current();
        PData origpdata = (PData)start.current().isotope();

        PData prime = new EventPData(origpdata.dscope(), cdata);
        prime.setIsotopeOf(rdata);
        return new Distillate(prev, prime);
    }

    public boolean distillFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof PData);
    }

    public boolean condenseFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof MapData);
    }

    //For now have to do this. But this definitely has to change.
    //The object travesal was introduced just to avoid these kind of bugs
    //but looks like it is very difficult to get anyone to follow stds around here
    //Vinay did a big mistake of not using the object 
    //traversal, so for now putting in a quick fix. RS
    private void addSuperFields(Object event, Class cls, Map addTo)
        throws CtxException
    {
        try
        {
            Field[] flds = cls.getDeclaredFields();
            for(Field f : flds)
            {
                f.setAccessible(true);
                int mod = f.getModifiers();
                if (Modifier.isTransient(mod) || Modifier.isStatic(mod))
                    continue;
                Object fldVal = f.get(event);
                Class fType = f.getType();
                if (fldVal != null)
                    fType = fldVal.getClass();
                if((fldVal != null) && (!(f.getName().startsWith("___smart"))))
                {
                    addToMap(f.getName(), fType, fldVal, addTo);
                }
            }

            if (cls.getSuperclass() != null)
                addSuperFields(event, cls.getSuperclass(), addTo);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Exception", e.getMessage()));
        }
    }
    
    private Map<String, Object> objectToMapForInternalMessage(Object event) 
    	throws CtxException
    {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] flds = event.getClass().getDeclaredFields();
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		CrossLinkDeploymentShell dshell = tenant.deploymentShell();
        String nm = objectName(event);
		List<CrossLinkFlowDeployment> fds = dshell.flowForType(nm);
        assertion().assertTrue(fds.size() > 0, "Cannot find deployment for: " + nm);
        CrossLinkFlowDeployment fd = fds.get(0);
		
		String  flow = fd.deployedName(); 
        System.out.println("Getting deployment for flow: " + flow);
        Field dest = AnnotationUtils.destinations(event.getClass());
        if (dest != null)
        {
            dest.setAccessible(true);
            Object val = reflect().getAnyFieldValue(event.getClass(), event, dest.getName());
            assertion().assertNotNull(val, "Destination value for " + dest.getName() + " is NULL in INTERNAL event.");
            flow = AnnotationUtils.flowFor(val.getClass());
            System.out.println("Got destination field as: " + dest + ":" + flow);
        }
		for(Field f : flds)
		{
			f.setAccessible(true);
            int mod = f.getModifiers();
            if (Modifier.isTransient(mod) || Modifier.isStatic(mod))
                continue;
			Object fldVal = reflect().getAnyFieldValue(event.getClass(), event, f.getName());
			Class fType = f.getType();
            if (fldVal != null)
                fType = fldVal.getClass();
			Class dCls = null;
            String clsName = AnnotationUtils.crossClassName(fType);
			if(clsName != null)
				dCls = dshell.dataClass(flow, clsName);
			
			if(dCls != null) // FLD IS DATA CLASS
			{
				assertion().assertNotNull(fldVal, "Data field "+ f.getName() +" is NULL in internal event");
				
				//assertion().assertTrue((fldVal instanceof DSpaceObject), "Fld value is not DSpaceObject"); 
				Map dataObj = new HashMap();
				CrossLinkSmartPrimeData dspaceObj = new CrossLinkSmartPrimeData(fldVal);
	    		dataObj.put("___smart_action___", "lookup");
	    		dataObj.put("___smart_value___", dspaceObj.smart___keys().get(1)); //TODO passing User key instead of smart_id
	    		map.put(clsName, dataObj);
			}
			else if((fldVal != null) && (!(f.getName().startsWith("___smart"))))
			{
                addToMap(f.getName(), fType, fldVal, map);
			}
		}

        if (event.getClass().getSuperclass() != null)
            addSuperFields(event, event.getClass().getSuperclass(), map);
		
		System.out.println("Converted for INTERNAL MESSAGE:"+map);
		return map;
	}

    private void addToMap(String name, Class fType, Object fldVal, Map map)
        throws CtxException
    {
        if(type().checkPrimitive(fType))
        {
            map.put(name, fldVal);
        }
        else if (fldVal instanceof Collection)
        {
            map.put(name, convert().listObjectsToMap((Collection)fldVal));
        }
        else
        {
            map.put(name, convert().objectToMap(fldVal));
        }
    }
}

