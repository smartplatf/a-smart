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
 * File:                org.anon.smart.base.stt.BaseSTTContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A base context for all stereotypes
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

public abstract class BaseSTTContext implements STTContext
{
    private String _name;
    private String _signature;
    private int _access;

    protected ClazzDescriptor _descriptor;

    protected BaseSTTContext(ClazzDescriptor descriptor, String name, String signature, int access)
    {
        _name = name;
        _signature = signature;
        _access = access;
        _descriptor = descriptor;
    }

    public String name() { return _name; }
    public String signature() { return _signature; }
    public int access() { return _access; }
    public ClazzDescriptor descriptor() { return _descriptor; }
}
