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
 * File:                org.anon.smart.smcore.channel.distill.translation.FormTranslator
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-12-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A translator of form data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.translation;

import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;

import org.anon.smart.smcore.channel.distill.ChannelConstants;

import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class FormTranslator implements ChannelConstants
{
    public FormTranslator()
    {
    }

    public static Map<String, Object> readStream(InputStream in)
        throws CtxException
    {
        StringBuffer buff = io().readStream(in);
        String str = buff.toString();

        Map<String, Object> ret = new HashMap<String, Object>();

        Map<String, Object> key = null;
        String keyname = null;

        String[] data = str.split("&");
        for (int i = 0; (data != null) && (i < data.length); i++)
        {
            String[] d = data[i].split("=");
            assertion().assertNotNull(d, "Wrongly formatted form data.");
            assertion().assertTrue(d.length == 2, "Wrongly formatted form data. Need to be of format key=data&key=data");
            ret.put(d[0], d[1]);

            if (d[0].equals("keyobject"))
            {
                keyname = d[1];
            }
            else if (d[0].equals("keyvalue"))
            {
                key = new HashMap<String, Object>();
                key.put(ACTION, LOOKUP_ACTION);
                key.put(VALUE, d[1]);
            }
        }

        assertion().assertNotNull(keyname, "Need to post data to a specific data.");
        assertion().assertNotNull(key, "Need to post data to a specific data. No value found.");

        ret.put(keyname, key);
        return ret;
    }
}

