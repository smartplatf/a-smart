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
 * File:                org.anon.smart.base.test.testanatomy.TestContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test context based on which the module is initialized
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.testanatomy;

import org.anon.smart.base.anatomy.SmartModuleContext;
import org.anon.smart.base.dspace.DSpaceAuthor;
import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.loader.LoaderVars;
import org.anon.smart.base.dspace.DefaultAuthor;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.anatomy.JVMEnvironment;
import org.anon.utilities.exception.CtxException;

public class TestContext implements SmartModuleContext
{
    public TestContext()
    {
    }

    public JVMEnvironment vmEnvironment()
        throws CtxException
    {
        return null; //use default
    }

    public ClassLoader smartLoader(ClassLoader ldr, String name)
        throws CtxException
    {
        if (ldr instanceof SmartLoader)
        {
            LoaderVars vars = new LoaderVars(name);
            SmartLoader replicate = (SmartLoader)ldr;
            return (SmartLoader)replicate.repeatMe(vars);
        }

        except().te(this, "Cannot replicate a non SmartLoader.");
        return null;
    }

    public DSpaceAuthor spaceAuthor()
        throws CtxException
    {
        return new DefaultAuthor();
    }

    public String[] getEnableFlows()
    {
        return null;
    }
}

