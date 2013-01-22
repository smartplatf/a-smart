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

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.base.flow.FlowModel;
import org.anon.smart.base.loader.LoaderVars;
import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.tenant.shell.CrossLinkDataShell;
import org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell;
import org.anon.smart.base.tenant.shell.DeploymentShell;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.loader.RelatedObject;
import org.anon.utilities.exception.CtxException;

public class SmartTenant implements RelatedObject, TenantConstants, DSpaceObject, java.io.Serializable
{
    private static final Map<String, String> GROUP_MAPPING = new HashMap<String, String>();

    static
    {
        GROUP_MAPPING.put(USERS_SPACE, STANDARD_GROUP);
        GROUP_MAPPING.put(SESSIONS_SPACE, STANDARD_GROUP);
        GROUP_MAPPING.put(ROLES_SPACE, STANDARD_GROUP);
        GROUP_MAPPING.put(CONFIG_SPACE, STANDARD_GROUP);
        GROUP_MAPPING.put(MONITOR_SPACE, STANDARD_GROUP);
        GROUP_MAPPING.put(WORKING_SPACE, WORKING_GROUP);
    }

    private String _name;
    private boolean _platformOwner;

    private transient SmartLoader _loader;
    private transient Map<String, CrossLinkDataShell> _data;
    private transient CrossLinkRuntimeShell _runtimeShell;
    private transient DeploymentShell _deploymentShell;

    public SmartTenant(String name)
        throws CtxException
    {
        _name = name;
        if ((name.equals(PLATFORMOWNER)) && (this.getClass().getClassLoader() instanceof SmartLoader))
        {
            SmartLoader ldr = (SmartLoader)this.getClass().getClassLoader();
            ldr.setRelatedTo(this);
            _loader = ldr;
            _platformOwner = true;
        }
        else if (this.getClass().getClassLoader() instanceof SmartLoader)
        {
            SmartLoader ldr = (SmartLoader)this.getClass().getClassLoader();
            LoaderVars vars = new LoaderVars(name);
            _loader = (SmartLoader)ldr.repeatMe(vars);
            ldr.setRelatedTo(this);
            _platformOwner = false;
        }
        else
        {
            except().te("Cannot create this object using any other classloader other than smart classloader");
        }

        createShells();
    }

    private void createShells()
        throws CtxException
    {
        _data = new HashMap<String, CrossLinkDataShell>();
        int start = 0;
        CrossLinkDataShell std = new CrossLinkDataShell(start, _loader);
        start = std.addStandardSpaces();
        _data.put(STANDARD_GROUP, std);
        CrossLinkDataShell work = new CrossLinkDataShell(start, _loader);
        start = work.addWorkingSpaces();
        _data.put(WORKING_GROUP, work);
        CrossLinkDataShell flow = new CrossLinkDataShell(start, _loader);
        _data.put(FLOW_GROUP, flow);
        _runtimeShell = new CrossLinkRuntimeShell(_loader);
        _deploymentShell = new DeploymentShell(_loader);
    }

    public String getName() { return _name; }
    public ClassLoader getRelatedLoader() { return _loader; }
    public boolean isPlatformOwner() { return _platformOwner; }

    public Object dataShellFor(String spacemodel)
    {
        Object ret = null;
        CrossLinkDataShell shell = null;
        if (GROUP_MAPPING.containsKey(spacemodel))
            shell = _data.get(GROUP_MAPPING.get(spacemodel));
        else
            shell = _data.get(FLOW_GROUP);

        if (shell != null)
            ret = shell.link();
        
        return ret;
    }

    public Object runtimeShell()
    {
        return _runtimeShell.link();
    }

    public DeploymentShell deploymentShell()
    {
        return _deploymentShell;
    }

    public void cleanup()
        throws CtxException
    {
        _runtimeShell.cleanup();
        for (CrossLinkDataShell shell : _data.values())
            shell.cleanup();
        _deploymentShell.cleanup();
    }

    public void enableFlow(FlowModel model)
        throws CtxException
    {
        //for now. If anything more need to add here
        Object amodel = serial().cloneIn(model, _loader);
        CrossLinkDataShell shell = _data.get(FLOW_GROUP);
        shell.addSpace(amodel);
    }

    public List<Object> smart___keys()
        throws CtxException
    {
        List<Object> keys = new ArrayList<Object>();
        keys.add(_name);
        return keys;
    }

    public String smart___objectGroup()
        throws CtxException
    {
        return TENANTGROUP;
    }
}

