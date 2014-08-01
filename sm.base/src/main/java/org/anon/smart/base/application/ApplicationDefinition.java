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
 * File:                org.anon.smart.base.application.ApplicationDefinition
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A definition of an application in SMART
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class ApplicationDefinition implements java.io.Serializable, VerifiableObject
{
    private String name;
    private String type;
    private List<FlowDefinition> flows;
    private List<RoleDefinition> roles;
    private List<PackageDefinition> packages;
    private List<BTxnDefinition> transactions;

    public ApplicationDefinition()
    {
    }

    public void setupVariables(Map<String, String> vars)
    {
        Map<String, String> str = new HashMap<String, String>();
        for (String k : vars.keySet())
        {
            str.put("\\$" + k, vars.get(k));
        }

        for (int i = 0; (flows != null) && (i < flows.size()); i++)
        {
            FlowDefinition fdef = flows.get(i);
            fdef.setupVariables(str);
        }
    }

    public boolean verify()
        throws CtxException
    {
        assertion().assertNotNull(name, "Need an application Name.");
        assertion().assertNotNull(flows, "Need atleast one flow to be deployed as a part of this application.");
        assertion().assertNotNull(packages, "Need atleast one package to be defined as a part of this application.");
        assertion().assertNotNull(transactions, "Need atleast one transaciotn to be defined as a part of this application.");

        for (FlowDefinition def : flows)
            def.verify();

        if (roles != null)
        {
            for (RoleDefinition rdef : roles)
                rdef.verify();
        }

        for (PackageDefinition pdef : packages)
            pdef.verify();

        for (BTxnDefinition bdef : transactions)
            bdef.verify();

        return true;
    }

    public String getName() { return name; }
    public List<FlowDefinition> getFlows() { return flows; }
    public List<RoleDefinition> getRoles() { return roles; }
    public List<PackageDefinition> getPackages() { return packages; }
    public List<BTxnDefinition> getTransactions() { return transactions; }

    public boolean isVerified()
    {
        return true;
    }
}

