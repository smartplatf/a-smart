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
 * File:                org.anon.smart.base.stt.StereoTypes
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of stereotypes for any given class
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.base.stt.tl.TemplateReader;

import org.anon.utilities.exception.CtxException;

public class StereoTypes
{
    private List<String> _types;
    private List<STTDescriptor> _descriptors;

    public StereoTypes(BaseTL[] annotations, ClassLoader ldr)
        throws CtxException
    {
        List<String> types = new ArrayList<String>();
        for (int i = 0; i < annotations.length; i++)
        {
            String[] stypes = annotations[i].getTypes();
            for (int j = 0; (stypes != null) && (j < stypes.length); j++)
            {
                if (!types.contains(stypes[j]))
                    types.add(stypes[j]);
            }
        }
        //checking
        String[] extras = TemplateReader.getExtraTypes();
        for (int i = 0; (extras != null) && (i < extras.length); i++)
        {
            if (!types.contains(extras[i]))
                types.add(extras[i]);
        }

        _types = types;
        _descriptors = new ArrayList<STTDescriptor>();
        addSTTDescriptors(types, ldr);
    }

    private void addSTTDescriptors(List<String> types, ClassLoader ldr)
        throws CtxException
    {
        List<String> extra = new ArrayList<String>();
        for (int i = 0; i < types.size(); i++)
        {
            STTReader reader = STTRegister.currentReader();
            STTDescriptor desc = STTRegister.getSTTDescriptor(types.get(i), reader, ldr);
            _descriptors.add(desc);
            extra.addAll(desc.extraTypes());
        }

        if (extra.size() > 0)
            addSTTDescriptors(extra, ldr);
    }

    public List<String> interfaces()
    {
        List<String> ints = new ArrayList<String>();
        for (STTDescriptor desc : _descriptors)
            ints.addAll(desc.interfaces());
        return ints;
    }

    public List<CustomSTT> custom()
        throws CtxException
    {
        List<CustomSTT> custom = new ArrayList<CustomSTT>();
        for (STTDescriptor desc : _descriptors)
        {
            List<CustomSTT> custs = desc.custom();
            custom.addAll(custs);
        }

        return custom;
    }

    public List<MethodDet> enterMethods(String name)
    {
        List<MethodDet> mthds = new ArrayList<MethodDet>();
        for (STTDescriptor desc : _descriptors)
        {
            MethodDet det = desc.enterMethod(name);
            if (det != null) 
            {
                mthds.add(det);
            }
        }
        return mthds;
    }

    public List<MethodDet> exitMethods(String name)
    {
        List<MethodDet> mthds = new ArrayList<MethodDet>();
        for (STTDescriptor desc : _descriptors)
        {
            MethodDet det = desc.exitMethod(name);
            if (det != null) 
            {
                mthds.add(det);
            }
        }
        return mthds;
    }

    public List<STTDescriptor> sttdescriptors() { return _descriptors; }
}

