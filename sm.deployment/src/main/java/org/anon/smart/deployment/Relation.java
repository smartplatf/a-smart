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
 * File:                org.anon.smart.deployment.Relation
 * Author:              rsankar
 * Revision:            1.0
 * Date:                13-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A relation between objects
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

public class Relation implements VerifiableObject
{
    private String data;
    private String dataKey;
    private String relatedTo;
    private String relatedBy;

    private boolean _verified;

    public Relation()
    {
    }

    public boolean isVerified() { return _verified; }
    public boolean verify()
        throws CtxException
    {
        //TODO:
        _verified = true;
        return _verified;
    }
}

