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
 * File:                org.anon.smart.base.stt.tl.DefaultTemplate
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of default configuration values for each type of object
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.tl;

import org.anon.smart.deployment.Deployment;
import org.anon.smart.deployment.DeployRuntime;
import org.anon.smart.base.flow.FlowDeployment;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class DefaultTemplate implements SearchTemplate
{
    public DefaultTemplate()
    {
    }

    public boolean templateFound(String cls, ClassLoader ldr)
    {
        if (cls.indexOf("DeployRuntime") >= 0)
            return false;

        try
        {
            CrossLinkAny cl = new CrossLinkAny(DeployRuntime.class.getName(), ldr);
            Object dep = cl.invoke("getDeploying");
            if (cls.indexOf("testapp") >= 0)
                System.out.println("The deployment got is: " + dep + ":" + cls + ":" + ldr);
            //I know maybe we shd just do it as an instanceof FlowDeployment? But it may not work because of classloaders
            boolean ret = ((dep != null) && ((dep.getClass().getName().equals(FlowDeployment.class.getName())) ||
                                            (dep.getClass().getName().equals("org.anon.smart.secure.flow.SecureFlowDeployment"))));
            //do not create a template for any of the smart or utility classes.
            //smart classes that need soa, needs to have an explicit soa declared
            ret = ret && (!cls.startsWith("org.anon.smart"));
            ret = ret && (!cls.startsWith("org.anon.utilities"));
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public BaseTL[] searchFor(String clsname, ClassLoader ldr, Object[] noparms)
        throws CtxException
    {
        System.out.println("Creating a default soa for: " + clsname);
        BaseTL ret = null;
        CrossLinkAny cldrt = new CrossLinkAny(DeployRuntime.class.getName(), ldr);
        Object odep = cldrt.invoke("getDeploying");
        CrossLinkAny cl = new CrossLinkAny(odep);
        String dtype = (String)cl.invoke("getDataType", clsname);
        //assertion().assertNotNull(dtype, "Cannot find the type for: " + clsname);
        if (dtype != null)
        {
            String name = (String)cl.invoke("deployedName");
            String[] parms = (String[])cl.invoke("getParmsFor", dtype, clsname);
            Class<? extends BaseTL> cls = TemplateReader.getTemplateMapping(dtype);
            System.out.println("searchFor for: " + clsname + ":" + dtype + ":" + name + ":" + parms.length + ":" + cls);
            if (cls != null)
            {
                CrossLinkAny clbtl = new CrossLinkAny(cls.getName());
                ret = (BaseTL)clbtl.invoke("defaultFor", new Class[] { String.class, String.class, String.class, String[].class }, 
                        new Object[] { clsname, dtype, name, parms });
            }


            return new BaseTL[] { ret };
        }
        
        return null;
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new DefaultTemplate();
    }
}

