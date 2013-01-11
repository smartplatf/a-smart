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
 * File:                org.anon.smart.base.stt.MethodContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A context for method stereotype
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.util.List;
import java.util.ArrayList;

public class MethodContext extends BaseSTTContext implements Constants
{
    private List<String> _exceptions;
    private String _description;

    public MethodContext(ClazzDescriptor descriptor, String name, String signature, int access, String description, String[] exceptions)
    {
        super(descriptor, name, signature, access);
        _exceptions = new ArrayList<String>();
        for (int i = 0; (exceptions != null) && (i < exceptions.length); i++)
            _exceptions.add(exceptions[i]);
        _description = description;
    }

    public String description() { return _description; }
    public String[] exceptions() { return _exceptions.toArray(new String[0]); }

    public void addException(String exception) 
    { 
        if (!_exceptions.contains(exception)) 
            _exceptions.add(exception); 
    }

    public void removeException(String exception)
    {
        if (_exceptions.contains(exception))
            _exceptions.remove(exception);
    }

    public void changeDescription(String desc)
    {
        _description = desc;
    }

    public boolean shouldBCI(MethodDet det)
    {
        boolean bci = det.inMethod().equals(ALL_VALUE);
        if (!bci && (det.inMethod().equals(CONSTRUCTOR_VAL) && name().equals(CONSTRUCTOR_NAME)))
            bci = true;

        if (!bci && (det.inMethod().equals(name())))
            bci = true;

        return bci;
    }

    public List<MethodDet> enterMethods()
    {
        return _descriptor.enterMethods(name());
    }

    public List<MethodDet> exitMethods()
    {
        return _descriptor.exitMethods(name());
    }
}

