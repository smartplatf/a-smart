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
 * File:                org.anon.smart.atomicity.Atomicity
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An atomic operation provided for all included hypothesis
 *
 * ************************************************************
 * */

package org.anon.smart.atomicity;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.utilities.exception.CtxException;

public class Atomicity
{
    private UUID _atomicID;
    private Map<String, Hypothesis> _hypothesis;

    public Atomicity()
    {
        _atomicID = UUID.randomUUID();
        _hypothesis = new ConcurrentHashMap<String, Hypothesis>();
    }

    public EmpiricalData includeEmpiricalData(EmpiricalData edata)
        throws CtxException
    {
        EmpiricalData eret = edata;
        String dt = edata.dataType();
        Hypothesis hypo = _hypothesis.get(dt);    
        if (hypo == null)
            hypo = new DeductiveHypothesis(_atomicID, edata);
        else
            eret = hypo.collect(edata);

        _hypothesis.put(dt, hypo);
        return eret;
    }

    public List<EmpiricalData> dataFor(String dt, String tag)
        throws CtxException
    {
        Hypothesis hypo = _hypothesis.get(dt);
        return hypo.empiricalDataFor(tag);
    }

    public List<EmpiricalData> searchDataFor(String dt, String[] regex)
        throws CtxException
    {
        Hypothesis hypo = _hypothesis.get(dt);
        return hypo.searchEmpiricalData(regex);
    }

    
}

