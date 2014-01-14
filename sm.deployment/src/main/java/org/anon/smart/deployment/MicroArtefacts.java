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
 * File:                org.anon.smart.deployment.MicroArtefacts
 * Author:              rsankar
 * Revision:            1.0
 * Date:                12-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A list of all artefacts deployed into the platform
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.concurrent.ConcurrentHashMap;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class MicroArtefacts
{
    private Map<String, Artefact> _artefacts;
    private Map<String, Artefact[]> _clsArtefacts;

    public MicroArtefacts()
    {
        _artefacts = new ConcurrentHashMap<String, Artefact>();
        _clsArtefacts = new ConcurrentHashMap<String, Artefact[]>();
    }

    /*
     * It is assumed that each class a set of unique keys that do not collide with the keys
     * of other Deployed Classes. Data, events, responses can be deployed with only names. Transitions
     * can be deployed with name+object+event, which will make it unique. To get a list of classes
     * for object+event, a wild char matching pattern can be used such as *|object|event
     */
    public Artefact[] deployClazz(Class clazz)
        throws CtxException
    {
        if (_clsArtefacts.containsKey(clazz.getName()))
            return _clsArtefacts.get(clazz.getName());

        Artefact[] artefacts = Artefact.artefactsFor(clazz);
        _clsArtefacts.put(clazz.getName(), artefacts);
        addToArtefacts(artefacts);
        return artefacts;
    }

    private void addToArtefacts(Artefact[] artefacts)
        throws CtxException
    {
        for (int i = 0; i < artefacts.length; i++)
        {
            String[] keys = artefacts[i].getKeys();
            for (int j = 0; j < keys.length; j++)
            {
	            if (_artefacts.containsKey(keys[j]))
                    except().te(this, "An artefact for key: " + keys[j] + " already exists, duplicate deployment.");
                _artefacts.put(keys[j], artefacts[i]);
            }
        }
    }

    public Artefact[] deployArtefacts(Artefact[] arts, Map<String, List<String>> links)
        throws CtxException
    {
        if (arts == null)
            return arts;

        List<Artefact> ret = new ArrayList<Artefact>();
        Artefact[] artefacts = Artefact.artefactsFor(arts, links);
        Map<String, List<Artefact>> group = new HashMap<String, List<Artefact>>();
        for (int i = 0; i < artefacts.length; i++)
        {
            List<Artefact> lart = group.get(artefacts[i].getClazz().getName());
            if (lart == null)
                lart = new ArrayList<Artefact>();

            lart.add(artefacts[i]);
            group.put(artefacts[i].getClazz().getName(), lart);
        }

        for (String clzName : group.keySet())
        {
            if (!_clsArtefacts.containsKey(clzName))
            {
                List<Artefact> lclsarts = group.get(clzName);
                Artefact[] clsarts = lclsarts.toArray(new Artefact[0]);
                _clsArtefacts.put(clzName, clsarts);
                addToArtefacts(clsarts);
                ret.addAll(lclsarts);
            }
            else
            {
                Artefact[] clsarts = _clsArtefacts.get(clzName);
                for (int i = 0; i < clsarts.length; i++)
                    ret.add(clsarts[i]);
            }
        }

        return ret.toArray(new Artefact[0]);
    }

    public Artefact[] artefactsForClazz(String clsname)
    {
        return _clsArtefacts.get(clsname);
    }

    public Artefact artefactFor(String key)
    {
        return _artefacts.get(key);
    }

    public Class clazzFor(String key, ArtefactType type, ClassLoader ldr, Deployment d)
        throws CtxException
    {
        if (_artefacts.containsKey(key))
        {
            Artefact a = _artefacts.get(key);
            if (a.getType().equals(type))
                return a.getClazz(ldr, d);
        }

        return null;
    }

    public List<Class> clazzezFor(String wild, ArtefactType type, ClassLoader ldr, Deployment d)
        throws CtxException
    {
        List<Class> ret = new ArrayList<Class>();
        Pattern p = Pattern.compile(wild);
        for (String k : _artefacts.keySet())
        {
            Matcher m = p.matcher(k);
            if (m.matches())
            {
                Artefact a = _artefacts.get(k);
                if (a.getType().equals(type))
                    ret.add(a.getClazz(ldr, d));
            }
        }

        return ret;
    }
}

