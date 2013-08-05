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
 * File:                org.anon.smart.secure.test.SecureServerUtilities
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A server starter for secure server
 *
 * ************************************************************
 * */

package org.anon.smart.secure.test;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.test.ModConstants;
import org.anon.smart.smcore.test.CoreServerUtilities;
import org.anon.smart.secure.loader.SmartSecureLoader;

import org.anon.utilities.test.PathHelper;

import org.anon.utilities.anatomy.CrossLinkApplication;

public class SecureServerUtilities extends CoreServerUtilities implements ModConstants
{
    public SecureServerUtilities(int port)
    {
        super(port);
    }

    @Override
    protected URL[] getURLs()
        throws Exception
    {
        URL[] urls = super.getURLs();
        List<URL> mod = new ArrayList<URL>();
        for (int i = 0; i < urls.length; i++)
            mod.add(urls[i]);
        mod.add(new URL(PathHelper.getJar(true, CORE)));
        return mod.toArray(new URL[0]);
    }

    @Override
    protected String[] getModules()
    {
        String[] comps = new String[] { 
            "org.anon.smart.secure.anatomy.SecureModule", 
            "org.anon.smart.smcore.anatomy.SMCoreModule", 
            "org.anon.smart.base.test.testanatomy.TestModule" 
        };
        return comps;
    }

    @Override
    protected SmartLoader createLoader()
        throws Exception
    {
        URL[] urls = getURLs();
        String[] comps = getModules();
        SmartLoader ldr = new SmartSecureLoader(urls, comps);
        CrossLinkApplication.getApplication().setStartLoader(ldr);
        return ldr;
    }


}

