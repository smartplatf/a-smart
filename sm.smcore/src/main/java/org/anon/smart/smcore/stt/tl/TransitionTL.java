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
 * File:                org.anon.smart.smcore.stt.tl.TransitionTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A template to read transition configuration
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt.tl;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.base.annot.TransitionAnnotate;
import org.anon.smart.smcore.annot.ServicesAnnotate;

import org.anon.utilities.exception.CtxException;

public class TransitionTL extends BaseTL
{
    private String prime;
    private String event;
    private String foreach;
    private List<MethodTL> actions;
    private List<MethodTL> services;
    private List<ServiceTL> callservices;

    public TransitionTL()
    {
    }

    @Override
    public Class[] getAnnotations(String name)
        throws CtxException
    {
        List<Class> annons = new ArrayList<Class>();
        Class[] annots = super.getAnnotations(name);
        for (int i = 0; (annots != null) && (i < annots.length); i++)
            annons.add(annots[i]);

        annons.add(TransitionAnnotate.class);
        annons.add(ServicesAnnotate.class);
        return annons.toArray(new Class[0]);
    }

    public static TransitionTL defaultFor(String clsname, String type, String flow, String[] parms)
    {
        return null; //no defaults, has to be provided.
    }

    @Override
    public boolean shouldAdd(String type)
    {
        return true;
    }

}

