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
 * File:                org.anon.smart.smcore.test.CoreServerUtilities
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of utilities to run server here
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.test;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.test.ServerUtilities;
import org.anon.smart.base.test.ModConstants;

import org.anon.utilities.test.PathHelper;

public class CoreServerUtilities extends ServerUtilities implements ModConstants
{
    public CoreServerUtilities(int port)
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
        mod.add(new URL(PathHelper.getJar(true, BASE)));
        return mod.toArray(new URL[0]);
    }

    @Override
    protected String[] getModules()
    {
        String[] comps = new String[] { "org.anon.smart.smcore.anatomy.SMCoreModule", "org.anon.smart.base.test.testanatomy.TestModule" };
        return comps;
    }


}

