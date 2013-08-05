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
 * File:                org.anon.smart.smcore.test.TestDistillation
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A testing for refinery
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test;

import org.anon.smart.channels.distill.Distillation;
import org.anon.smart.channels.distill.Rectifier;
import org.anon.smart.channels.distill.Distillate;
import org.anon.smart.smcore.test.channel.TestPData;

import org.anon.utilities.exception.CtxException;

public class ResponseCollector implements Distillation
{
    private Rectifier _myRectifier;
    private String _response;
    private Object _waitForResponse;
    private boolean _wait;

    public ResponseCollector(boolean waitresp)
    {
        _wait = waitresp;
        if (_wait)
            _waitForResponse = new Object();
    }

    public void setRectifier(Rectifier rectifier)
    {
        _myRectifier = rectifier;
    }

    public Distillate distill(Distillate prev)
        throws CtxException
    {
        TestPData data = (TestPData)prev.current();
        _response = data.getPosted();
        System.out.println(prev.current());
        if (_waitForResponse != null)
        {
            _wait = false;
            synchronized(_waitForResponse)
            {
                _waitForResponse.notifyAll();
            }
        }
        return prev;
    }

    public Distillate condense(Distillate prev)
        throws CtxException
    {
        System.out.println(prev.current());
        return prev;
    }

    public boolean distillFrom(Distillate prev)
        throws CtxException
    {
        return true;
    }

    public boolean condenseFrom(Distillate prev)
        throws CtxException
    {
        return true;
    }

    public String getResponse()
    {
        return _response;
    }

    public void waitForResponse()
        throws Exception
    {
        if (_wait)
        {
            synchronized(_waitForResponse)
            {
                if (_wait)
                    _waitForResponse.wait();
            }
        }
    }
}

