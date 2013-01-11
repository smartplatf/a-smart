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
 * File:                org.anon.smart.base.stt.MethodDet
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Details of method to be invoked in a method enter or exit
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

public class MethodDet implements Constants
{
    private String _name;
    private int _access;
    private String _signature;
    private String _inMethod;

    public MethodDet(String name, int access, String signature, String callIn)
    {
        _name = name;
        _access = access;
        _signature = signature;
        _inMethod = callIn;
    }

    public String name() { return _name; }
    public int access() { return _access; }
    public String signature() { return _signature; }
    public String inMethod() { return _inMethod; }

    public boolean hasClazzParam()
    {
        return (_signature.equals(CLASS_PARAMS) || (_signature.equals(CLASS_MTHD_PARAMS)));
    }

    public boolean hasMthdParam()
    {
        return (_signature.equals(CLASS_MTHD_PARAMS));
    }
}

