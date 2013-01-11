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
 *
 * ************************************************************
 * HEADERS
 * ************************************************************
 * File:                org.anon.smart.base.test.loader.TestLoader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A test case to test loader
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.loader;

import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.stt.STTRegister;

import org.anon.utilities.test.PathHelper;

public class TestLoader
{
    private void assertFieldAnnotations(java.lang.reflect.Field fld)
        throws Exception
    {
        java.lang.annotation.Annotation[] annots = fld.getAnnotations();
        assertTrue(annots != null);
        assertTrue(annots.length == 1);
        assertTrue(annots[0].annotationType().getName().equals("org.anon.smart.base.annot.Synthetic"));
    }

    @Test
    public void testTestLoader()
        throws Exception
    {
        STTRegister.registerSTT("SmartData", "org.anon.smart.base.test.loader.SmartDataStereoType");
        STTRegister.registerSTT("TagObject", "org.anon.smart.base.test.loader.TagObjectStereoType");
        STTRegister.registerSTT("Object", "org.anon.smart.base.test.loader.ObjectStereoType");
        SmartLoader ldr = new SmartLoader(new URL[]{ new URL(PathHelper.getProjectBuildPath()), new URL(PathHelper.getProjectTestBuildPath())},
                new String[]{});
        Class cls = ldr.loadClass("org.anon.smart.base.test.loader.TestSmartData");
        assertTrue(cls != null);
        java.lang.reflect.Method mthd = cls.getDeclaredMethod("initializeMe");
        assertTrue(mthd != null);
        java.lang.reflect.Field fld = cls.getDeclaredField("__smart__id__");
        assertTrue(fld != null);
        assertFieldAnnotations(fld);
        Object obj = cls.newInstance();
        fld.setAccessible(true);
        Object val = fld.get(obj);
        assertTrue(val != null);
        mthd = cls.getDeclaredMethod("getTest");
        mthd.invoke(obj);
        mthd = cls.getDeclaredMethod("getName");
        mthd.invoke(obj);
        fld = cls.getDeclaredField("__tag__name__");
        assertTrue(fld != null);
        assertFieldAnnotations(fld);
        fld.setAccessible(true);
        val = fld.get(obj);
        assertTrue(val != null);
        assertTrue(val.equals(cls.getName()));
        Class[] ints = cls.getInterfaces();
        assertTrue(ints != null);
        System.out.println("interfaces:" + ints.length);
        assertTrue(ints.length == 1);
        assertTrue(ints[0].getName().equals("org.anon.utilities.fsm.StateEntity"));
        fld = cls.getDeclaredField("__object__type__");
        assertTrue(fld != null);
        fld.setAccessible(true);
        val = fld.get(obj);
        assertTrue(val != null);
        fld = cls.getDeclaredField("__current__state__");
        assertFieldAnnotations(fld);
        assertTrue(fld != null);

        fld = cls.getDeclaredField("_test");
        assertTrue(fld != null);
        java.lang.annotation.Annotation[] fannots = fld.getAnnotations();
        System.out.println("Test Annotation: " + fannots.length);
        for (int i = 0; i < fannots.length; i++)
            System.out.println("Test Annotation: " + fannots[i]);

        fld = cls.getDeclaredField("_name");
        assertTrue(fld != null);
        fannots = fld.getAnnotations();
        System.out.println("Name Annotation: " + fannots.length);
        for (int i = 0; i < fannots.length; i++)
            System.out.println("Name Annotation: " + fannots[i]);

        java.lang.annotation.Annotation[] annots = cls.getAnnotations();
        for (int i = 0; i < annots.length; i++)
            System.out.println("Annotation: " + annots[i]);
    }
}

