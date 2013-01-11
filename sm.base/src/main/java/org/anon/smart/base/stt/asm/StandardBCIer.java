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
 * File:                org.anon.smart.base.stt.asm.StandardBCIer
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A BCI which bci's the templates
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;

import org.anon.smart.base.stt.STTVisitor;
import org.anon.smart.base.stt.BCIer;

public class StandardBCIer implements BCIer
{
    public StandardBCIer()
    {
    }

    public byte[] bci(byte[] bytes, STTVisitor visit, ClassLoader ldr)
        throws CtxException
    {
        try
        {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter cw = new LoaderAwareCW(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES, ldr);
            StandardVisitor visitor = new StandardVisitor(cw, visit);
            reader.accept(visitor, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Error BCI: StandardVisitor", e.getMessage()));
        }

        return null;
    }
}

