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
 * File:                org.anon.smart.smcore.stt.EventResponseSTT
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A response stereotyping
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt;

import java.util.UUID;

import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.smcore.events.SmartEventResponse;
import org.anon.smart.smcore.transition.TransitionContext;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class EventResponseSTT implements SmartEventResponse
{
    private UUID ___smart_responseid___;

    public EventResponseSTT()
    {
    }

    @MethodExit("constructor")
    private void smart___initresponse()
        throws CtxException
    {
        ___smart_responseid___ = UUID.randomUUID();
        //include into txn
        if (threads().threadContext() instanceof TransitionContext)
        {
            //can hve errored even before this.
            TransitionContext ctx = (TransitionContext)threads().threadContext();
            if (ctx != null)
                ctx.atomicity().includeResponse(this);
        }
    }

    public UUID smart___eventID() { return ___smart_responseid___; }

}

