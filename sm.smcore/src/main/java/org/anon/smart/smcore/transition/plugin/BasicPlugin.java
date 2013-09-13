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
 * File:                org.anon.smart.smcore.transition.plugin.BasicPlugin
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A basic plugin that does nothing during the hooks provided
 * Use this so that any new hooks added will not affect existing hooks
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.plugin;

import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.events.SmartEvent;

import org.anon.utilities.exception.CtxException;

public class BasicPlugin implements TransitionPlugin
{
    public BasicPlugin()
    {
    }

    public void eventProcessed(SmartEvent evt)
        throws CtxException
    {
    }

    public void objectCreated(SmartData obj)
        throws CtxException
    {
    }

    public void objectModified(SmartData obj)
        throws CtxException
    {
    }

    public void primeObjectCreated(SmartPrimeData obj)
        throws CtxException
    {
    }

    public void stateTransitioned(SmartData obj, String from, String to)
        throws CtxException
    {
    }
}

