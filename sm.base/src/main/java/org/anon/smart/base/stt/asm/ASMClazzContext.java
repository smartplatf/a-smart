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
 * File:                org.anon.smart.base.stt.asm.ASMClazzContext
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A clazz context used by asm
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.asm;

import org.objectweb.asm.ClassVisitor;

import org.anon.smart.base.stt.ClazzContext;
import org.anon.smart.base.stt.ClazzDescriptor;

public class ASMClazzContext extends ClazzContext
{
    private ClassVisitor _visitor;

    public ASMClazzContext(ClazzDescriptor descriptor, String name, String signature, int access, int version, String superclazz, 
            String[] interfaces, ClassVisitor visitor)
    {
        super(descriptor, name, signature, access, version, superclazz, interfaces);
        _visitor = visitor;
    }

    void setClassVisitor(ClassVisitor cv) { _visitor = cv; }
    public ClassVisitor visitor() { return _visitor; }
}

