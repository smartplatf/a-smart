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
 * File:                org.anon.smart.base.application.FlowDefinition
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of definitions for flows
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.util.Map;
import java.util.List;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class FlowDefinition implements java.io.Serializable, VerifiableObject
{
    private String name;
    private String soa;
    private String file;
    private List<String> libraries;

    public FlowDefinition()
    {
    }

    public void setupVariables(Map<String, String> vars)
    {
        for (String k : vars.keySet())
        {
            file = file.replaceAll(k, vars.get(k));
            for (int i = 0; (libraries != null) && (i < libraries.size()); i++)
            {
                String l = libraries.get(i);
                String s = l.replaceAll(k, vars.get(k));
                libraries.set(i, s);
            }
        }
    }

    public boolean verify()
        throws CtxException
    {
        assertion().assertNotNull(name, "Need an flow Name.");
        assertion().assertNotNull(soa, "Need the soa file for the flow " + name);
        assertion().assertNotNull(file, "Need the file for the flow " + name);
        return true;
    }

    public String getName() { return name; }
    public String getSoa() { return soa; }
    public String getFile() { return file; }
    public List<String> getLibraries() { return libraries; }

    public boolean isVerified() { return true; }
}

