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
 * File:                org.anon.smart.base.stt.tl.BaseTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A base for all TLs used
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.tl;

import java.util.Map;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

import org.anon.smart.base.stt.Constants;
import org.anon.smart.base.annot.BaseAnnotate;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public abstract class BaseTL implements Constants
{
    static
    {
        try
        {
            TemplateReader.registerTemplate("SmartData", SmartObjectTL.class);
            TemplateReader.addAnnotates(BaseAnnotate.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected String name;
    protected String type;
    protected String flow;

    protected BaseTL()
    {
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getFlow() { return flow; }
    public String[] getTypes()
        throws CtxException
    { 
        return value().rangeAsString(type); 
    }

    public Class[] getAnnotations(String name)
        throws CtxException
    {
        return TemplateReader.getAddedAnnotates();
    }

    public Object findValue(String nm)
        throws CtxException
    {
        Object val = null;
        try
        {
            Field fld = reflect().getAnyField(this.getClass(), nm);
            if (fld != null)
            {
                fld.setAccessible(true);
                val = fld.get(this);
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Problem in accessing field: " + nm, e.getMessage()));
        }
        return val;
    }

    static String[] getTypes(Map values)
        throws CtxException
    {
        assertion().assertTrue(values.containsKey(TYPE_CONFIG), "Not a valid config to be read");
        String str = values.get(TYPE_CONFIG).toString();
        return value().rangeAsString(str);
    }

    protected static String populateDefault(BaseTL tl, String clsname, String t, String f)
    {
        int ind = clsname.lastIndexOf(".");
        tl.name = clsname.substring(ind + 1);
        tl.type = t;
        tl.flow = f;

        return tl.name;
    }

    public boolean shouldAdd(String type)
    {
        return false;
    }

    public String[] getExtras()
        throws CtxException
    {
        return TemplateReader.getExtraTypes();
    }
}

