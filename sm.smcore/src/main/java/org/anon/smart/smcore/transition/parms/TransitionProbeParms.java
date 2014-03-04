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
 * File:                org.anon.smart.smcore.transition.parms.TransitionProbeParms
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of transition probe parameters
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.parms;

import java.util.List;

import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.transition.TransitionContext;

import org.anon.utilities.gconcurrent.execute.DefaultProbeParms;

public class TransitionProbeParms extends DefaultProbeParms
{
    private SmartEvent _event;
    private SmartPrimeData _prime;

    public TransitionProbeParms(TransitionContext ctx, List<Object> prms)
    {
        super(ctx, prms);
        _event = ctx.event();
        //_prime = ctx.primeData();
        _prime = (SmartPrimeData)ctx.primeED().empirical();
    }

    public SmartEvent event() { return _event; }
    public SmartPrimeData primeData() { return _prime; }
}

