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
 * File:                org.anon.smart.channels.distill.RectifierInstinct
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An instinct in which rectification occurs
 *
 * ************************************************************
 * */

package org.anon.smart.channels.distill;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.channels.Route;
import org.anon.smart.channels.MessageReader;
import org.anon.smart.channels.shell.DataInstincts;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.concurrency.ExecutionUnit;
import org.anon.utilities.exception.CtxException;

public abstract class RectifierInstinct implements DataInstincts
{
    private Rectifier _rectifier;

    public RectifierInstinct(Rectifier rectifier)
    {
        _rectifier = rectifier;
    }

    protected abstract Distillate createStart(Route chnl, Object msg, MessageReader rdr)
        throws CtxException;

    public void whenConnect(Route chnl)
        throws CtxException
    {
        //I have nothing here
    }

    public void whenMessage(Route chnl, Object msg, MessageReader rdr)
        throws CtxException
    {
        Distillate start = createStart(chnl, msg, rdr);
        RectifierUnit unit = new RectifierUnit(_rectifier, start, true);
        List<ExecutionUnit> exec = new ArrayList<ExecutionUnit>();
        exec.add(unit);
        execute().synch(exec);
    }
}

