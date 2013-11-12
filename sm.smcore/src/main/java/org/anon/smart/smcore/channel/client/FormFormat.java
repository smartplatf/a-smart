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
 * File:                org.anon.smart.smcore.channel.client.FormFormat
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-10-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A form formatter, that formats data as a form post
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.client;

import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;

public class FormFormat
{
    private Map _values;

    public FormFormat(Map values)
    {
        _values = values;
        if (values == null)
            _values = new HashMap();
    }

    public String toString()
    {
        String ret = "";
        try
        {
            String add = "";
            for (Object k : _values.keySet())
            {
                if (_values.get(k) != null)
                    ret = ret + add + k + "=" + URLEncoder.encode(_values.get(k).toString(), "UTF-8");
                add = "&";
            }
        }
        catch (Exception e)
        {
            //Should not happen, if it does need to fix at develop time.
            e.printStackTrace();
        }
        return ret;
    }
}

