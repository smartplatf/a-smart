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
 * File:                org.anon.smart.base.tenant.shell.ShellContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                16-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context for the shell
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;

import static org.anon.utilities.loader.RelatedUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class ShellContext
{
    private CrossLinkSmartTenant _tenant;
    private String _name;

    public ShellContext()
        throws CtxException
    {
        Object related = getRelatedObject(this);
        assertion().assertNotNull(related, "The shell object context cannot be null.");
        _tenant = new CrossLinkSmartTenant(related);
        _name = _tenant.getName();
    }

    public String name() { return _name; }

    public CrossLinkSmartTenant tenant() { return _tenant; }
}

