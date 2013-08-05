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
 * File:                org.anon.smart.smcore.anatomy.SMCoreModule
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A module for core module
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.anatomy;

import java.util.Map;
import java.util.HashMap;

import org.anon.smart.base.flow.FlowService;
import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.base.tenant.TenantsHosted;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.channels.shell.SCShell;
import org.anon.smart.channels.shell.ExternalConfig;
import org.anon.smart.smcore.stt.STTService;
import org.anon.smart.smcore.transition.TransitionService;
import org.anon.smart.deployment.MacroDeployer;

import static org.anon.smart.base.utils.AnnotationUtils.*;

import org.anon.utilities.anatomy.AModule;
import org.anon.utilities.anatomy.ModuleContext;
import org.anon.utilities.anatomy.StartConfig;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class SMCoreModule extends AModule implements FlowConstants
{
    public SMCoreModule(AModule parent)
        throws CtxException
    {
        super(parent, new SMCoreContext(), true);
    }

    protected void setup()
        throws CtxException
    {
        FlowService.initialize();
        STTService.initialize();
        TransitionService.initialize();
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new SMCoreModule(_parent);
    }

    public void start(StartConfig cfg)
        throws CtxException
    {
        if (!(cfg instanceof SMCoreConfig))
            return;

        SMCoreContext smctx = (SMCoreContext)_context;
        SCShell shell = smctx.smartChannelShell();
        SMCoreConfig ccfg = (SMCoreConfig)cfg;
        ExternalConfig[] channels = ccfg.startChannels();
        for (int i = 0; i < channels.length; i++)
            shell.addChannel(channels[i]);

        shell.startAllChannels();

        if (ccfg.firstJVM())
        {
            TenantsHosted.initialize();
            Map<String, String[]> features = MacroDeployer.deployFile(FLOW, "AdminSmartFlow.soa", null);
            Map<String, String[]> featuresForAllFlows = MacroDeployer.deployFile(FLOW, "AllFlows.soa", null);
            features.putAll(featuresForAllFlows);
            
            SmartTenant powner = TenantsHosted.platformOwner();
            //enable before committing, else the space will not be present
            for (String flow : features.keySet())
                powner.deploymentShell().enableForMe(flow, features.get(flow), new HashMap<String, String>());
            TenantAdmin tadmin = new TenantAdmin(powner.getName(), powner);
            powner.setAdmin(tadmin);
            Object admin = powner.getAdmin();
            String flow = flowFor(admin.getClass());
            RuntimeShell rshell = (RuntimeShell)powner.runtimeShell();
            rshell.commitToSpace(flow, new DSpaceObject[] { (DSpaceObject)admin });
        }
    }

    public void stop()
        throws CtxException
    {
        SMCoreContext smctx = (SMCoreContext)_context;
        SCShell shell = smctx.smartChannelShell();
        shell.stopAllChannels();

        TenantsHosted.cleanup();
    }
}

