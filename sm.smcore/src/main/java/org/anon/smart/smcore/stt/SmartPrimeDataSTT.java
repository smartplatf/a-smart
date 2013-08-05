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
 * File:                org.anon.smart.smcore.stt.SmartPrimeDataSTT
 * Author:              rsankar
 * Revision:            1.0
 * Date:                19-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A stereotype for prime smart data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt;

import static org.anon.smart.base.utils.AnnotationUtils.*;

import org.anon.smart.smcore.flow.SmartFlow;
import org.anon.smart.smcore.data.SmartPrimeData;

import org.anon.smart.base.stt.annot.IncludeSTT;
import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.base.flow.FlowModel;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.utils.InternalData;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;

import org.anon.utilities.exception.CtxException;

@IncludeSTT(types={"Data"})
public class SmartPrimeDataSTT extends SmartDataSTT implements SmartPrimeData
{
    private SmartFlow ___smart_flow___;

    public SmartPrimeDataSTT()
    {
    }

    @MethodExit("constructor")
    private void smart___initPrime()
        throws CtxException
    {
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkDeploymentShell shell = tenant.deploymentShell();
        String name = objectName(this);
        Object check = this;
        CrossLinkFlowDeployment dep = null;
        if (check instanceof InternalData)
        {
            String flow = ((InternalData)check).myFlow();
            dep = shell.deploymentFor(flow);
        }
        else
        {
            String flow = flowFor(this.getClass());
            dep = shell.flowForPrimeType(flow, name);
        }
        FlowModel model = (FlowModel)dep.model(tenant.getRelatedLoader());
        ___smart_flow___ = new SmartFlow(model);
    }

    public SmartFlow smart___flow()
    {
        return ___smart_flow___;
    }

	public void initPrimeObject()
		throws CtxException
	{
		smart___initPrime();
		
	}
}

