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
 * File:                org.anon.smart.channels.distill.Distillate
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An intermediatary product in the process of rectification
 *
 * ************************************************************
 * */

package org.anon.smart.channels.distill;

public class Distillate
{
    private Distillate _from;
    private Isotope _current;
    private boolean _done;

    public Distillate(Isotope start)
    {
        _current = start;
        _done = false;
    }

    public Distillate(Distillate from, Isotope to)
    {
        _from = from;
        _current = to;
        _current.setIsotopeOf(from.current());
        _done = false;
    }

    public Distillate from() { return _from; }
    public Isotope current() { return _current; }

    public boolean isDone() { return _done; }
    public void setDone() { _done = true; }
}

