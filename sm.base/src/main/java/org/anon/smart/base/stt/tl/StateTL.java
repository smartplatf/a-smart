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
 * File:                org.anon.smart.base.stt.tl.StateTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A template for specifying states
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.tl;

import java.util.List;
import java.lang.annotation.Annotation;

import org.anon.smart.base.annot.StateAnnotate;

public class StateTL extends BaseTL
{
    private String name;
    private boolean endState;
    private boolean startState;
    private String parentState;
    private List<ChildStateTL> childStates;
    private int timeout = -1;

    public StateTL()
    {
    }

    //public boolean isStartState() { return ((startState != null) && (startState.equals("yes"))); }
    //public boolean isEndState() { return ((endState != null) && (endState.equals("yes"))); }
    public String getParentState() { return parentState; }
    public int getTimeOut() { return timeout; }
    public List<ChildStateTL> getChildren() { return childStates; }

    @Override
    public Class[] getAnnotations(String name)
    {
        return new Class[] { StateAnnotate.class };
    }

    static StateTL getDefaultStartState()
    {
        StateTL active = new StateTL();
        active.name = "active";
        active.startState = true;
        active.endState = false;
        return active;
    }

    static StateTL getDefaultEndState()
    {
        StateTL inactive = new StateTL();
        inactive.name = "inactive";
        inactive.startState = false;
        inactive.endState = true;
        return inactive;
    }
}

