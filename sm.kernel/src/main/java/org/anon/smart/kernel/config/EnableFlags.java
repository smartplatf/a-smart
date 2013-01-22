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
 * File:                org.anon.smart.kernel.config.EnableFlags
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A configuration for enable configurations
 *
 * ************************************************************
 * */

package org.anon.smart.kernel.config;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

public class EnableFlags implements VerifiableObject, java.io.Serializable
{
    private String secure;
    private String distribute;

    public EnableFlags()
    {
    }

    public boolean isDistribute()
    {
        return convert().stringToBoolean(distribute);
    }

    public boolean isSecure()
    {
        return convert().stringToBoolean(secure);
    }

    public boolean getEnabled(String flag)
        throws CtxException
    {
        return convert().stringToBoolean((String)reflect().getAnyFieldValue(this.getClass(), this, flag));
    }

    public boolean isVerified() { return true; }

    public boolean verify()
        throws CtxException
    {
        return true;
    }
}

