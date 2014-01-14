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
 * File:                org.anon.smart.base.flow.CrossLinkLink
 * Author:              rsankar
 * Revision:            1.0
 * Date:                18-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A cross link for the link object
 *
 * ************************************************************
 * */

package org.anon.smart.base.flow;


import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkLink extends CrossLinker
{
    public class CrossLinkLinkObject extends CrossLinker
    {
        public CrossLinkLinkObject(Object obj)
        {
            super(obj);
        }

        public String getFlow() 
            throws CtxException
        { 
            return (String)linkMethod("getFlow"); 
        }

        public String getObject() 
            throws CtxException
        { 
            return (String)linkMethod("getObject"); 
        }

        public String getField() 
            throws CtxException
        { 
            return (String)linkMethod("getField"); 
        }

        public String getCardinality() 
            throws CtxException
        { 
            return (String)linkMethod("getCardinality"); 
        }
    }

    public CrossLinkLink(Object obj)
    {
        super(obj);
    }

    public CrossLinkLinkObject getFromObject()
        throws CtxException
    {
        Object obj = linkMethod("getFromObject");
        return new CrossLinkLinkObject(obj);
    }

    public CrossLinkLinkObject getToObject()
        throws CtxException
    {
        Object obj = linkMethod("getToObject");
        return new CrossLinkLinkObject(obj);
    }

    public CrossLinkLinkObject getViaFlowObject()
        throws CtxException
    {
        Object obj = linkMethod("getViaFlowObject");
        if (obj != null)
            return new CrossLinkLinkObject(obj);

        return null;
    }

    public String getVia()
        throws CtxException
    {
        return (String)linkMethod("getVia");
    }

    public String getViaFlow()
        throws CtxException
    {
        return (String)linkMethod("getViaFlow");
    }

    public boolean isInternal()
        throws CtxException
    {
        Boolean b = (Boolean)linkMethod("isInternal");
        return b.booleanValue();
    }

    public CrossLinkLinkObject getViaObject()
        throws CtxException
    {
        Object obj = linkMethod("getViaObject");
        if (obj != null)
            return new CrossLinkLinkObject(obj);

        return null;
    }
}

