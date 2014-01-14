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
 * File:                org.anon.smart.deployment.Artefact
 * Author:              rsankar
 * Revision:            1.0
 * Date:                12-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An artifact that is deployed
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class Artefact implements Deployable
{
    private String _name;
    private Class _clazz;
    private ArtefactType _type;
    private String[] _keys;

    private Map<String, List<String>> _links;

    private Artefact(Class clazz, ArtefactType type)
        throws CtxException
    {
        _clazz = clazz;
        _type = type;
        _keys = type.getKeys(clazz);
        _name = type.getName(clazz);
    }

    public String[] expandLinks(String key)
    {
        if (_links != null)
        {
            List<String> k = new ArrayList<String>();
            expandMe(key, _links, k, false);
            return k.toArray(new String[0]);
        }

        return new String[] { key };
    }

    private void expandMe(String key, Map<String, List<String>> links, List<String> k, boolean add)
    {
        if (key.indexOf("needslink") >= 0)
        {
            String[] keyparts = ArtefactType.getKeyParts(key);
            //only one part can have a needslink
            String[] val = null;
            int ind = keyparts.length;
            for (int j = 0; j < keyparts.length; j++)
            {
                System.out.println("Checking: " + keyparts[j] + ":" + links.get(keyparts[j]));
                if ((keyparts[j].indexOf("needslink") >= 0) && (links.containsKey(keyparts[j])))
                {
                    List<String> part = links.get(keyparts[j]);
                    String part1 = "";
                    if ((part != null) && (part.size() > 0))
                        part1 = part.get(0);
                    System.out.println("Got: " + keyparts[j] + ":" + part1);
                    if (add) _links.put(keyparts[j], part);
                    String[] lparts = part1.split("\\.");
                    String use = part1;
                    if (lparts.length >= 2)
                        use = lparts[1];
                    val = use.split(",");
                    ind = j;
                    break;
                }
            }

            if (ind < keyparts.length)
            {
                String[] get = ArtefactType.createKeys(keyparts, val, ind);
                for (int l = 0; l < get.length; l++)
                    k.add(get[l]);
            }
            else
                k.add(key); //add as is, there is no link created

            System.out.println("Expanded: " + key + ":" + k + ":" + links);
        }
    }

    private Artefact(Artefact art, Map<String, List<String>> links)
    {
        _name = art._name;
        _clazz = art._clazz;
        _type = art._type;

        List<String> k = new ArrayList<String>();
        _links = new HashMap<String, List<String>>();
        for (int i = 0; i < art._keys.length; i++)
        {
            if (art._keys[i].indexOf("needslink") >= 0)
                expandMe(art._keys[i], links, k, true);
            else
                k.add(art._keys[i]);
        }
        _keys = k.toArray(new String[0]);
    }

    public String[] getKeys() { return _keys; }
    public String getName() { return _name; }
    public Class getClazz() { return _clazz; }
    public ArtefactType getType() { return _type; }
    public Class getClazz(ClassLoader ldr, Deployment d) 
        throws CtxException
    { 
        try
        {
            CrossLinkAny cldeprt = new CrossLinkAny(DeployRuntime.class.getName(), ldr);
            cldeprt.invoke("setupDeploying", new Class[] { Object.class }, new Object[] { d });
            Class ret = ldr.loadClass(_clazz.getName()); 
            cldeprt.invoke("setupDeploying", new Class[] { Object.class }, new Object[] { null });
            return ret;
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Artefact.getClazz", "Exception"));
        }
        return null;
    }

    public String deployedURI()
    {
        return _name;
    }

    public String deployedName()
    {
        return _name;
    }

    public boolean belongsToMe(Class cls)
    {
        return _clazz.equals(cls);
    }

    public static Artefact[] artefactsFor(Class clazz)
        throws CtxException
    {
        ArtefactType[] t = ArtefactType.recognizeArtefactType(clazz);
        Artefact[] ret = new Artefact[t.length];
        for (int i = 0; i < t.length; i++)
        {
            ret[i] = new Artefact(clazz, t[i]);
        }
        return ret;
    }

    public static Artefact[] artefactsFor(Artefact[] arts, Map<String, List<String>> links)
        throws CtxException
    {
        if (arts == null)
            return arts;

        Artefact[] ret = new Artefact[arts.length];
        for (int i = 0; i < arts.length; i++)
            ret[i] = new Artefact(arts[i], links);

        return ret;
    }
}

