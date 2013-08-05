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
 * File:                org.anon.smart.secure.sdomain.DomainAccessRequest
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A request for the given domain
 *
 * ************************************************************
 * */

package org.anon.smart.secure.sdomain;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.Visitor;
import org.anon.smart.secure.access.Accessed;
import org.anon.smart.secure.access.AccessedImpl;
import org.anon.smart.secure.access.AccessRequest;
import org.anon.smart.secure.access.RecognizeVisitor;

import org.anon.utilities.exception.CtxException;

public class DomainAccessRequest implements AccessRequest
{
    private Accessed _accessed;
    private Visitor _visitor;
    private Access _requested;

    public DomainAccessRequest(Object accessed, Access request, Object ... parms)
        throws CtxException
    {
        _requested = request;
        _accessed = new AccessedImpl(accessed, parms);
        _visitor = RecognizeVisitor.recognizeCurrentVisitor();
    }

    public Visitor getVisitor()
    {
        return _visitor;
    }

    public Accessed getAccessed()
    {
        return _accessed;
    }

    public Access getRequestedAccess()
    {
        return _requested;
    }
}

