/**
 * Utilities - Utilities used by anon
 *
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of Utilities.
 *
 * Utilities is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Utilities is distributed in the hope that it will be useful,
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
 * File:                org.anon.smart.base.stt.ClazzDescriptor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A descritor for the given class based on which it is loaded
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.base.stt.tl.AttributeTL;

import org.anon.utilities.exception.CtxException;

public class ClazzDescriptor implements Constants
{
    private String _clazzName;
    private StereoTypes _stereotypes;
    private BaseTL[] _annotations;
    private List<CustomSTT> _custom;
    private String _internalClazzName;

    public ClazzDescriptor(String clazz, BaseTL[] annotations, ClassLoader ldr)
        throws CtxException
    {
        _clazzName = clazz;
        _annotations = annotations;
        _stereotypes = new StereoTypes(annotations, ldr);
        _custom = _stereotypes.custom();
    }

    public List<String> interfaces() { return _stereotypes.interfaces(); }
    public List<CustomSTT> custom() { return _custom; }
    public String clazzName() { return _internalClazzName; }
    public void setInternalClazzName(String name) { _internalClazzName = name; }

    public List<MethodDet> enterMethods(String name)
    {
        return _stereotypes.enterMethods(name);
    }

    public List<MethodDet> exitMethods(String name)
    {
        return _stereotypes.exitMethods(name);
    }

    public List<STTDescriptor> sttdescriptors() { return _stereotypes.sttdescriptors(); }
    public BaseTL[] getAnnotations() { return _annotations; }

    private List<BaseTL> collectionValues(String nm)
        throws CtxException
    { 
        List<BaseTL> attrs = new ArrayList<BaseTL>();
        for (int i = 0; i < _annotations.length; i++)
        {
            Object val = _annotations[i].findValue(nm);
            if ((val != null) && (val instanceof Collection))
            {
                Collection coll = (Collection)val;
                for (Object o : coll)
                    attrs.add((BaseTL)o);
            }
            else if ((val != null) && (val instanceof BaseTL))
                attrs.add((BaseTL)val);
        }
        return attrs; 
    }

    public List<BaseTL> attributes() 
        throws CtxException
    {
        return collectionValues(ATTRIBUTE_CONFIG);
    }

    public List<BaseTL> actions()
        throws CtxException
    {
        return collectionValues(ACTION_CONFIG);
    }

    public List<BaseTL> services()
        throws CtxException
    {
        return collectionValues(SERVICE_CONFIG);
    }
}

