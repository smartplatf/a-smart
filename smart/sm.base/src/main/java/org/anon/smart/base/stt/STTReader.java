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
 * File:                org.anon.smart.base.stt.STTReader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A reader interface that allows an implementation to read a clazz
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.io.InputStream;
import java.util.List;

import org.anon.utilities.exception.CtxException;

public interface STTReader
{
    public void initialize(InputStream istr)
        throws CtxException;

    public void readClassAnnotations(STTDescriptor desc)
        throws CtxException;

    public void readMethodAnnotations(STTDescriptor desc)
        throws CtxException;

    public void readClassInterfaces(STTDescriptor desc)
        throws CtxException;

    public void addFieldAnnotation(Class annot)
        throws CtxException;

    public boolean validate(int access)
        throws CtxException;

    public CustomSTT createCustom(Class cls)
        throws CtxException;

    public List fields();
    public List methods();
    public String sttname();

    public String getName(Object obj);
}

