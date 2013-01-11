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
 * File:                org.anon.smart.channels.distill.RectifierUnit
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An execution unit to rectify
 *
 * ************************************************************
 * */

package org.anon.smart.channels.distill;

import org.anon.utilities.concurrency.ExecutionUnit;
import org.anon.utilities.exception.CtxException;

public class RectifierUnit implements ExecutionUnit
{
    private Rectifier _rectifier;
    private boolean _distill;
    private Distillate _distillate;

    public RectifierUnit(Rectifier rectifier, Distillate start, boolean distill)
    {
        _rectifier = rectifier;
        _distillate = start;
        _distill = distill;
    }

    public void execute()
        throws CtxException
    {
        if (_distill)
            _distillate = _rectifier.distill(_distillate);
        else
            _distillate = _rectifier.condense(_distillate);
    }

    public Distillate finalDistillate() { return _distillate; }
}

