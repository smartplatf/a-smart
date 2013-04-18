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
 * File:                org.anon.smart.base.stt.Templatizer
 * Author:              rsankar
 * Revision:            1.0
 * Date:                28-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A stereotypist that adds templates to the class
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import java.io.InputStream;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.base.stt.tl.TemplateReader;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.loader.ResourceMod;
import org.anon.utilities.exception.CtxException;

public class Templatizer implements StereoTypist, ResourceMod
{
    public Templatizer()
    {
    }

    public boolean modifies(String className, ClassLoader ldr)
        throws CtxException
    {
        return TemplateReader.shouldTemplatize(className, ldr);
    }

    public byte[] typecast(InputStream istr, String cls, ClassLoader ldr)
        throws CtxException
    {
        //DO NOT USE THE parms field, can't getit correctly. Have to try other options such as
        //threadlocals
        byte[] ret = io().readBytes(istr);
        BaseTL[] temp = TemplateReader.readTemplate(cls, ldr);
        if (temp != null)
        {
            STTVisitor visit = new STTVisitor(cls, temp, ldr);
            int cnt = 0;
            BCIerFactory fact = STTRegister.supportedstt.currentBCIer();
            BCIer bcier = fact.nextBCIer(cnt);
            while (bcier != null)
            {
                ret = bcier.bci(ret, visit, ldr);
                cnt++;
                bcier = fact.nextBCIer(cnt);
            }
        }
        return ret;
    }

    public byte[] modify(InputStream str, String cls, ClassLoader ldr)
        throws CtxException
    {
        return typecast(str, cls, ldr);
    }
}

