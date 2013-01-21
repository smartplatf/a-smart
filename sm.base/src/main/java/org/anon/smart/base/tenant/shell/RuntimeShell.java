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
 * File:                org.anon.smart.base.tenant.shell.RuntimeShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A shell within which tenant flows execute
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import java.util.List;

import org.anon.utilities.exception.CtxException;

public class RuntimeShell implements SmartShell
{
    private transient ShellContext _context;

    public RuntimeShell()
        throws CtxException
    {
        initializeShell();
    }

    public void initializeShell()
        throws CtxException
    {
        _context = new ShellContext();
    }

    public void cleanup()
        throws CtxException
    {
    }

    public Object lookupFor(String spacemodel, String group, Object key)
        throws CtxException
    {
        //no need to search different spaces to find out which space has it
        //it is searched directly on the shell that has to have it. IF not 
        //present, then it is not accessible or not present.
        DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
        Object ret = shell.lookup(spacemodel, group, key);
        return ret;
    }

    public List<Object> searchFor(String spacemodel, String group, Object query)
        throws CtxException
    {
        DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
        return shell.search(spacemodel, group, query);
    }
}

