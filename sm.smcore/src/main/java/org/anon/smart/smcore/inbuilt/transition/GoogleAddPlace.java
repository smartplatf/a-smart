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
 * File:                org.anon.smart.smcore.inbuilt.transition.GoogleAddPlace
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-11-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An add place object to be posted
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import java.util.List;
import java.util.ArrayList;

public class GoogleAddPlace implements java.io.Serializable
{
    class Location implements java.io.Serializable
    {
        double lat;
        double lng;
    }

    private Location location;
    private int accuracy;
    private String name;
    private List<String> types;

    public GoogleAddPlace(double l1, double l2, String nm, int acc, String type)
    {
        location = new Location();
        location.lat = l1;
        location.lng = l2;
        accuracy = acc;
        name = nm;
        types = new ArrayList<String>();
        types.add(type);
    }
}

