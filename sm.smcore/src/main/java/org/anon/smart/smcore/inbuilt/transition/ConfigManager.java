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
 * File:                org.anon.smart.smcore.inbuilt.transition.ConfigManager
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A manager to create configuration
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.smcore.data.ConfigData;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.config.ConfigService;
import org.anon.smart.smcore.inbuilt.events.ConfigFlow;
import org.anon.smart.smcore.inbuilt.responses.SuccessCreated;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class ConfigManager
{
    public ConfigManager()
    {
    }

    private Object[] getConfigKeys(ConfigFlow evt, String flow, CrossLinkDeploymentShell dshell)
        throws CtxException
    {
        List<Object> keys = new ArrayList<Object>();
        if (evt.getConfigObject() != null)
            keys.add(evt.getConfigObject());

        if ((evt.getConfigFor() != null) && (evt.getConfigFor().length() > 0))
        {
            Class cls = dshell.dataClass(flow, evt.getConfigFor());
            assertion().assertNotNull(cls, "Cannot find a deployment for: " + evt.getConfigFor() + ":" + flow);
            keys.add(cls);
        }

        if ((evt.getStaticKey() != null) && (evt.getStaticKey().length() > 0))
            keys.add(evt.getStaticKey());

        if (keys.size() <= 0)
            keys.add(flow);

        return keys.toArray(new Object[0]);
    }

    public void createConfiguration(ConfigFlow evt)
        throws CtxException
    {
        Object o = evt;
        SmartEvent sevt = (SmartEvent)o;
        String flow = sevt.smart___flowname();
        assertion().assertNotNull(flow, "Cannot find the flow for event");

        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        assertion().assertNotNull(tenant, "Cannot find the tenant for which to create the config.");
        CrossLinkDeploymentShell dshell = tenant.deploymentShell();
        assertion().assertNotNull(dshell, "Cannot find the deploymentshell for the tenant: " + tenant.getName());
        Class<? extends ConfigData> confcls = (Class<? extends ConfigData>)dshell.configClass(flow, evt.getName());
        if (confcls == null)
            confcls = (Class<? extends ConfigData>)dshell.configClass("AllFlows", evt.getName()); //check in allflows
        assertion().assertNotNull(confcls, "Cannot find config for: " + evt.getName() + ":" + flow);
        Object[] keys = getConfigKeys(evt, flow, dshell);
        for (int i = 0; (keys != null) && (i < keys.length); i++)
        {
            ConfigData[] cfgs = ConfigService.saveConfig(confcls, keys[i], evt.getConfigValues());
            for (int j = 0; (cfgs != null) && (j < cfgs.length); j++)
            {
                CrossLinkAny clany = new CrossLinkAny(cfgs[j]);
                clany.invoke("configstt___init");
            }
        }

        SuccessCreated resp = new SuccessCreated(evt.getName()); 
    }
}

