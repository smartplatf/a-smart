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
 * File:                org.anon.smart.smcore.channel.distill.alteration.AlterationStage
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A stage where data is altered to an event
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.alteration;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.channels.distill.Distillation;

import org.anon.smart.smcore.channel.distill.sanitization.SearchedData;

import org.anon.utilities.reflect.ClassTraversal;
import org.anon.utilities.exception.CtxException;

public class AlterationStage implements Distillation
{
    public AlterationStage()
    {
    }

    public Distillate distill(Distillate prev)
        throws CtxException
    {
        SearchedData data = (SearchedData)prev.current();
        List<SearchedData.PrimeFlow> flows = data.getPrimes();
        Class evtClazz = data.eventClass();
        List<Object> events = new ArrayList<Object>();
        for (SearchedData.PrimeFlow f : flows)
        {
            CreateEventVisitor visitor = new CreateEventVisitor(data, f);
            ClassTraversal traverse = new ClassTraversal(evtClazz, visitor);
            Object ret = traverse.traverse();
            events.add(ret);
        }

        AlteredData isotope = new AlteredData(data, events);
        return new Distillate(prev, isotope);
    }

    public Distillate condense(Distillate prev)
        throws CtxException
    {
        //TODO:
        return null;
    }

    public boolean distillFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof SearchedData);
    }

    public boolean condenseFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof SearchedData);
    }
}

