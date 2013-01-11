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
 * File:                org.anon.smart.base.stt.asm.ASMSTTWriter
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A write that writes ASM related codes
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.asm;

import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;

import org.anon.smart.base.stt.STTVisitor;
import org.anon.smart.base.stt.STTWriter;
import org.anon.smart.base.stt.STTDescriptor;
import org.anon.smart.base.stt.Constants;

public class ASMSTTWriter implements STTWriter, Constants
{
    public ASMSTTWriter()
    {
    }

    public Object createMethod(STTVisitor sttvisit, STTDescriptor stt, Object mthd)
    {
        ASMClazzContext cctx = (ASMClazzContext)sttvisit.clazzContext();
        ClassVisitor visit = cctx.visitor();
        MethodNode nde = (MethodNode)mthd;
        if (!nde.name.equals(CONSTRUCTOR_NAME)) //ignore constructor
        {
            String[] exceptions = new String[nde.exceptions.size()];
            nde.exceptions.toArray(exceptions); //TODO check the reference for this
            MethodVisitor mv = visit.visitMethod( nde.access, nde.name, nde.desc, nde.signature, exceptions);
            nde.instructions.resetLabels();
            nde.accept(new RemappingMethodAdapter(nde.access, nde.desc, mv, new SimpleRemapper(stt.reader().sttname(), cctx.name())));
            return nde;
        }
        return null;
    }

    public Object createField(STTVisitor sttvisit, STTDescriptor stt, Object field)
    {
        ASMClazzContext cctx = (ASMClazzContext)sttvisit.clazzContext();
        ClassVisitor visit = cctx.visitor();
        FieldNode sfn = (FieldNode)field;
        FieldNode fn = new FieldNode(sfn.access, sfn.name, sfn.desc, sfn.signature, sfn.value);
        List<AnnotationNode> annons = sfn.visibleAnnotations;
        if (annons != null)
        {
            for (AnnotationNode annon : annons)
                fn.visitAnnotation(annon.desc, true);
        }

        sttvisit.addFieldAnnotations(this, stt, sfn, sfn.name);
        fn.accept(visit);
        return fn;
    }

    public Object createAnnotation(STTVisitor sttvisit, STTDescriptor stt, Class clz, Object whichobj, String arrname)
    {
        Object which = whichobj;
        String aname = arrname;
        if (clz != null)
            aname = Type.getDescriptor(clz);

        if (whichobj instanceof ASMClazzContext)
            which = ((ASMClazzContext)whichobj).visitor();
        else if (whichobj instanceof ASMFieldContext)
            which = ((ASMFieldContext)whichobj).visitor();
        if (whichobj instanceof ASMMethodContext)
            which = ((ASMMethodContext)whichobj).visitor();

        AnnotationVisitor av = null;
        if (which instanceof FieldVisitor) 
            av = ((FieldVisitor)which).visitAnnotation(aname, true);
        else if (which instanceof MethodVisitor) 
            av = ((MethodVisitor)which).visitAnnotation(aname, true);
        else if (which instanceof ClassVisitor)
            av = ((ClassVisitor)which).visitAnnotation(aname, true);
        else if ((which instanceof AnnotationVisitor) && (arrname == null))
            av = ((AnnotationVisitor)which).visitAnnotation(null, aname);
        else if ((which instanceof AnnotationVisitor) && (arrname != null))
            av = ((AnnotationVisitor)which).visitArray(arrname);
                    
        return av;
    }

    public Object annotateFieldValue(Object a, Object val, String name)
    {
        AnnotationVisitor visit = (AnnotationVisitor)a;
        visit.visit(name, val);
        return visit;
    }

    public Object endAnnotation(Object a)
    {
        AnnotationVisitor visit = (AnnotationVisitor)a;
        visit.visitEnd();
        return visit;
    }
}

