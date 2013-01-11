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
 * File:                org.anon.smart.base.stt.tl.ClassTemplate
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * This searches for a direct configuration on the class recognized by class name
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt.tl;

import java.util.Map;
import java.io.InputStream;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import org.anon.utilities.config.Format;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class ClassTemplate implements SearchTemplate
{
    public ClassTemplate()
    {
    }

    private String getTemplateFile(String cls)
    {
        String file = cls.replaceAll("\\.", "/");
        file += ".soa";
        return file;
    }

    public boolean templateFound(String cls, ClassLoader ldr)
    {
        String file = getTemplateFile(cls);
        if (ldr.getResource(file) != null)
            return true;
        return false;
    }

    public BaseTL[] searchFor(String cls, ClassLoader ldr)
        throws CtxException
    {
        String file = getTemplateFile(cls);
        InputStream str = ldr.getResourceAsStream(file);
        if (str != null)
        {
            Format fmt = config().readYMLConfig(str);
            Map vals = fmt.allValues();
            return TemplateReader.readConfig(vals);
        }

        return null;
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new ClassTemplate();
    }
}

