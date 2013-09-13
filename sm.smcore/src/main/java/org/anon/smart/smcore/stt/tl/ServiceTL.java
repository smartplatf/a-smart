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
 * File:                org.anon.smart.smcore.stt.tl.ServiceTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                20-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A template for calling service
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt.tl;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.smcore.annot.ServiceAnnotate;

public class ServiceTL extends BaseTL
{
    private String name;
    private String service;
    private String foreach;
    private String parms;
    private String event;
    private String prime;
    private String from;
    private String to;
    private String runAfter;
    private String runBefore;

    public ServiceTL()
    {
    }

    @Override
    public Class[] getAnnotations(String mName)
    {
        List<Class> annons = new ArrayList<Class>();
        annons.add(ServiceAnnotate.class);
        return annons.toArray(new Class[0]);
    }
    
}

