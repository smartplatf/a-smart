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
 * File:                org.anon.smart.secure.inbuilt.data.iden.Identity
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An identity for a user
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data.iden;

public class Identity implements java.io.Serializable
{
    private String _smartUser;
    private String _identity;
    private String _randomCode;
    private SCredential _credential;

    public Identity(String user, String iden, SCredential cred)
    {
        _smartUser = user;
        _identity = iden;
        _credential = cred;
        _randomCode = "Nothing";
    }

    public String getSmartUser() { return _smartUser; }
    public String getIdentity() { return _identity; }
    public SCredential getCredential() { return _credential; }
    public void setRandomCode(String cde)
    {
        System.out.println("The current code is: " + _randomCode + ":" + cde);
        _randomCode = cde;
    }

    public void resetCode()
    {
        _randomCode = "Nothing";
    }

    public String getRandomCode()
    {
        return _randomCode;
    }

    public String toString()
    {
        return _smartUser + ":" + _identity + ":" + _randomCode + ":" + _credential;
    }
}

