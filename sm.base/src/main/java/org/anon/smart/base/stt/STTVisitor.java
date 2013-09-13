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
 * File:                org.anon.smart.base.stt.STTVisitor
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A visitor which needs to be called for all stt
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.anon.smart.base.stt.tl.BaseTL;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import org.anon.utilities.exception.CtxException;

public class STTVisitor
{
    private ClazzDescriptor _descriptor;
    private List<CustomSTT> _custom;
    private ClazzContext _clsContext;
    private List<BaseTL> _attributes;
    private List<BaseTL> _actions;

    public STTVisitor(String clazz, BaseTL[] annotate, ClassLoader ldr)
        throws CtxException
    {
        _descriptor = new ClazzDescriptor(clazz, annotate, ldr);
        _custom = _descriptor.custom();
        _attributes = _descriptor.attributes();
        _actions = _descriptor.actions();
        List<BaseTL> services = _descriptor.services();
        _actions.addAll(services);
    }

    public void visit(ClazzContext ctx)
    {
        _clsContext = ctx;
        for (CustomSTT custom : _custom)
            custom.visit(ctx);
    }

    public void visit(FieldContext ctx)
    {
        for (CustomSTT custom : _custom)
            custom.visitField(ctx);

        addFieldAnnotations(ctx);
    }

    public void visit(MethodContext ctx)
    {
        for (CustomSTT custom : _custom)
            custom.visitMethod(ctx);

        addMethodAnnotations(ctx);
    }

    public void visitEnd(ClazzContext ctx)
    {
        try
        {
            _clsContext = ctx;
            STTWriter writer = STTRegister.currentWriter();
            List<STTDescriptor> sttdescs = _descriptor.sttdescriptors();

            for (STTDescriptor stt : sttdescs)
            {
                List lst = stt.reader().fields();
                for (Object nde : lst)
                {
                    Object fn = writer.createField(this, stt, nde);
                }

                lst = stt.reader().methods();
                for (Object mthd : lst)
                {
                    Object m = writer.createMethod(this, stt, mthd);
                }
            }

            for (CustomSTT custom : _custom)
                custom.visitEnd(_clsContext);

            addClassAnnotations(_clsContext);
        }
        catch (Exception e)
        {
            //TODO: Log
        }

    }

    public ClazzDescriptor descriptor() { return _descriptor; }
    public ClazzContext clazzContext() { return _clsContext; }

    private void addAnnotField(Object annotation, STTWriter write, STTDescriptor descriptor, String fldname, Object val, String name, Class rclazz)
        throws CtxException
    {
        if (val == null) 
            return;

        if (type().isAssignable(val.getClass(), rclazz))
        {
            write.annotateFieldValue(annotation, val, name);
        }
        else if (rclazz.isArray())
        {
            assertion().assertTrue((val.getClass().isArray() || (val instanceof Collection)), "Cannot force a non array to an array");
            Object arrannotation = write.createAnnotation(this, descriptor, null, annotation, name);
            Class comp = rclazz.getComponentType();
            Collection coll = convert().objectToCollection(val, false);
            for (Object cval : coll)
            {
                assertion().assertTrue((cval instanceof BaseTL), "Cannot force a non annotation to an annotation");
                addAnnotations(write, descriptor, fldname, arrannotation, (BaseTL)cval, new ArrayList<Class>());
            }
            write.endAnnotation(arrannotation);
        }
        else if (type().isAssignable(rclazz, Annotation.class))
        {
            assertion().assertTrue((val instanceof BaseTL), "Cannot force a non annotation to an annotation");
            BaseTL tl = (BaseTL)val;
            addAnnotations(write, descriptor, fldname, annotation, tl, new ArrayList<Class>());
        }
    }

    private void addAnnotations(STTWriter write, STTDescriptor descriptor, String fldname, Object addto, BaseTL annot, List<Class> added)
        throws CtxException
    {
        String name = fldname;
        if ((descriptor != null) && (name == null) && (addto != null))
            name = descriptor.reader().getName(addto);
        Class<? extends Annotation>[] annotations = annot.getAnnotations(name);
        for (int i = 0; (annotations != null) && (i < annotations.length); i++)
        {
            if ((annotations[i] != null) && (!added.contains(annotations[i])))
            {
                Object annotation = write.createAnnotation(this, descriptor, annotations[i], addto, null);
                Method[] mthds = annotations[i].getDeclaredMethods();
                for (int j = 0; j < mthds.length; j++)
                {
                    Class rclazz = mthds[j].getReturnType();
                    String nm = mthds[j].getName();
                    Object val = annot.findValue(nm);
                    addAnnotField(annotation, write, descriptor, fldname, val, nm, rclazz);
                }
                write.endAnnotation(annotation);
            }
        }
    }

    public void addFieldAnnotations(STTWriter write, STTDescriptor stt, Object addTo, String fldname)
    {
        try
        {
            for (int i = 0; (_attributes != null) && (i < _attributes.size()); i++)
            {
                BaseTL b = _attributes.get(i);
                addAnnotations(write, stt, fldname, addTo, b, new ArrayList<Class>());
            }
        }
        catch (Exception e)
        {
            //TODO:
            e.printStackTrace();
        }
    }

    public void addFieldAnnotations(FieldContext ctx)
    {
        try
        {
            STTWriter writer = STTRegister.currentWriter();
            for (int i = 0; (_attributes != null) && (i < _attributes.size()); i++)
            {
                BaseTL b = _attributes.get(i);
                addAnnotations(writer, null, ctx.name(), ctx, b, new ArrayList<Class>());
            }
        }
        catch (Exception e)
        {
            //TODO
            e.printStackTrace();
        }
    }

    public void addMethodAnnotations(MethodContext ctx)
    {
        try
        {
            STTWriter writer = STTRegister.currentWriter();
            for (int i = 0; (_actions != null) && (i < _actions.size()); i++)
            {
                BaseTL b = _actions.get(i);
                addAnnotations(writer, null, ctx.name(), ctx, b, new ArrayList<Class>());
            }
        }
        catch (Exception e)
        {
            //TODO
            e.printStackTrace();
        }
    }

    public void addClassAnnotations(ClazzContext ctx)
    {
        try
        {
            STTWriter writer = STTRegister.currentWriter();
            BaseTL[] annots = _descriptor.getAnnotations();
            for (int i = 0; i < annots.length; i++)
            {
                addAnnotations(writer, null, ctx.name(), ctx, annots[i], new ArrayList<Class>());
            }
        }
        catch (Exception e)
        {
            //TODO
            e.printStackTrace();
        }
    }
}

