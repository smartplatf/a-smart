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
 * File:                org.anon.smart.base.application.BTxnDefinition
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of definitions for business transactions
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.util.List;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class BTxnDefinition implements java.io.Serializable, VerifiableObject
{
    private String name;
    private List<String> primeData;
    private List<String> events;

    public BTxnDefinition()
    {
    }

    public String getName() { return name; }
    public List<String> getPrimeData() { return primeData; }
    public List<String> getEvents() { return events; }

    public boolean verify()
        throws CtxException
    {
        assertion().assertNotNull(name, "Please provide a name for the transaction.");
        assertion().assertNotNull(primeData, "Please provide atleast one prime data.");
        assertion().assertTrue(primeData.size() > 0, "Please provide atleast one prime data.");
        //assertion().assertNotNull(events, "Please provide atleast one event");
        return true;
    }

    public boolean isVerified() { return true; }
}

