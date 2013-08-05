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
 * File:                org.anon.smart.secure.inbuilt.data.auth.EmptyDetails
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of details that has no information in it
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data.auth;

public class EmptyDetails implements AuthDetails
{
    private boolean _verified;
    private String _smartUser;
    private String _identity;

    public EmptyDetails(boolean verified, String usr, String identity)
    {
        _verified = verified;
        _smartUser = usr;
        _identity = identity;
    }

    public boolean isVerified() { return _verified; }
    public String smartUser() { return _smartUser; }
    public String identity() { return _identity; }
}

