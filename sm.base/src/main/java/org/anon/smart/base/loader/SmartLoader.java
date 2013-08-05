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
 * File:                org.anon.smart.base.loader.SmartLoader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A class loader used in smart platform
 *
 * ************************************************************
 * */

package org.anon.smart.base.loader;

import java.net.URL;

import org.anon.smart.base.stt.Templatizer;

import org.anon.utilities.loader.RelatedLoader;
import org.anon.utilities.exception.CtxException;

public class SmartLoader extends RelatedLoader
{
    static
    {
        addForceLoadSuper("org.anon.smart.base.stt.*");
        addForceLoadSuper("org.anon.smart.base.loader.*");
        //addForceLoadSuper("org.anon.smart.base.annot.*");
    }

    public SmartLoader(URL[] urls, String name, String[] comps)
        throws CtxException
    {
        super(urls, name, comps);
        addResourceMod(new Templatizer());
    }

    public SmartLoader(URL[] urls, String[] comps)
        throws CtxException
    {
        this(urls, "unreferenced:SmartLoader", comps);
    }

    public SmartLoader repeatMe(LoaderVars lvars)
        throws CtxException
    {
        SmartLoader ldr = new SmartLoader(this.getURLs(), lvars.getName(), _initComps);
        return ldr;
    }

    public String[] addExtraSTT(String type)
    {
        return new String[0];
    }
}

