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
 * File:                org.anon.smart.secure.anatomy.SecureModule
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A module for security module
 *
 * ************************************************************
 * */

package org.anon.smart.secure.anatomy;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantsHosted;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.deployment.MacroDeployer;
import org.anon.smart.secure.stt.SecureSTTService;
import org.anon.smart.secure.sdomain.SmartSecurityManager;
import org.anon.smart.secure.inbuilt.transition.TransitionService;


import org.anon.utilities.anatomy.AModule;
import org.anon.utilities.anatomy.ModuleContext;
import org.anon.utilities.anatomy.StartConfig;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class SecureModule extends AModule implements FlowConstants
{
    public SecureModule(AModule parent)
        throws CtxException
    {
        super(parent, new SecureContext(), false);
    }

    protected void setup()
        throws CtxException
    {
        SecureSTTService.initialize();
        TransitionService.initialize();
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new SecureModule(_parent);
    }

    public void start(StartConfig cfg)
        throws CtxException
    {
        System.setSecurityManager(new SmartSecurityManager());
        if (!(cfg instanceof SecureConfig))
            return;

        SecureContext smctx = (SecureContext)_context;
        SecureConfig ccfg = (SecureConfig)cfg;

        if (ccfg.firstJVM())
        {
            //the smcore should have initialized before this, hence this shd just deploy and use??
            Map<String, String[]> features = MacroDeployer.deployFile(FLOW, "Security.soa", null);
            SmartTenant powner = TenantsHosted.platformOwner();
            //enable before committing, else the space will not be present
            for (String flow : features.keySet())
                powner.deploymentShell().enableForMe(flow, features.get(flow), new HashMap<String, String>());

            TransitionService.setupDefaultRolesAndUsers(powner);
            System.out.println("STARTED SECURE MODULE");
        }
    }

    public void stop()
        throws CtxException
    {
    }
}

