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
 * File:                org.anon.smart.base.utils.AnnotationUtils
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of utils for annotation specific to core
 *
 * ************************************************************
 * */

package org.anon.smart.base.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.anon.smart.base.annot.BaseAnnotate;
import org.anon.smart.base.annot.ConfigAnnotate;
import org.anon.smart.base.annot.EventAnnotate;
import org.anon.smart.base.annot.SmartDataAnnotate;
import org.anon.smart.base.annot.PrimeDataAnnotate;
import org.anon.smart.base.annot.KeyAnnotate;
import org.anon.smart.base.annot.Synthetic;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class AnnotationUtils
{
    private AnnotationUtils()
    {
    }

    public static boolean isSynthetic(Field fld)
    {
        boolean ret = false;
        if (fld != null)
        {
            Annotation[] annots = fld.getAnnotations();
            String synthetic = Synthetic.class.getName();
            for (int i = 0; (!ret) && (i < annots.length); i++)
            {
                if (annots[i].annotationType().getName().equals(synthetic))
                    ret = true;
            }
        }

        return ret;
    }

    public static String objectName(Object data)
        throws CtxException
    {
        assertion().assertNotNull(data, "Cannot find the name of a null data");
        return className(data.getClass());
    }

    public static String className(Class cls)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find the name of a null class");
        BaseAnnotate annot = (BaseAnnotate)reflect().getAnnotation(cls, BaseAnnotate.class);
        if (annot != null)
            return annot.name();

        return null;
    }

    public static String typeFor(Class cls)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find the type of a null class");
        BaseAnnotate annot = (BaseAnnotate)reflect().getAnnotation(cls, BaseAnnotate.class);
        if (annot != null)
            return annot.type();

        return null;
    }

    public static String flowFor(Class cls)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find the flow of a null class");
        BaseAnnotate annot = (BaseAnnotate)reflect().getAnnotation(cls, BaseAnnotate.class);
        if (annot != null)
            return annot.flow();

        return null;
    }

    public static String configFor(Class cls)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find the config of a null class");
        SmartDataAnnotate annot = (SmartDataAnnotate)reflect().getAnnotation(cls, SmartDataAnnotate.class);
        System.out.println("Annot is: " + annot);
        if (annot != null)
            return annot.config();

        PrimeDataAnnotate pannot = (PrimeDataAnnotate)reflect().getAnnotation(cls, PrimeDataAnnotate.class);
        System.out.println("Annot is: " + pannot);
        if (pannot != null)
            return pannot.config();

        return null;
    }

    public static String commitFor(Class cls)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find the commit of a null class");
        SmartDataAnnotate annot = (SmartDataAnnotate)reflect().getAnnotation(cls, SmartDataAnnotate.class);
        System.out.println("Annot is: " + annot);
        if (annot != null)
            return annot.commit();

        PrimeDataAnnotate pannot = (PrimeDataAnnotate)reflect().getAnnotation(cls, PrimeDataAnnotate.class);
        System.out.println("Annot is: " + pannot);
        if (pannot != null)
            return pannot.commit();

        return null;
    }

    public static String filterFor(Class cls)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find the filter for a null class");
        EventAnnotate annot = (EventAnnotate)reflect().getAnnotation(cls, EventAnnotate.class);
        if (annot != null)
            return annot.filter();

        return null;
    }

    public static Class[] keyTypes(Class cls)
        throws CtxException
    {
        assertion().assertNotNull(cls, "Cannot find the keytypes for null class");
        Field[] flds = reflect().getAnnotatedFields(cls, KeyAnnotate.class);
        assertion().assertNotNull(flds, "The class does not contain keys associated: " + cls.getName());
        Class[] ret = new Class[flds.length];
        //only get the ones that are not synthetic
        for (int i = 0; i < flds.length; i++)
        {
            if (!isSynthetic(flds[i]))
                ret[i] = flds[i].getType();
        }
        return ret;
    }

    public static boolean isConfig(Class cls)
        throws CtxException
    {
        ConfigAnnotate annot = (ConfigAnnotate)reflect().getAnnotation(cls, ConfigAnnotate.class);
        return (annot != null);
    }
}

