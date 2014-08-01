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
 * File:                org.anon.smart.base.stt.tl.AttributeTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A template for attribute specification
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.tl;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

import org.anon.smart.base.annot.DestinationAnnotate;
import org.anon.smart.base.annot.KeyAnnotate;
import org.anon.smart.d2cache.store.index.solr.NoIndex;

public class AttributeTL extends BaseTL
{
    private static Map<String, Class> _annotationMapping;

    private static void initialize()
    {
        if (_annotationMapping == null)
        {
            _annotationMapping = new HashMap<String, Class>();
            _annotationMapping.put("destination", DestinationAnnotate.class);
            _annotationMapping.put("key", KeyAnnotate.class);
            _annotationMapping.put("noindex", NoIndex.class);
        }
    }

    private String attribute;
    private String configName;
    private String groups;

    public AttributeTL()
    {
    }

    public String getAttribute() { return attribute; }
    public String getConfigName() { return configName; }
    public String getGroups() { return groups; }

    public Class[] getAnnotations(String name)
    {
        initialize();
        List<Class> annons = new ArrayList<Class>();
        if (name.equals(attribute))
        {
            String type = getType();
            if (type != null)
            {
                Class an = _annotationMapping.get(type);
                if (an != null)
                    annons.add(an);
            }
        }

        return annons.toArray(new Class[0]);
    }

    public static AttributeTL getKeyAttribute(String key)
    {
        AttributeTL tl = new AttributeTL();
        tl.attribute = key;
        tl.type = "key";
        return tl;
    }
}

