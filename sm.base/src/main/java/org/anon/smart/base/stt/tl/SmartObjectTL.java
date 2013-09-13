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
 * File:                org.anon.smart.base.stt.tl.SmartObjectTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A smart object template
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.tl;

import java.util.List;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

import org.anon.smart.base.annot.SmartDataAnnotate;
import org.anon.smart.base.annot.StatesAnnotate;

import org.anon.utilities.exception.CtxException;

public class SmartObjectTL extends BaseTL
{
    private String merge;
    private String commit;
    private String store;
    private String config;
    private SpaceTL space;
    private KeyTL key;
    private List<StateTL> states;
    private List<AttributeTL> attributes;

    public SmartObjectTL()
    {
    }

    public SpaceTL getSpace() { return space; }
    public KeyTL getKey() { return key; }
    public List<StateTL> getStates() { return states; }
    public List<AttributeTL> getAttributes() { return attributes; }

    @Override
    public Class[] getAnnotations(String name)
        throws CtxException
    {
        List<Class> annons = new ArrayList<Class>();
        Class[] annots = super.getAnnotations(name);
        for (int i = 0; (annots != null) && (i < annots.length); i++)
            annons.add(annots[i]);

        annons.add(SmartDataAnnotate.class);
        annons.add(StatesAnnotate.class);
        return annons.toArray(new Class[0]);
    }

    @Override
    public boolean shouldAdd(String type)
    {
        return true;
    }

    static void populateDefaults(SmartObjectTL ret, String type, String clsname, String key, String flow)
    {
        BaseTL.populateDefault(ret, clsname, type, flow);
        ret.states = new ArrayList<StateTL>();
        ret.states.add(StateTL.getDefaultStartState());
        ret.states.add(StateTL.getDefaultEndState());
        ret.attributes = new ArrayList<AttributeTL>();
        ret.attributes.add(AttributeTL.getKeyAttribute(key));
    }

    public static SmartObjectTL defaultFor(String clsname, String type, String flow, String[] parms)
    {
        SmartObjectTL ret = new SmartObjectTL();
        populateDefaults(ret, type, clsname, parms[0], flow);
        return ret;
    }
}

