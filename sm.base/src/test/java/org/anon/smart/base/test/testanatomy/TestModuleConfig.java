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
 * File:                org.anon.smart.base.test.testanatomy.TestModuleConfig
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for the test module
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.testanatomy;

import org.anon.utilities.anatomy.StartConfig;
import org.anon.utilities.exception.CtxException;

public interface TestModuleConfig extends StartConfig
{
    public boolean initTenants();
    public String[] jarFiles();
    public String[] deploymentFiles();
    public String[] tenantsToCreate();
    public String[] tenantsToEnableFor(String deployment);
    public Object configFor(String tenant, ClassLoader ldr)
        throws CtxException;
}

