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
 * File:                org.anon.smart.secure.flow.SecureFlowDeployment
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A flow deployment with security related config
 *
 * ************************************************************
 * */

package org.anon.smart.secure.flow;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.deployment.Artefact;
import org.anon.smart.deployment.Deployment;

import org.anon.utilities.exception.CtxException;

public class SecureFlowDeployment extends FlowDeployment
{
    private List<SecureConfig> security;

    public SecureFlowDeployment()
    {
        super();
        security = new ArrayList<SecureConfig>();
    }

    public SecureFlowDeployment(String nm, Artefact[] a)
    {
        super(nm, a);
        security = new ArrayList<SecureConfig>();
    }

    public SecureFlowDeployment(SecureFlowDeployment dep, String[] features)
        throws CtxException
    {
        super(dep, features);
        security = new ArrayList<SecureConfig>();
        copy(dep.security, security);
    }

    @Override
    public <T extends Deployment> T createDefault(String[] features, Class<T> cls)
        throws CtxException
    {
        return cls.cast(new SecureFlowDeployment(this, features));
    }

    public List<SecureConfig> getSecurity() { return security; }

}

