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
 * File:                org.anon.smart.base.tenant.TenantsHosted
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A dspace where tenants are saved
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import java.util.Set;

import static org.anon.smart.base.dspace.DSpaceService.*;

import org.anon.smart.base.dspace.DSpace;
import org.anon.smart.base.dspace.TransactDSpace;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.d2cache.BrowsableReader;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.utils.ApplicationSingleton;
import org.anon.utilities.exception.CtxException;

public class TenantsHosted extends ApplicationSingleton implements TenantConstants
{
    private static TenantsHosted TENANTS_HOSTED = null;

    private TransactDSpace _tenantSpace;

    public TenantsHosted()
        throws CtxException
    {
        super();
        _tenantSpace = (TransactDSpace)spaceFor(TENANTSPACENAME, true);
    }

    private static void setSingleInstance(Object obj)
    {
        if (TENANTS_HOSTED == null)
            TENANTS_HOSTED = (TenantsHosted)obj;
    }

    private static Object getSingleInstance()
    {
        return TENANTS_HOSTED;
    }

    public Object getTenant(String name)
        throws CtxException
    {
        return getTenant(name, false);
    }

    public Object getTenant(String name, boolean memOnly)
        throws CtxException
    {
        return lookupIn(_tenantSpace, name, TENANTGROUP, memOnly);
    }
    
    protected static Object tenantsSpace()
        throws CtxException
    {
        return getAppInstance(TenantsHosted.class.getName(), "org.anon.smart.base.tenant.TenantSpaceCreator", "createTenantsHosted", 
                new Class[] { String.class }, new Object[] { TenantsHosted.class.getName() });
    }

    //Please note, this function is exposed purely for testing purposes. DO NOT USE IT ANYWHERE ELSE
    public static SmartTenant addTenant(String tenant)
        throws CtxException
    {
        TenantsHosted ts = (TenantsHosted)tenantsSpace();
        SmartTenant t = new SmartTenant(tenant);
        addToSpace(ts._tenantSpace, new DSpaceObject[] { t });
        return t;
    }

    public static void commitTenant(SmartTenant tenant)
        throws CtxException
    {
        TenantsHosted ts = (TenantsHosted)tenantsSpace();
        addToSpace(ts._tenantSpace, new DSpaceObject[] { tenant });
    }

    public static void initialize()
        throws CtxException
    {
        addTenant(PLATFORMOWNER);
    }

    public static SmartTenant platformOwner()
        throws CtxException
    {
        return tenantFor(PLATFORMOWNER);
    }

    public static SmartTenant tenantFor(String name)
            throws CtxException
    {
            TenantsHosted ts = (TenantsHosted)tenantsSpace();
            return (SmartTenant)ts.getTenant(name);
    }
    
    public static SmartTenant tenantFor(String name, boolean memOnly)
        throws CtxException
    {
        TenantsHosted ts = (TenantsHosted)tenantsSpace();
        return (SmartTenant)ts.getTenant(name, memOnly);
    }

    public static CrossLinkSmartTenant crosslinkedTenantFor(String name)
        throws CtxException
    {
        CrossLinkTenantsHosted th = new CrossLinkTenantsHosted(tenantsSpace());
        return new CrossLinkSmartTenant(th.getTenant(name));
    }

    public static CrossLinkSmartTenant crosslinkedPlatformOwner()
        throws CtxException
    {
        return crosslinkedTenantFor(PLATFORMOWNER);
    }

    public void cleanMe()
        throws CtxException
    {
        /* TODO: gives an exception for browsablereader?? */
        BrowsableReader rdr = browsableReaderFor(_tenantSpace);
        Set tenants = rdr.currentKeySet(TENANTGROUP);
        for (Object tenant : tenants)
        {
            SmartTenant t = tenantFor(tenant.toString());
            if (t != null)
                t.cleanup();
        }
        
        SmartTenant ts = tenantFor(PLATFORMOWNER);
        if (ts != null)
            ts.cleanup();
        _tenantSpace.cleanup();
    }

    public static void cleanup()
        throws CtxException
    {
        CrossLinkTenantsHosted cra = new CrossLinkTenantsHosted(tenantsSpace());
        cra.cleanMe();
    }
}

