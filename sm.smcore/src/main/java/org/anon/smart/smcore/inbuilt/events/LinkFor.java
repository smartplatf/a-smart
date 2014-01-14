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
 * File:                org.anon.smart.smcore.inbuilt.events.LinkFor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A link specified
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.events;

public class LinkFor implements java.io.Serializable
{
    private String name;
    private String flow;
    private String object;
    private String attribute;

    public LinkFor()
    {
    }

    LinkFor(String f, String a)
    {
        String[] s = f.split("\\.");
        String add = "";
        flow = "";
        for (int i = 0; i < (s.length - 1); i++)
        {
            flow += add + s[i];
            add = ".";
        }
        name = s[s.length - 1];
        s = a.split("\\.");
        add = "";
        object = "";
        for (int i = 0; i < (s.length - 1); i++)
        {
            object += add + s[i];
            add = ".";
        }
        attribute = s[s.length - 1];
    }

    public String getFlow() { return flow; }
    public String getObject() { return object; }
    public String getAttribute() { return attribute; }

    public String getName() { return name; }
    public String getTo() 
    { 
        String ret = "";
        String add = "";
        if ((flow != null) && (flow.length() > 0))
        {
            ret = flow;
            add = ".";
        }

        if ((object != null) && (object.length() > 0))
        {
            ret += add + object;
            add = ".";
        }

        if ((attribute != null) && (attribute.length() > 0))
            ret += add + attribute;

        return ret;
    }
}

