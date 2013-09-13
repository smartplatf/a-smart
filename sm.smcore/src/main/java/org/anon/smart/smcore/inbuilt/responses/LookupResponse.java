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
 * File:                org.anon.smart.smcore.inbuilt.responses.LookupResponse
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 6, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.responses;

import java.util.List;
import java.util.ArrayList;

public class LookupResponse implements java.io.Serializable
{
    private List<Object> result;

    public LookupResponse(Object o) {
		result = new ArrayList<Object>();
        if (o != null)
            result.add(o);
	}
    public List<Object> getResult(){ return result; }

    public String toString()
    {
        return "Result is: " + result;
    }
}
