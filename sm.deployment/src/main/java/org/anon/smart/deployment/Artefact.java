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

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class Artefact implements Deployable
{
    private String _name;
    private Class _clazz;
    private ArtefactType _type;
    private String[] _keys;

    private Artefact(Class clazz, ArtefactType type)
        throws CtxException
    {
        _clazz = clazz;
        _type = type;
        _keys = type.getKeys(clazz);
        _name = type.getName(clazz);
    }

    public String[] getKeys() { return _keys; }
    public String getName() { return _name; }
    public Class getClazz() { return _clazz; }
    public ArtefactType getType() { return _type; }
    public Class getClazz(ClassLoader ldr) 
        throws CtxException
    { 
        try
        {
            return ldr.loadClass(_clazz.getName()); 
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
            ret[i] = new Artefact(clazz, t[i]);
        return ret;
    }
}

