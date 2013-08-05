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
 * File:                org.anon.smart.smcore.channel.distill.sanitization.SanitizationStage
 * Author:              rsankar
 * Revision:            1.0
 * Date:                19-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A stage where data is sanitized
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.sanitization;

import java.util.Map;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.channels.distill.Distillation;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.smcore.channel.distill.translation.MapData;

import org.anon.utilities.exception.CtxException;

public class SanitizationStage implements Distillation
{
    private Rectifier _myRectifier;

    public SanitizationStage()
    {
    }

    public void setRectifier(Rectifier rectifier)
    {
        _myRectifier = rectifier;
    }

    protected SearchedData createData(Isotope curr)
    {
        return new SearchedData(curr);
    }

    protected SanitizeData createSanitizer()
    {
        return new SanitizeData();
    }

    public Distillate distill(Distillate prev)
        throws CtxException
    {
        Isotope curr = prev.current();
        PData pdata = (PData)prev.from().current();
        SearchedData data = createData(curr);
        SanitizeData sanitize = createSanitizer();
        sanitize.sanitizePData(pdata, data);
        if (curr instanceof MapData)
        {
            MapData m = (MapData)curr;
            sanitize.sanitizeMap(m, data);
        }

        return new Distillate(prev, data);
    }

    public Distillate condense(Distillate prev)
        throws CtxException
    {
        //for now this does not take care of reducing the size of
        //hosted objects and they are sent out as is.
        SearchedData sdata = (SearchedData)prev.current();
        MapData d = new MapData(sdata, sdata.searchedMap());

        return new Distillate(prev, d);
    }

    public boolean distillFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof MapData);
    }

    public boolean condenseFrom(Distillate prev)
        throws CtxException
    {
        return (prev.current() instanceof SearchedData);
    }
}

