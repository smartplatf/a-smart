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
import java.util.List;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.data.RData;
import org.anon.smart.channels.data.ContentData;
import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.distill.Distillation;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.smcore.channel.server.EventPData;
import org.anon.smart.smcore.channel.distill.alteration.AlteredData;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.objservices.ConvertService.*;

import org.anon.utilities.exception.CtxException;

public class TranslationStage implements Distillation
{
    private translator _type;

    public TranslationStage(translator t)
    {
        _type = t;
    }

    public Distillate distill(Distillate prev)
        throws CtxException
    {
        PData data = (PData)prev.current();
        InputStream str = data.cdata().data();
        Object convert = convert().readObject(str, Map.class, _type);
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
            convert().writeObject(resp, ostr, _type);
        }
        byte[] bytes = ostr.toByteArray();
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
}

