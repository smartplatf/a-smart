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
 * File:                org.anon.smart.base.application.PackageDefinition
 * Author:              rsankar
 * Revision:            1.0
 * Date:                09-03-2014
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of definitions for packages
 *
 * ************************************************************
 * */

package org.anon.smart.base.application;

import java.util.List;
import java.util.ArrayList;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class PackageDefinition implements java.io.Serializable, VerifiableObject
{
    public class LinkDefinition implements java.io.Serializable
    {
        private String name;
        private String flow;
        private String object;
        private String attribute;

        public String getLinkName() { return name; }
        public String getFlow() { return flow; }
        public String getObject() { return object; }
        public String getAttribute() { return attribute; }

        public List<String> getArray() 
        { 
            List<String> arr = new ArrayList<String>();
            String val = "";
            String add = "";
            if (flow != null)
            {
                val = val + flow;
                add = ".";
            }
            if (object != null)
            {
                val = val + add + object;
                add = ".";
            }
            if (attribute != null)
            {
                val = val + add + attribute;
                add = ".";
            }
            arr.add(val);
            return arr;
        }

        public boolean verify()
            throws CtxException
        {
            assertion().assertNotNull(name, "Please provide a name for the link.");
            return true;
        }
    }

    public class EnabledDefinition implements java.io.Serializable
    {
        private String enableFlow;
        private String tenant;
        private List<String> enableFeatures;
        private List<String> addFeatures;
        private List<LinkDefinition> links;

        public String getEnableFlow() { return enableFlow; }
        public String getTenant() { return tenant; }
        public List<String> getEnabledFeatures() { return enableFeatures; }
        public List<String> getAddFeatures() { return addFeatures; }
        public List<LinkDefinition> getLinks() { return links; }

        public boolean verify()
            throws CtxException
        {
            assertion().assertNotNull(enableFlow, "Please provide a flow to enable.");
            assertion().assertNotNull(enableFeatures, "Please provide atleast one feature to enable");
            assertion().assertTrue(enableFeatures.size() > 0, "Please provide atleast one feature to enable");
            if (links != null)
            {
                for (LinkDefinition l : links)
                    l.verify();
            }

            return true;
        }
    }

    private String name;
    private String extend;
    private List<String> description;
    private List<EnabledDefinition> enabled;
    private List<String> roles;

    public PackageDefinition()
    {
    }

    public String getName() { return name; }
    public String getExtend() { return extend; }
    public List<EnabledDefinition> getEnableFlows() { return enabled; }
    public List<String> getRoles() { return roles; }

    public boolean verify()
        throws CtxException
    {
        assertion().assertNotNull(name, "Please provide a name for the package.");
        assertion().assertNotNull(description, "Please provide atleast one description for the package");
        assertion().assertNotNull(enabled, "Please provide atleast one flow to enable.");
        assertion().assertTrue(enabled.size() > 0, "Please provide atleast one flow to enable");
        return true;
    }

    public boolean isVerified() { return true; }
}

