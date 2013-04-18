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
 * File:                org.anon.smart.channels.AbstractServerChannel
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An abstract server channel
 *
 * ************************************************************
 * */

package org.anon.smart.channels;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.anon.smart.channels.data.RData;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.SCConfig;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public abstract class AbstractServerChannel extends AbstractChannel implements SmartServerChannel
{
    protected ExecutorService _responder; //the responses are sent via this to the client back

    public AbstractServerChannel(SCShell shell, SCConfig cfg)
        throws CtxException
    {
        super(shell, cfg);
        _responder = Executors.newCachedThreadPool(); //note this cannot be a distributed pool.
        initialize(shell, cfg);
    }

    public void sendResponses(RData data)
        throws CtxException
    {
        assertion().assertNotNull(data, "Cannot send responses for null data.");
        Runnable run = new ResponseSender(data);
        _responder.execute(run);
    }

    protected abstract void initialize(SCShell shell, SCConfig cfg)
        throws CtxException;

    protected void shutdown()
        throws CtxException
    {
        try
        {
            _responder.shutdown();
            if (!_responder.awaitTermination(120, TimeUnit.SECONDS))
                _responder.shutdownNow();
            if (!_responder.awaitTermination(60, TimeUnit.SECONDS))
                except().te("Error shutting down the responder.");
        }
        catch (Exception e)
        {
            _responder.shutdownNow();
            except().rt(e, new CtxException.Context("Error in shutdown.", "Exception"));
        }
    }
}

