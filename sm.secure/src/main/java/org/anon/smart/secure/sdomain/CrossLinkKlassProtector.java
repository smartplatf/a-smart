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
 * File:                org.anon.smart.secure.sdomain.CrossLinkKlassProtector
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A cross link for protection
 *
 * ************************************************************
 * */

package org.anon.smart.secure.sdomain;

import java.security.CodeSource;
import java.security.CodeSigner;
import java.security.ProtectionDomain;
import java.security.PermissionCollection;
import java.security.Permissions;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkKlassProtector extends CrossLinker
{
    public CrossLinkKlassProtector(ClassLoader loader, String name, CodeSource source, PermissionCollection permission)
        throws CtxException
    {
        super(loader);
        create(name, source, permission);
    }

    public CrossLinkKlassProtector(Object obj)
    {
        super(obj);
    }

    protected Class[] parmTypes(String method, Object ... params)
    {
        if (method.equals("<init>"))
        {
            return new Class[] { String.class, CodeSource.class, PermissionCollection.class };
        }

        return super.parmTypes(method, params);
    }

    public void protectKlass(Class cls)
        throws CtxException
    {
        linkMethod("protectKlass", cls);
    }
}

