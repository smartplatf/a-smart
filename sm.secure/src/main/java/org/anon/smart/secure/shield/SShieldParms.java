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
 * File:                org.anon.smart.secure.shield.SShieldParms
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of parameters passed to create a shield
 *
 * ************************************************************
 * */

package org.anon.smart.secure.shield;

import org.anon.smart.secure.access.AccessScope;

import org.anon.utilities.utils.RepeaterVariants;

public class SShieldParms implements RepeaterVariants
{
    private AccessScope _scope;
    private Class _klass;

    public SShieldParms(AccessScope scope, Class klass)
    {
        _scope = scope;
        _klass = klass;
    }

    public AccessScope getScope() { return _scope; }
    public Class getKlass() { return _klass; }
}

