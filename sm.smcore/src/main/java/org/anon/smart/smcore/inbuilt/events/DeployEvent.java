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
 * File:                org.anon.smart.smcore.inbuilt.events.DeployEvent
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * an event to deploy a jar file
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.events;

public class DeployEvent implements java.io.Serializable
{
    private String deployJar; //assumes this is present on the server
    private String flowsoa; //for now, change all this when a jar deployer is present

    public DeployEvent()
    {
    }

    protected DeployEvent(String j, String f)
    {
        deployJar = j;
        flowsoa = f;
    }

    public String getJar() { return deployJar; }
    public String getFlowSoa() { return flowsoa; }
}

