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
 * File:                org.anon.smart.base.tenant.TenantSpaceCreator
 * Author:              rsankar
 * Revision:            1.0
 * Date:                16-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A creator for tenant space which also created default tenant
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import org.anon.utilities.utils.DefaultVMSCreator;
import org.anon.utilities.exception.CtxException;

public class TenantSpaceCreator extends DefaultVMSCreator implements TenantConstants
{
    public TenantSpaceCreator()
    {
    }

    public static Object createTenantsHosted(String cls)
        throws CtxException
    {
        TenantsHosted thosted = (TenantsHosted)DefaultVMSCreator.createVMS(cls);
        //TODO: this needs to be the transaction to create the tenant, not direct as this
        SmartTenant smo = new SmartTenant(PLATFORMOWNER);
        return thosted;
    }
}

