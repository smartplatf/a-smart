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
 * File:                org.anon.smart.smcore.data.datalinks.LinkedData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                18-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data stored for all links
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.data.datalinks;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.dspace.DSpaceObject;

import org.anon.utilities.exception.CtxException;

public class LinkedData implements DSpaceObject, java.io.Serializable
{
    private Object _linkKey;
    private List<UUID> _links;

    private String _linkType;
    private String _linkedType;

    public LinkedData(String l, String lo, Object key)
    {
        _linkType = l;
        _linkedType = lo;
        _linkKey = key;
        _links = new ArrayList<UUID>();
    }

    public void addLink(UUID link)
    {
        if (!_links.contains(link))
            _links.add(link);
    }

    public void removeLink(UUID link)
    {
        if (_links.contains(link))
            _links.remove(link);
    }

    public List<Object> smart___keys()
    {
        List<Object> keys = new ArrayList<Object>();
        keys.add(_linkKey);
        return keys;
    }

    public String linkType() { return _linkType; }
    public String linkedType() { return _linkedType; }
    public List<UUID> getLinks() { return _links; }
    public Object getKey() { return _linkKey; }
    public String smart___objectGroup()
        throws CtxException
    {
        return _linkType + "__" + _linkedType;
    }

    public String toString()
    {
        return "LinkType: " + _linkType + ":" + _linkedType + ":" + _linkKey + ":" + _links;
    }

	public void smart___initOnLoad() 
        throws CtxException 
    {
        //if (_links == null)
         //   _links = new ArrayList<UUID>();
    }
}

