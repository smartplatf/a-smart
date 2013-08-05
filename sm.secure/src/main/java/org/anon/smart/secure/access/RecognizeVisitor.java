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
 * File:                org.anon.smart.secure.access.RecognizeVisitor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A recognizer for the visitor
 *
 * ************************************************************
 * */

package org.anon.smart.secure.access;

import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.secure.session.SessionDirector;
import org.anon.smart.secure.dspace.SecureDataFilter;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class RecognizeVisitor
{
    private RecognizeVisitor()
    {
    }

    public static Visitor recognizeCurrentVisitor()
        throws CtxException
    {
        threads().addToContextLocals(SecureDataFilter.SETTING_VISITOR, SecureDataFilter.YES);
        Session sess = SessionDirector.currentSession();
        Visitor ret = null;
        if (sess != null)
        {
            ret = new KnownVisitor(sess);
        }
        else
        {
            ret = new UnknownVisitor();
        }
        threads().addToContextLocals(SecureDataFilter.SETTING_VISITOR, SecureDataFilter.NO);

        return ret;
    }
}

