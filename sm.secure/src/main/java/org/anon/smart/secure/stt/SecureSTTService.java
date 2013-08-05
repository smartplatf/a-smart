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
 * File:                org.anon.smart.secure.stt.SecureSTTService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A service which registers the template requirements for security
 *
 * ************************************************************
 * */

package org.anon.smart.secure.stt;

import org.anon.smart.base.stt.STTRegister;
import org.anon.smart.base.stt.tl.TemplateReader;
import org.anon.smart.secure.stt.tl.SecurityTL;

import org.anon.utilities.exception.CtxException;

public class SecureSTTService implements Constants
{
    private SecureSTTService()
    {
    }

    public static void initialize()
        throws CtxException
    {
        STTRegister.registerSTT(SECUREDATA, "org.anon.smart.secure.stt.SecureDataSTT");

        TemplateReader.registerTemplate(SECUREDATA, SecurityTL.class);
        TemplateReader.addExtraType(SECUREDATA);
        //if we create an extra BaseTL, that will create an annotation for it.
        //TemplateReader.addAnnotates(SecurityAnnotate.class);
    }
}

