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
 * File:                org.anon.smart.base.stt.asm.ASMSTTReader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A stt reader for bci using asm
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.asm;

import java.util.List;
import java.io.InputStream;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.smart.base.stt.STTDescriptor;
import org.anon.smart.base.stt.STTReader;
import org.anon.smart.base.stt.Constants;
import org.anon.smart.base.stt.CustomSTT;
import org.anon.utilities.exception.CtxException;

public class ASMSTTReader implements STTReader, Constants
{
    private ClassNode _clazz;

    public ASMSTTReader()
    {
    }

    public void initialize(InputStream istr)
        throws CtxException
    {
        try
        {
            ClassReader reader = new ClassReader(istr);
            _clazz = new ClassNode();
            reader.accept(_clazz, ClassReader.EXPAND_FRAMES);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Error initializing: ", "ASMSTTReader"));
        }
    }

    public void readClassAnnotations(STTDescriptor desc)
        throws CtxException
    {
        List custom = _clazz.visibleAnnotations;
        if (custom == null)
            return;

        String cvcls = Type.getDescriptor(CUSTOMVISIT_CLASS);
        String sttcls = Type.getDescriptor(INCLUDESTT_CLASS);
        for (Object o : custom)
        {
            AnnotationNode a = (AnnotationNode)o;
            List lst = (List)a.values.get(1);
            if (a.desc.equals(cvcls))
            {
                for (Object o1 : lst)
                {
                    Type t = (Type)o1;
                    desc.addCustom(t.getClassName());
                }
            }
            else if (a.desc.equals(sttcls))
            {
                for (Object o1 : lst)
                    desc.addExtraType(o1.toString());
            }
        }
    }

    public void readMethodAnnotations(STTDescriptor desc)
        throws CtxException
    {
        String callmenter = Type.getDescriptor(METHODENTER_CLASS);
        String callmexit = Type.getDescriptor(METHODEXIT_CLASS);

        for (Object m : _clazz.methods)
        {
            MethodNode mn = (MethodNode)m;
            List annons = mn.visibleAnnotations;
            if (annons == null)
                continue;
            for (Object a : annons)
            {
                AnnotationNode an = (AnnotationNode)a;
                String name = mn.name;
                int access = mn.access;
                String sign = mn.desc;
                List vals = an.values;
                String callIn = null;
                if ((vals != null) && (vals.size() >= 2) && (vals.get(1) != null))
                    callIn = vals.get(1).toString();
                if (an.desc.equals(callmenter))
                    desc.addEnterMethodCall(name, access, sign, callIn);
                else if (an.desc.equals(callmexit))
                    desc.addExitMethodCall(name, access, sign, callIn);
            }
        }
    }

    public void readClassInterfaces(STTDescriptor desc)
        throws CtxException
    {
        List<String> interfaces = _clazz.interfaces;
        for (String inter : interfaces)
            desc.addInterface(inter);
    }

    public void addFieldAnnotation(Class annot)
        throws CtxException
    {
        for (Object f : _clazz.fields)
        {
            FieldNode fn = (FieldNode)f;
            String annotcls = Type.getDescriptor(annot);
            fn.visitAnnotation(annotcls, true);
        }
    }

    public boolean validate(int access)
        throws CtxException
    {
        if ((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC)
            return false;

        return true;
    }

    public CustomSTT createCustom(Class cls)
        throws CtxException
    {
        return null;//TODO
    }

    public List fields() { return _clazz.fields; }
    public List methods() { return _clazz.methods; }
    public String sttname() { return _clazz.name; }

    public String getName(Object obj) 
    {
        String ret = "";
        if (obj instanceof FieldNode)
            ret = ((FieldNode)obj).name;
        else if (obj instanceof MethodNode)
            ret = ((MethodNode)obj).name;
        else if (obj instanceof ClassNode)
            ret = ((ClassNode)obj).name;

        return ret;
    }
}

