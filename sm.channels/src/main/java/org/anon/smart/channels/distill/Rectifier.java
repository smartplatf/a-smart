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
 * File:                org.anon.smart.channels.distill.Rectifier
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A processing of rectifying data over multiple steps
 *
 * ************************************************************
 * */

package org.anon.smart.channels.distill;

import java.util.List;
import java.util.ArrayList;

import org.anon.utilities.exception.CtxException;

public class Rectifier
{
    private List<Distillation> _steps;
    private boolean _completed;

    public Rectifier()
    {
        _steps = new ArrayList<Distillation>();
        _completed = false;
    }

    public void addStep(Distillation distill)
    {
        _steps.add(distill);
    }

    public Distillate distill(Distillate start)
        throws CtxException
    {
        return distill(start, _steps.size());
    }

    public Distillate distill(Distillate start, int till)
        throws CtxException
    {
        Distillate cont = start;
        for (int i = 0; i < till; i++)
        {
            Distillation dist = _steps.get(i);
            if (dist.distillFrom(cont))
            {
                cont = dist.distill(cont);
            }
        }

        _completed = cont.isDone();
        return cont;
    }

    public Distillate condense(Distillate start)
        throws CtxException
    {
        return condense(start, _steps.size());
    }

    public Distillate condense(Distillate start, int till)
        throws CtxException
    {
        Distillate cont = start;
        for (int i = (till - 1); i >= 0; i--)
        {
            Distillation dist = _steps.get(i);
            if (dist.condenseFrom(cont))
            {
                cont = dist.condense(cont);
            }
        }

        _completed = cont.isDone();
        return cont;
    }

    public boolean hasCompleted() { return _completed; }
}

