/**
 * Utilities - Utilities used by anon
 *
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of Utilities.
 *
 * Utilities is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Utilities is distributed in the hope that it will be useful,
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
 * File:                org.anon.smart.base.stt.asm.LoaderAwareCW
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A Class writer that is aware of the classloader to use for loading classes
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class LoaderAwareCW extends ClassWriter
{
    private ClassLoader _loader;

    public LoaderAwareCW(ClassReader cr, int pint, ClassLoader ldr)
    {
        super(cr, pint);
        _loader = ldr;
    }

    @Override
    protected String getCommonSuperClass(String paramString1, String paramString2)
    {
	    Class localClass1;
	    Class localClass2;
	    ClassLoader localClassLoader = _loader;
	    try
	    {
            localClass1 = Class.forName(paramString1.replace('/', '.'), false, localClassLoader);
            localClass2 = Class.forName(paramString2.replace('/', '.'), false, localClassLoader);
	    }
	    catch (Exception localException)
	    {
            throw new RuntimeException(localException.toString());
	    }
	    if (localClass1.isAssignableFrom(localClass2))
            return paramString1;
	    if (localClass2.isAssignableFrom(localClass1))
	        return paramString2;
	    if ((localClass1.isInterface()) || (localClass2.isInterface()))
	        return "java/lang/Object";
	    do
        {
	        localClass1 = localClass1.getSuperclass();
        } while (!(localClass1.isAssignableFrom(localClass2)));
	    return localClass1.getName().replace('.', '/');
    }
}

