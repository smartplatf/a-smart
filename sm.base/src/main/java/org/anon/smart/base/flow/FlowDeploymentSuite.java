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
 * File:                org.anon.smart.base.flow.FlowDeploymentSuite
 * Author:              rsankar
 * Revision:            1.0
 * Date:                14-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A flow service for flows
 *
 * ************************************************************
 * */

package org.anon.smart.base.flow;

import org.anon.smart.deployment.DeploymentSuite;
import org.anon.smart.deployment.ArtefactType;

import org.anon.utilities.exception.CtxException;

public class FlowDeploymentSuite extends DeploymentSuite<FlowDeployment>
{
    private static FlowDeploymentSuite APP_INSTANCE = null;

    protected FlowDeploymentSuite()
        throws CtxException
    {
        super();
        setHandleDeployment(FlowDeployment.class);
    }

    private static void setSingleInstance(Object obj)
    {
        if (APP_INSTANCE == null)
            APP_INSTANCE = (FlowDeploymentSuite)obj;
    }

    private static Object getSingleInstance()
    {
        return APP_INSTANCE;
    }

}

