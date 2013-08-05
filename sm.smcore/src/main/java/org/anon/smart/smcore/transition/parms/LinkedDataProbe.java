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
 * File:                org.anon.smart.smcore.transition.parms.LinkedDataProbe
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A probe that retrieves related data for a transition
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.parms;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Type;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.data.datalinks.DataLinker;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.gconcurrent.execute.PDescriptor;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.PProbe;
import org.anon.utilities.exception.CtxException;

public class LinkedDataProbe implements AtomicityConstants, PProbe
{
    public LinkedDataProbe()
    {
    }

    public Object valueFor(Class cls, Type type, ProbeParms parms, PDescriptor desc)
        throws CtxException
    {
        if (desc != null)
        {

        }
        //TODO
        return null;
    }

    public Object valueFor(Class cls, Type type, ProbeParms parms)
        throws CtxException
    {
        //if the descriptor is not given it is assumed to be related in the current flow.
        TransitionProbeParms tparms = (TransitionProbeParms)parms;
        //when nothing is specified, relation is searched in the flow for which this
        //event is posted if not present then no data is retrieved
        SmartPrimeData prime = tparms.primeData();
        String flow = tparms.event().smart___flowname();
        DataLinker linker = new DataLinker();
        List<SmartData> data = linker.getLinked(cls, type, prime);

        System.out.println("Searched for type: " + cls + ":" + data);

        if ((data == null) || (data.size() <= 0))
            return null; //let it be picked up and handled by others

        List <SmartData> ret = new ArrayList<SmartData>(); //can this list be greater than some value X?? we need to validate
        for (SmartData d1 : data)
        {
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            if (ctx != null)
            {
                SmartDataED ed = ctx.atomicity().includeData(d1);
                ret.add(ed.empirical());
            }
        }

        //return the empirical data
        if (type().isAssignable(cls, Collection.class)) //if it is a collection just pass what we have.
            return ret;

        SmartData d = ret.get(0);

        //we pick up and pass the first one, not sure which to pass,
        //have to handle it as a list in the transition, but shd we
        //throw exception?
        if (type().isAssignable(d.getClass(), cls))
            return d;

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

