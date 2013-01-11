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
 * File:                org.anon.smart.base.stt.ClazzContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a context for class stereotype
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.util.List;
import java.util.ArrayList;

public class ClazzContext extends BaseSTTContext
{
    private int _version;
    private String _superClazz;
    private List<String> _interfaces;

    public ClazzContext(ClazzDescriptor descriptor, String name, String signature, int access, int version, String superClazz, String[] interfaces)
    {
        super(descriptor, name, signature, access);
        descriptor.setInternalClazzName(name);
        _version = version;
        _superClazz = superClazz;
        _interfaces = new ArrayList<String>();
        for (int i = 0; i < interfaces.length; i++)
            _interfaces.add(interfaces[i]);

        List<String> newinterfaces = descriptor.interfaces();
        _interfaces.addAll(newinterfaces);
    }

    public int version() { return _version; }
    public String superClazz() { return _superClazz; }
    public String[] interfaces() { return _interfaces.toArray(new String[0]); }

    public void addInterface(String inter) 
    {
        if (!_interfaces.contains(inter))
            _interfaces.add(inter);
    }

    public void removeInterface(String inter)
    {
        if (_interfaces.contains(inter))
            _interfaces.remove(inter);
    }

    public void changeSuper(String sup)
    {
        _superClazz = sup;
    }
}

