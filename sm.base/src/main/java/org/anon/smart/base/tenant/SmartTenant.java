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
 * File:                org.anon.smart.base.tenant.SmartTenant
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a tenant for the platform
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import org.anon.smart.base.loader.LoaderVars;
import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.tenant.shell.CrossLinkDataShell;
import org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.loader.RelatedObject;
import org.anon.utilities.exception.CtxException;

public class SmartTenant implements RelatedObject
{
    private String _name;
    private SmartLoader _loader;
    private CrossLinkDataShell _dataShell;
    private CrossLinkRuntimeShell _runtimeShell;

    public SmartTenant(String name)
        throws CtxException
    {
        _name = name;
        if (this.getClass().getClassLoader() instanceof SmartLoader)
        {
            SmartLoader ldr = (SmartLoader)this.getClass().getClassLoader();
            LoaderVars vars = new LoaderVars(name);
            _loader = (SmartLoader)ldr.repeatMe(vars);
            ldr.setRelatedTo(this);
        }
        else
        {
            except().te("Cannot create this object using any other classloader other than smart classloader");
        }
    }

    public String getName() { return _name; }
    public ClassLoader getRelatedLoader() { return _loader; }
}

