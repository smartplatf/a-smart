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
 * File:                org.anon.smart.smcore.transition.graph.TransitionNodeDetails
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of details for transitions
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.graph;

import java.lang.reflect.Method;

import org.anon.smart.smcore.annot.MethodAnnotate;
import org.anon.smart.smcore.annot.ServiceAnnotate;

import org.anon.utilities.gconcurrent.DefaultNodeDetails;
import org.anon.utilities.exception.CtxException;

public class TransitionNodeDetails extends DefaultNodeDetails
{
    private String _name;
    private String _from;
    private String _to;
    private String _after;
    private String _before;

    public TransitionNodeDetails(Class cls, Method mthd, MethodAnnotate annot)
        throws CtxException
    {
        super(cls, mthd, annot.parms());
        _from = annot.from();
        _to = annot.to();
        _after = annot.runAfter();
        _before = annot.runBefore();
        _name = annot.name();
        System.out.println("Annotate is: " + annot + ":" + _after);
    }

    public TransitionNodeDetails(Class cls, Method mthd, ServiceAnnotate annot, String parms)
        throws CtxException
    {
        super(cls, mthd, parms);
        _from = annot.from();
        _to = annot.to();
        _after = annot.runAfter();
        _before = annot.runBefore();
        _name = annot.name();
        System.out.println("Annotate is: " + annot + ":" + _after);
    }

    public String name() { return _name; }
    public String from() { return _from; }
    public String to() { return _to; }
    public String after() { return _after; }
    public String before() { return _before; }
}

