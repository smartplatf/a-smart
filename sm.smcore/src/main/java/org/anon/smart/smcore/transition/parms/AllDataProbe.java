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
 * File:                org.anon.smart.smcore.transition.parms.AllDataProbe
 * Author:              rsankar
 * Revision:            1.0
 * Date:                25-02-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A probe that retrieves all the data as a list
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

//Use with caution. This will retrieve all the data which can become
//non-performant
public class AllDataProbe implements AtomicityConstants, PProbe
{
    public AllDataProbe()
    {
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
            //assertion().assertTrue((links.length >= 2), "Need to provide an object to search");
            String type = desc.attribute();
            if (links.length > 1)
                type = links[1];

            System.out.println("The key got for: " + links.length + ":" + parms + ":" + ":" + flow + ":" + type);
            return getSmartData(flow, type, rshell);
        }
        return null;
    }

    private Object getSmartData(String flow, String type, RuntimeShell rshell)
        throws CtxException
    {
        List<Object> data = rshell.listAll(flow, type, Integer.MAX_VALUE, type);
        System.out.println("The key got for: " + ":" + ":" + flow + ":" + type);
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if ((ctx != null) && (data != null))
        {
            List<Object> ret = new ArrayList<Object>();
            for (int i = 0; i < data.size(); i++)
            {
                SmartDataED ed = ctx.atomicity().includeData((SmartData)data.get(i));
                ret.add(ed.empirical());
            }
            return ret;
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

