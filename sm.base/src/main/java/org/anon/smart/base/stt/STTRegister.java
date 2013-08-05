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
 * File:                org.anon.smart.base.stt.STTRegister
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a register where all typecasts are registered for use
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.utils.VMSingleton;
import org.anon.smart.base.stt.asm.ASMSTTReader;
import org.anon.smart.base.stt.asm.ASMBCIers;
import org.anon.smart.base.stt.asm.ASMSTTWriter;

public final class STTRegister extends VMSingleton
{
    private static final String STTREGNAME = "org.anon.smart.base.stt.STTRegister";

    private static STTRegister SINGLE_INSTANCE = null;

    private static void setSingleInstance(Object obj)
    {
        if (SINGLE_INSTANCE == null)
            SINGLE_INSTANCE = (STTRegister)obj;
    }

    private static Object getSingleInstance()
    {
        return SINGLE_INSTANCE;
    }

    enum supportedstt
    {
        asm(ASMSTTReader.class, ASMSTTWriter.class, new ASMBCIers());

        private Class<? extends STTReader> _readerCls;
        private Class<? extends STTWriter> _writerCls;
        private BCIerFactory _factory;

        private supportedstt(Class<? extends STTReader> cls, Class<? extends STTWriter> writer, BCIerFactory fact)
        {
            _readerCls = cls;
            _factory = fact;
            _writerCls = writer;
        }

        private STTReader getReader()
            throws CtxException
        {
            try
            {
                return _readerCls.newInstance();
            }
            catch (Exception e)
            {
                except().rt(e, new CtxException.Context("Error Reader Cls:", _readerCls.getName()));
            }

            return null;
        }

        private STTWriter getWriter()
            throws CtxException
        {
            try
            {
                return _writerCls.newInstance();
            }
            catch (Exception e)
            {
                except().rt(e, new CtxException.Context("Error Writer Cls:", _writerCls.getName()));
            }

            return null;
        }

        private static supportedstt currentstt()
        {
            String provider = System.getenv("BCI_PROVIDER");
            if (provider == null)
                provider = "asm";

            //default or not available stt, defaults to asm
            supportedstt current = supportedstt.valueOf(provider);
            if (current == null)
                current = supportedstt.asm;
            return current;
        }

        static STTReader currentReader()
            throws CtxException
        {
            supportedstt current = currentstt();
            return current.getReader();
        }

        static BCIerFactory currentBCIer()
        {
            supportedstt current = currentstt();
            return current._factory;
        }

        static STTWriter currentWriter()
            throws CtxException
        {
            supportedstt current = currentstt();
            return current.getWriter();
        }
    }

    private Map<String, String> _register;
    private Map<String, STTDescriptor> _cache;

    private STTRegister()
    {
        _register = new HashMap<String, String>();
        _cache = new HashMap<String, STTDescriptor>();
    }

    private InputStream getSTTStream(String type, ClassLoader ldr)
        throws CtxException
    {
        String name = _register.get(type);
        name = name.replaceAll("\\.", "/");
        name = name + ".class";
        return ldr.getResourceAsStream(name);
    }

    private void registerType(String name, String clsname)
    {
        _register.put(name, clsname);
    }

    private STTDescriptor getDescriptor(String type, STTReader reader, ClassLoader ldr)
        throws CtxException
    {
        if (!_cache.containsKey(type))
        {
            synchronized(_cache)
            {
                if (!_cache.containsKey(type))
                {
                    InputStream istr = getSTTStream(type, ldr);
                    STTDescriptor desc = new STTDescriptor(type, istr, reader, ldr);
                    _cache.put(type, desc);
                }
            }
        }
        STTDescriptor desc = _cache.get(type);
        return desc;
    }

    private STTReader getReader()
        throws CtxException
    {
        return supportedstt.currentReader();
    }

    private STTWriter getWriter()
        throws CtxException
    {
        return supportedstt.currentWriter();
    }

    public static void registerSTT(String name, String clsname)
        throws CtxException
    {
        STTRegister reg = (STTRegister)getInstance(STTREGNAME);
        reg.registerType(name, clsname);
    }

    public static STTDescriptor getSTTDescriptor(String type, STTReader reader, ClassLoader ldr)
        throws CtxException
    {
        STTRegister reg = (STTRegister)getInstance(STTREGNAME);
        return reg.getDescriptor(type, reader, ldr);
    }

    public static STTReader currentReader()
        throws CtxException
    {
        STTRegister reg = (STTRegister)getInstance(STTREGNAME);
        return reg.getReader();
    }

    public static STTWriter currentWriter()
        throws CtxException
    {
        STTRegister reg = (STTRegister)getInstance(STTREGNAME);
        return reg.getWriter();
    }
}

