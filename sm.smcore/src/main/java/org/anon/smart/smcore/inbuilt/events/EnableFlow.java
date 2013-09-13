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
 * File:                org.anon.smart.smcore.inbuilt.events.EnableFlow
 * Author:              rsankar
 * Revision:            1.0
 * Date:                14-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An event to enable flow for a tenant
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.events;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class EnableFlow implements java.io.Serializable
{
    private String tenant;
    private String enableFlow;
    private List<String> enableFeatures;
    private List<LinkFor> links;

    public EnableFlow()
    {
    }

    protected EnableFlow(String t, String ef, List<String> f, Map<String, String> lnks)
    {
        tenant = t;
        enableFlow = ef;
        enableFeatures = f;
        if (lnks != null)
        {
            links = new ArrayList<LinkFor>();
            for (String k : lnks.keySet())
            {
                LinkFor l = new LinkFor(k, lnks.get(k));
                links.add(l);
            }
        }
    }

    public String getEnableFlow()
    {
        return enableFlow;
    }

    public List<String> getEnableFeatures()
    {
        return enableFeatures;
    }

    public String getTenant()
    {
        return tenant;
    }

    public List<LinkFor> getLinks() { return links; }
}

