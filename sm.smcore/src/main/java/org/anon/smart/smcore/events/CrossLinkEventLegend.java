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
 * File:                org.anon.smart.smcore.events.CrossLinkEventLegend
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A crosslinker for event legend
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.events;

import java.util.UUID;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkEventLegend extends CrossLinker
{
    public CrossLinkEventLegend(UUID sess, String origin, ClassLoader ldr)
        throws CtxException
    {
        super(ldr);
        create(sess, origin);
    }

    protected Class[] parmTypes(String mthd, Object ... params)
    {
        if (mthd.equals("<init>"))
        {
            return new Class[] { UUID.class, String.class };
        }
        else if (mthd.equals("stampReceived") && (params.length > 0))
        {
            return new Class[] { Long.TYPE };
        }

        return super.parmTypes(mthd, params);
    }

    public void stampReceived(long recvd)
        throws CtxException
    {
        linkMethod("stampReceived", recvd);
    }
}

