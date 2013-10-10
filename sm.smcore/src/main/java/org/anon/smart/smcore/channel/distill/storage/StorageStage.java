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
 * File:                org.anon.smart.smcore.channel.distill.storage.StorageStage
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A stage where data is pushed into the queue for processing
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.storage;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.channels.distill.Distillation;
import org.anon.smart.channels.distill.Rectifier;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.smcore.flow.CrossLinkSmartFlow;
import org.anon.smart.smcore.channel.server.EventRData;
import org.anon.smart.smcore.channel.distill.alteration.AlteredData;
import org.anon.smart.smcore.channel.distill.sanitization.SearchedData;
import org.anon.smart.smcore.channel.internal.MessagePData;

import org.anon.utilities.exception.CtxException;

public class StorageStage implements Distillation
{
    private Rectifier _myRectifier;

    public StorageStage()
    {
    }

    public void setRectifier(Rectifier parent)
    {
        _myRectifier = parent;
    }

    protected void cleanup(CrossLinkSmartTenant tenant)
        throws CtxException
    {
    }

    public Distillate distill(Distillate prev)
        throws CtxException
    {
        AlteredData data = (AlteredData)prev.current();
        PData pdata = null;
        Distillate start = prev.from();
        SearchedData searched = null;
        while ((start != null) && (start.from() != null))
        {
            if (start.current() instanceof SearchedData)
                searched = (SearchedData)start.current();
            start = start.from();
        }
        pdata = (PData)start.current();
        List<AlteredData.FlowEvent> evts = data.events();
        for (AlteredData.FlowEvent evt : evts)
        {
            Object event = evt.event();
            EventRData rdata = new EventRData(_myRectifier, pdata, event, searched.tenant(), searched.flowDeployment());
            System.out.println("Flow is: " + evt.flow() + ":" + evt.getClass().getClassLoader() + ":" + evt.flow().getClass().getClassLoader());
            CrossLinkSmartFlow clsf = new CrossLinkSmartFlow(evt.flow());
            if(pdata instanceof MessagePData)
            	clsf.postInternal(rdata);
            else
            	clsf.postExternal(rdata);
        }

        cleanup(searched.tenant());//here does nothing.

        //shd not be used after this stage
        return prev;
    }

    public Distillate condense(Distillate prev)
        throws CtxException
    {
        EventRData data = (EventRData)prev.current(); //assumes that we get a rdata
        Object objTosend = data.event();
        List<AlteredData.FlowEvent> evts = new ArrayList<AlteredData.FlowEvent>();
        evts.add(new AlteredData.FlowEvent(null, objTosend));
        AlteredData adata = new AlteredData(data, evts);
        return new Distillate(prev, adata);
    }

    public boolean distillFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof AlteredData);
    }

    public boolean condenseFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof EventRData);
    }
}

