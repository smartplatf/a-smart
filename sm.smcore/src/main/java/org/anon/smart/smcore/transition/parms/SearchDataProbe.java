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
 * File:                org.anon.smart.smcore.transition.parms.SearchDataProbe
 * Author:              rsankar
 * Revision:            1.0
 * Date:                20-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A probe that searches for a given data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.parms;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Type;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.gconcurrent.execute.ParamType;
import org.anon.utilities.gconcurrent.execute.PDescriptor;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.PProbe;
import org.anon.utilities.exception.CtxException;

public class SearchDataProbe implements AtomicityConstants, PProbe
{
    public SearchDataProbe()
    {
    }

    private Object getKeyFor(String[] lnks, ProbeParms parms, String attribute)
        throws CtxException
    {
        if (lnks.length == 2)
            return attribute; //assume it is a constant and  use it

        System.out.println("Got links: " + lnks.length);
        for (int i = 0; i < lnks.length; i++)
            System.out.println(":" + lnks[i]);
        Object val = null;
        if (lnks.length > 2)
        {
            ParamType t = ParamType.valueOf(lnks[2]);
            assertion().assertNotNull(t, "Cannot find the provided param type. " + lnks[2]);
            String pstr = "(" + lnks[2] + "." + attribute + ")";
            List<PDescriptor> desc = PDescriptor.parseParamDesc(pstr);
            PProbe p = t.myProbe();
            val = p.valueFor(parms, null, desc.get(0));
            System.out.println("Getting the value for: " + pstr + ":" + p + ":" + val);
        }

        return val;
    }

    public Object valueFor(Class cls, Type t, ProbeParms parms, PDescriptor desc)
        throws CtxException
    {
        if (desc != null)
        {
            RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
            String[] links = desc.links();
            String flow = links[0];
            assertion().assertNotNull(flow, "Need to provide the type of object to search");
            assertion().assertTrue((links.length >= 2), "Need to provide an object to search");
            String type = links[1];

            Object key = getKeyFor(links, parms, desc.attribute());
            System.out.println("The key got for: " + links.length + ":" + parms + ":" + key + ":" + flow + ":" + type);
            if (key != null)
            {
                SmartData data = (SmartData)rshell.lookupFor(flow, type, key);
                System.out.println("The key got for: " + links.length + ":" + parms + ":" + key + ":" + flow + ":" + type);
                TransitionContext ctx = (TransitionContext)threads().threadContext();
                if ((ctx != null) && (data != null))
                {
                    SmartDataED ed = ctx.atomicity().includeData(data);
                    return ed.empirical();
                }
            }
        }
        return null;
    }

    public Object valueFor(Class cls, Type type, ProbeParms parms)
        throws CtxException
    {
        //if descriptor is not given then we do not know what to search.
        return null;
    }

    public Object valueFor(ProbeParms parms, Type type, PDescriptor desc)
        throws CtxException
    {
        return null;
    }

    public void releaseValues(Object[] val)
        throws CtxException
    {
    }
}

