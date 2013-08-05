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
 * File:                org.anon.smart.secure.inbuilt.data.iden.IdentityType
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A type of identity which recognizes how the authentication is done
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data.iden;

import org.anon.utilities.exception.CtxException;

public enum IdentityType
{
    custom(new Password());

    private SCredential _creden;

    private IdentityType(SCredential cred)
    {
        _creden = cred;
    }

    public SCredential credentialFor(String key)
        throws CtxException
    {
        SCredParms parms = new SCredParms(key);
        return (SCredential)_creden.repeatMe(parms);
    }

    public static SCredential getCredential(String type, String key)
        throws CtxException
    {
        IdentityType t = IdentityType.valueOf(type);
        if (t != null)
        {
            return t.credentialFor(key);
        }

        return null;
    }
}

