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
 * File:                org.anon.smart.base.stt.STTDescriptor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A description of the current typecasting
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.anon.smart.base.loader.SmartLoader;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public class STTDescriptor implements Constants
{
    private STTReader _reader;
    private List<Class> _custom;
    private List<String> _extraTypes;
    private List<String> _interfaces;
    private Map<String, MethodDet> _enterCalls;
    private Map<String, MethodDet> _exitCalls;

    public STTDescriptor(String type, InputStream istr, STTReader reader, ClassLoader ldr)
        throws CtxException
    {
        _reader = reader;

        _custom = new ArrayList<Class>();
        _extraTypes = new ArrayList<String>();
        _interfaces = new ArrayList<String>();
        _enterCalls = new HashMap<String, MethodDet>();
        _exitCalls = new HashMap<String, MethodDet>();

        _reader.initialize(istr);
        _reader.readClassAnnotations(this);
        _reader.readMethodAnnotations(this);
        _reader.readClassInterfaces(this);
        _reader.addFieldAnnotation(SYNTHETIC_CLASS);

        if (ldr instanceof SmartLoader)
        {
            SmartLoader sldr = (SmartLoader)ldr;
            String[] extras = sldr.addExtraSTT(type);
            for (int i = 0; (extras != null) && (i < extras.length); i++)
                addExtraType(extras[i]);
        }
    }

    public void addCustom(String name)
        throws CtxException
    {
        try
        {
            _custom.add(Class.forName(name));
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Error loading class for: " + name, "Adding custom"));
        }
    }

    public void addExtraType(String extra)
    {
        if (!_extraTypes.contains(extra))
            _extraTypes.add(extra);
    }

    public void addInterface(String inter)
    {
        if (!_interfaces.contains(inter))
            _interfaces.add(inter);
    }

    private void addMethodCall(String name, int access, String signature, String callIn, Map<String, MethodDet> calls)
        throws CtxException
    {
        if (!signature.equals(CLASS_PARAMS) && !signature.equals(CLASS_MTHD_PARAMS) && !signature.equals(NO_PARAMS))
        {
            except().te("BCI method does not have standard signatures." + signature);
        }

        if (!_reader.validate(access))
            except().te("BCI method with access: " + name + ":" + access);

        if ((callIn != null) && (callIn.length() > 0))
        {
            String[] multiple = value().listAsString(callIn);
            for (int i = 0; i < multiple.length; i++)
            {
                MethodDet mthd = new MethodDet(name, access, signature, multiple[i]);
                //assumption only one method call reqd in a single stt. for multiple, just create
                //one with all calls and annotate the super method.
                calls.put(multiple[i], mthd);
            }
        }
        else
        {
            MethodDet mthd = new MethodDet(name, access, signature, ALL_VALUE);
            calls.put(ALL_VALUE, mthd);
        }
    }

    public void addEnterMethodCall(String name, int access, String signature, String callIn)
        throws CtxException
    {
        addMethodCall(name, access, signature, callIn, _enterCalls);
    }

    public void addExitMethodCall(String name, int access, String signature, String callIn)
        throws CtxException
    {
        addMethodCall(name, access, signature, callIn, _exitCalls);
    }

    public List<String> extraTypes() { return _extraTypes; }
    public List<String> interfaces() { return _interfaces; }

    public List<CustomSTT> custom()
        throws CtxException
    {
        List<CustomSTT> cust = new ArrayList<CustomSTT>();
        for (int i = 0; (_custom != null) && (i < _custom.size()); i++)
        {
            CustomSTT obj = _reader.createCustom(_custom.get(i));
            cust.add(obj);
        }
        return cust;
    }

    public MethodDet enterMethod(String name)
    {
        if ((name != null) && (_enterCalls != null) && (_enterCalls.containsKey(name)))
            return _enterCalls.get(name);

        if ((_enterCalls != null) && !name.equals(CONSTRUCTOR_NAME))
            return _enterCalls.get(ALL_VALUE);

        return null;
    }

    public MethodDet exitMethod(String name)
    {
        if (name.equals(CONSTRUCTOR_NAME))
            name = CONSTRUCTOR_VAL;

        if ((name != null) && (_exitCalls != null) && (_exitCalls.containsKey(name)))
            return _exitCalls.get(name);

        if (_exitCalls != null)
            return _exitCalls.get(ALL_VALUE);

        return null;
    }

    public STTReader reader() { return _reader; }
}

