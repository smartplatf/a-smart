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
 * File:                org.anon.smart.smcore.inbuilt.config.GoogleAppConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-11-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for google services access
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.config;

public class GoogleAppConfig implements java.io.Serializable
{
    private final String appkey;
    private final String placeType;
    private final int accuracy;

    public GoogleAppConfig()
    {
        appkey = "";
        placeType = "";
        accuracy = 50;
    }

    public String getAppKey() { return appkey; }
    public String getPlaceType() { return placeType; }
    public int getAccuracy() { return accuracy; }
}

