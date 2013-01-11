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
 * File:                org.anon.smart.channels.data.PData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                11-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A primary data received from any channel
 *
 * ************************************************************
 * */

package org.anon.smart.channels.data;

import org.anon.smart.channels.distill.Isotope;

public class PData extends Isotope
{
    private DScope _scope;
    private long _receivedTime;
    private CData _data;

    public PData(DScope scope, CData data)
    {
        super();
        _scope = scope;
        _data = data;
        _receivedTime = System.nanoTime();
    }

    public CData cdata() { return _data; }
    public DScope dscope() { return _scope; }
    public long receivedTime() { return _receivedTime; }
}

