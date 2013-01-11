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
 * File:                org.anon.smart.base.test.loader.SmartDataStereoType
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test stereotype to be able to add to test
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.loader;

import java.util.UUID;

import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.base.stt.annot.MethodEnter;
import org.anon.smart.base.stt.annot.IncludeSTT;

@IncludeSTT(types={"TagObject"})
public class SmartDataStereoType
{
    private UUID __smart__id__;

    public SmartDataStereoType()
    {
    }

    @MethodExit("constructor")
    public void initializeMe()
    {
        __smart__id__ = UUID.randomUUID();
        System.out.println("Assigned a uuid as: " + __smart__id__);
    }

    public UUID getId() { return __smart__id__; }
    
    @MethodEnter("getTest")
    public void callAllMethods(String clsname, String mthdName)
    {
        System.out.println("Call in all methods: " + clsname + ":" + mthdName);
    }

    @MethodExit("")
    public void callAllMethodsExit(String clsname, String mthdName)
    {
        System.out.println("Call in all method exit: " + clsname + ":" + mthdName);
    }
}

