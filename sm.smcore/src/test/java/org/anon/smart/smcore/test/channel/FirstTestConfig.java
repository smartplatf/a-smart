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
 * File:                org.anon.smart.smcore.test.channel.FirstTestConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A config to setup for first test
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test.channel;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.test.testanatomy.TestTenantConfig;

import org.anon.utilities.exception.CtxException;

import org.anon.sampleapp.review.ReviewObject;

public class FirstTestConfig implements TestTenantConfig
{
    public FirstTestConfig()
    {
    }

    public DSpaceObject[] objectsFor(String deploy)
        throws CtxException
    {
        if (deploy.equals("ReviewFlow"))
        {
            Object rv = new ReviewObject("Object1");
            List<DSpaceObject> lst = new ArrayList<DSpaceObject>();
            lst.add((DSpaceObject)rv);
            return lst.toArray(new DSpaceObject[0]);
        }

        return null;
    }
}

