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
 * File:                org.anon.smart.smcore.channel.internal.MessageErrorHandler
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An error handler for events received
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.internal;

import org.anon.smart.smcore.inbuilt.responses.ErrorResponse;
import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.distill.AbstractErrorHandler;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class MessageErrorHandler extends AbstractErrorHandler
{
    private Throwable exception;

    public MessageErrorHandler()
    {
        super();
    }

    @Override
    protected Isotope[] createResponses(Rectifier rectifier, Throwable t, Distillate start)
    {
        /*
         * try
        {
            ErrorResponse response = new ErrorResponse(ErrorResponse.servererrors.exception, t);
            EventPData pdata = (EventPData)start.current();
            EventRData rdata = new EventRData(rectifier, pdata, response, null, null);
            return new Isotope[] { rdata };
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
        exception = t;
        return null;
    }

    public void throwException()
        throws CtxException
    {
        if (exception != null)
            except().rt(exception, new CtxException.Context("Exception", "Exception"));
    }
}

