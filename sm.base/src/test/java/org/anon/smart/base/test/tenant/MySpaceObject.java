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
 * File:                org.anon.smart.base.test.tenant.MySpaceObject
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An object generally there so I can test object put
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.tenant;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.dspace.DSpaceObject;

import org.anon.utilities.exception.CtxException;

public class MySpaceObject implements DSpaceObject, java.io.Serializable
{
    private String _name;

    public MySpaceObject(String nm)
    {
        _name = nm;
    }

    public List<Object> smart___keys()
        throws CtxException
    {
        List<Object> keys = new ArrayList<Object>();
        keys.add(_name);
        return keys;
    }

    public String smart___objectGroup()
        throws CtxException
    {
        return "MySpaceObject";
    }

	@Override
	public void smart___initOnLoad() throws CtxException {
		// TODO Auto-generated method stub
		
	}

}

