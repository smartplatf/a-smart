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
 * File:                org.anon.smart.smcore.stt.tl.ConfigTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A template for config object related settings
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt.tl;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.base.annot.ConfigAnnotate;

import org.anon.utilities.exception.CtxException;

public class ConfigTL extends BaseTL
{
    public ConfigTL()
    {
        super();
    }

    @Override
    public Class[] getAnnotations(String name)
        throws CtxException
    {
        List<Class> annons = new ArrayList<Class>();
        Class[] annots = super.getAnnotations(name);
        for (int i = 0; (annots != null) && (i < annots.length); i++)
            annons.add(annots[i]);

        annons.add(ConfigAnnotate.class);
        return annons.toArray(new Class[0]);
    }

    public static ConfigTL defaultFor(String clsname, String type, String flow, String[] parms)
    {
        ConfigTL ret = new ConfigTL();
        BaseTL.populateDefault(ret, clsname, type, flow);
        return ret;
    }

    @Override
    public boolean shouldAdd(String type)
    {
        return true;
    }

}

