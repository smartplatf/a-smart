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
    private static final String KEY_SEPARATOR = "|";
    private static Map<Class<? extends Annotation>, ArtefactType> ARTEFACTS = new HashMap<Class<? extends Annotation>, ArtefactType>();

    private String _typeName;
    private Class<? extends Annotation> _recognizer;
    private String _namekey;
    private Method _nameMethod;
    private Method[] _keyMethods;
    private String[] _keys;

    private ArtefactType(String typename, Class<? extends Annotation> annot, String namekey, String ... keys)
        throws CtxException
    {
        try
        {
            _typeName = typename;
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
            assertion().assertNotNull(cls, "Cannot find artefacttype for null cls ");
            assertion().assertNotNull(_recognizer, "Cannot find artefacttype for null recognizer " + cls);
            Class<? extends Annotation> recog = (Class<? extends Annotation>)cls.getClassLoader().loadClass(_recognizer.getName());
            Annotation annot = cls.getAnnotation(recog);
            assertion().assertNotNull(annot, "The class does not have the recognizer annotation");
            Method nmthd = recog.getDeclaredMethod(_namekey);
            return (String)nmthd.invoke(annot);
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
            Class<? extends Annotation> recog = (Class<? extends Annotation>)cls.getClassLoader().loadClass(_recognizer.getName());
            Annotation annot = cls.getAnnotation(recog);
            assertion().assertNotNull(annot, "The class does not have the recognizer annotation");
            Method[] kmthds = new Method[_keys.length];
            for (int i = 0; i < _keys.length; i++)
                kmthds[i] = recog.getDeclaredMethod(_keys[i]);
            for (int i = 0; i < kmthds.length; i++)
            {
                String val = (String)kmthds[i].invoke(annot);
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
                            String add = addTo + KEY_SEPARATOR + str[j];
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

    public static String[] getKeyParts(String key)
    {
        return key.split("\\" + KEY_SEPARATOR);
    }

    public static String[] createKeys(String[] keyparts, String[] val, int ind)
    {
        List<String> k = new ArrayList<String>();

        for (int i = 0; i < val.length; i++)
        {
            if (ind == 0)
                k.add(val[i]);
            else
                k.add(keyparts[0]);
        }

        for (int i = 1; i < keyparts.length; i++)
        {
            String add = keyparts[i];
            for (int j = 0; j < val.length; j++)
            {
                String curr = k.get(j);
                if (i == ind)
                    add = val[j];
                curr = curr + KEY_SEPARATOR + add;
                k.set(j, curr);
            }
        }

        return k.toArray(new String[0]);
    }

    public static String createKey(String ... keyvals)
        throws CtxException
    {
        String ret = "";
        String add = "";
        String useadd = "";
        boolean containswild = false;
        for (int i = 0; !containswild && (i < keyvals.length); i++)
        {
            if (keyvals[i].indexOf("*") > 0)
            {
                containswild = true;
                useadd = "\\";
            }
        }

        for (int i = 0; i < keyvals.length; i++)
        {
            ret += add + keyvals[i];
            add = useadd + KEY_SEPARATOR;
        }

        return ret;
    }

    public static void registerArtefactType(String typename, Class<? extends Annotation> annot, String namekey, String ... keys)
        throws CtxException
    {
        if (ARTEFACTS.containsKey(annot))
            return;

        ArtefactType t = new ArtefactType(typename, annot, namekey, keys);
        ARTEFACTS.put(annot, t);
    }

    public static ArtefactType artefactTypeFor(String name)
    {
        for (ArtefactType t : ARTEFACTS.values())
        {
            if (t._typeName.equals(name))
                return t;
        }

        return null;
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
            try
            {
                Class<? extends Annotation> check = (Class<? extends Annotation>)ArtefactType.class.getClassLoader().loadClass(annots[i].annotationType().getName());
                if (ARTEFACTS.containsKey(check))
                    ret.add(ARTEFACTS.get(check));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                except().rt(e, new CtxException.Context("", ""));
            }
        }

        return ret.toArray(new ArtefactType[0]);
    }

    public String toString() { return _typeName; }
}

