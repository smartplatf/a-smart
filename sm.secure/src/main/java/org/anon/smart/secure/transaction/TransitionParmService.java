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
 * File:                org.anon.smart.secure.transaction.TransitionParmService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An initialization of services in secure
 *
 * ************************************************************
 * */

package org.anon.smart.secure.transaction;

import org.anon.smart.smcore.transition.TConstants;
import org.anon.smart.secure.transition.parms.SessionProbe;

import org.anon.utilities.gconcurrent.execute.ParamType;
import org.anon.utilities.exception.CtxException;

public class TransitionParmService implements TConstants
{
    public TransitionParmService()
    {
    }

    public static void initialize()
        throws CtxException
    {
        ParamType.registerProbe(SESSION, new SessionProbe(), true);
    }
}

