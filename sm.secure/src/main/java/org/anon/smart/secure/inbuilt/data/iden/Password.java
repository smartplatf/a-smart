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
 * File:                org.anon.smart.secure.inbuilt.data.iden.Password
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A password credential
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.data.iden;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.secure.inbuilt.data.auth.SAuthenticator;
import org.anon.smart.secure.inbuilt.data.auth.CustomAuthenticator;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class Password implements SCredential, java.io.Serializable
{
    private transient byte[] _passwordString;
    private List<Byte> _passwordBytes;

    Password()
    {
    }

    public Password(String pwd)
        throws CtxException
    {
        _passwordString = crypt().encrypt(pwd);
        _passwordBytes = new ArrayList<Byte>();
        for (int i = 0; i < _passwordString.length; i++)
            _passwordBytes.add(_passwordString[i]);
    }

    private void setup()
    {
        if ((_passwordString == null) && (_passwordBytes != null))
        {
            _passwordString = new byte[_passwordBytes.size()];
            for (int i = 0; i < _passwordBytes.size(); i++)
            {
                _passwordString[i] = _passwordBytes.get(i);
            }
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean bret = false;
        if (obj instanceof Password)
        {
            Password s2 = (Password)obj;
            s2.setup();
            this.setup();
            if ((s2._passwordString != null) && (_passwordString != null))
            {
                bret = (s2._passwordString.length == _passwordString.length);
                for (int i = 0; bret && (i < _passwordString.length); i++)
                    bret = bret && (_passwordString[i] == s2._passwordString[i]);
            }
        }

        return bret;
    }

    @Override
    public int hashCode()
    {
        this.setup();
        String str = new String(_passwordString);
        return str.hashCode();
    }

    public SAuthenticator authenticator()
    {
        return new CustomAuthenticator();
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        Repeatable repeat = null;
        if (vars instanceof SCredParms)
        {
            SCredParms p = (SCredParms)vars;
            repeat = new Password(p.getKey());
        }

        return repeat;
    }
}

