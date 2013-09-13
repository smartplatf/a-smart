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
 * File:                org.anon.smart.monitor.util.MonitorUtils
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of utilites
 *
 * ************************************************************
 * */

package org.anon.smart.monitor.util;

import org.anon.smart.monitor.annot.MonitorAnnotate;
import org.anon.smart.monitor.stt.Constants;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class MonitorUtils implements Constants
{
    public static String[] getMonitors(Class cls)
        throws CtxException
    {
        MonitorAnnotate annot = (MonitorAnnotate)reflect().getAnnotation(cls, MonitorAnnotate.class);
        String monitors = DEFAULTMONITORS;
        if ((annot != null) && (annot.monitors().length() > 0))
            monitors = annot.monitors();
       
        String[] mons = value().listAsString(monitors);
        return mons;
    }
}

