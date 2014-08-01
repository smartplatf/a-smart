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
 * File:                org.anon.smart.base.tenant.TenantAdmin
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An administrator for a tenant, used as prime object in all events to tenant
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.anon.smart.base.dspace.RelatedObject;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell;

import org.anon.utilities.exception.CtxException;

public class TenantAdmin implements RelatedObject, java.io.Serializable
{
    private String _tenantName;
    private transient SmartTenant _newTenant;
    private transient Map<String, List<Object>> _defaultTenantObjects;

    public TenantAdmin(String name, SmartTenant tenant)
    {
        _tenantName = name;
        _newTenant = tenant;
        _defaultTenantObjects = new HashMap<String, List<Object>>();
    }

    public void addTenantObjects(String model, List<Object> objs)
    {
        //need to add validations for things like only smart objects,
        //only smart models etc.
        if (!_defaultTenantObjects.containsKey(model))
        {
            _defaultTenantObjects.put(model, objs);
        }
        else
        {
            List<Object> exist = _defaultTenantObjects.get(model);
            exist.addAll(objs);
            _defaultTenantObjects.put(model, exist);
        }
    }

    public String tenantName()
    {
        return _tenantName;
    }

    public void setupTenant(SmartTenant tenant)
    {
        _newTenant = tenant; //in a scenario where I am enabling, I am not in the same context of the tenant
    }

    public DSpaceObject[] relatedTo()
        throws CtxException
    {
        //if (_newTenant != null)
         //   return new DSpaceObject[] { _newTenant };

        return null;
    }

    public boolean isPlatformOwner()
        throws CtxException
    {
        SmartTenant tenant = _newTenant;
        if (_newTenant == null)
            tenant = (SmartTenant)CrossLinkSmartTenant.currentTenant().link();

        return tenant.isPlatformOwner();
    }

    public void commitTenant()
        throws CtxException
    {
    	//TenantsHosted.commitTenant(_newTenant);
    	SmartTenant tenant = _newTenant;
        if (_newTenant == null)
            tenant = (SmartTenant)CrossLinkSmartTenant.currentTenant().link();
        TenantsHosted.commitTenant(tenant);
        if(_defaultTenantObjects != null)
        {
            for (String model : _defaultTenantObjects.keySet())
            {
                List<Object> commitObjs = (List<Object>)_defaultTenantObjects.get(model);
                Object[] objs = commitObjs.toArray();
                if ((objs.length > 0) && (_newTenant != null))
                {
                    CrossLinkRuntimeShell shell = _newTenant.rshell();
                    shell.commitInternalObjects(model, objs);
                }
            }
            //can remove it as soon as committed, since it is available in cache henceforth
            cleanup();
        }
    }

    public void cleanup()
    {
        if (_defaultTenantObjects != null)
            _defaultTenantObjects.clear();

        _defaultTenantObjects = null;
        _newTenant = null;
    }
}

