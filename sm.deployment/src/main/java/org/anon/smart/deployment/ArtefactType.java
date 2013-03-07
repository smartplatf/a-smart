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
 * File:                org.anon.smart.deployment.ArtefactType
 * Author:              rsankar
 * Revision:            1.0
 * Date:                12-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A the type of the artifact deployed
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class ArtefactType
{
    private static Map<Class<? extends Annotation>, ArtefactType> ARTEFACTS = new HashMap<Class<? extends Annotation>, ArtefactType>();

    private Class<? extends Annotation> _recognizer;
    private String _namekey;
    private Method _nameMethod;
    private Method[] _keyMethods;
    private String[] _keys;

    private ArtefactType(Class<? extends Annotation> annot, String namekey, String ... keys)
        throws CtxException
    {
        try
        {
            _recognizer = annot;
            _keys = keys;
            _namekey = namekey;
            _nameMethod = annot.getDeclaredMethod(_namekey);
            _keyMethods = new Method[_keys.length];
            for (int i = 0; i < _keys.length; i++)
                _keyMethods[i] = annot.getDeclaredMethod(_keys[i]);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("ArtefactType::create", "Exception"));
        }
    }

    public Class<? extends Annotation> getRecognizer() { return _recognizer; }

    public boolean isType(Class<? extends Annotation> type)
    {
        return _recognizer.equals(type);
    }

    public String getName(Class cls)
        throws CtxException
    {
        try
        {
            Annotation annot = cls.getAnnotation(_recognizer);
            assertion().assertNotNull(annot, "The class does not have the recognizer annotation");
            return (String)_nameMethod.invoke(annot);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("ArtefactType::getName", "Exception"));
        }

        return "";
    }

    public String[] getKeys(Class cls)
        throws CtxException
    {
        List<String> ret = new ArrayList<String>();
        try
        {
            Annotation annot = cls.getAnnotation(_recognizer);
            assertion().assertNotNull(annot, "The class does not have the recognizer annotation");
            for (int i = 0; i < _keyMethods.length; i++)
            {
                String val = (String)_keyMethods[i].invoke(annot);
                String[] str = value().listAsString(val);
                if (ret.size() <= 0)
                {
                    for (int j = 0; j < str.length; j++)
                        ret.add(str[j]);
                }
                else
                {
                    List<String> newret = new ArrayList<String>();
                    for (int k = 0; k < ret.size(); k++)
                    {
                        String addTo = ret.get(k);
                        for (int j = 0; j < str.length; j++)
                        {
                            String add = addTo + "|" + str[j];
                            newret.add(add);
                        }
                    }
                    ret = newret;
                }
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Artefact.getKeys", "Exception"));
        }
        return ret.toArray(new String[0]);
    }

    public static void registerArtefactType(Class<? extends Annotation> annot, String namekey, String ... keys)
        throws CtxException
    {
        if (ARTEFACTS.containsKey(annot))
            return;

        ArtefactType t = new ArtefactType(annot, namekey, keys);
        ARTEFACTS.put(annot, t);
    }

    public static ArtefactType[] recognizeArtefactType(Class cls)
        throws CtxException
    {
        //assumption is that the annotation has to be present directly
        //on this class and not on the super classes.
        List<ArtefactType> ret = new ArrayList<ArtefactType>();
        Annotation[] annots = cls.getAnnotations();
        for (int i = 0; i < annots.length; i++)
        {
            if (ARTEFACTS.containsKey(annots[i]))
                ret.add(ARTEFACTS.get(annots[i]));
        }

        return ret.toArray(new ArtefactType[0]);
    }
}
