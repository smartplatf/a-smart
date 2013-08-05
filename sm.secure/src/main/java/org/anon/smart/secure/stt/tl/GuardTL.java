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
 * File:                org.anon.smart.secure.stt.tl.GuardTL
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A single guard configuration
 *
 * ************************************************************
 * */

package org.anon.smart.secure.stt.tl;

import java.util.List;

import org.anon.smart.base.stt.tl.BaseTL;
import org.anon.smart.secure.annot.GuardAnnotate;

import org.anon.utilities.exception.CtxException;

public class GuardTL extends BaseTL
{
    private String access;
    private List<String> guardFor;
    private String parm;

    public GuardTL()
    {
    }

    public String getAccess() { return access; }
    public List<String> getGuardFor() { return guardFor; }
    public String getParm() { return parm; }

    @Override
    public Class[] getAnnotations(String name)
        throws CtxException
    {
        return new Class[] { GuardAnnotate.class };
    }


}

