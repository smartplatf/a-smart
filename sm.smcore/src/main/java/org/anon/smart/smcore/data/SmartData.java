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
 * File:                org.anon.smart.smcore.data.SmartData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An interface for smart data implemented by all data objects
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.data;

import java.util.UUID;

import org.anon.smart.base.dspace.DSpaceObject;

import org.anon.utilities.fsm.StateEntity;
import org.anon.utilities.exception.CtxException;

public interface SmartData extends StateEntity, TaggedData, DSpaceObject
{
    public String smart___name()
        throws CtxException;
    public UUID smart___id();
    public String smart___owner();
    public String smart___group();
    public void smart___transition(String tostate)
        throws CtxException;
    public SmartDataTruth smart___myTruth()
        throws CtxException;
    public void smart___setGroup(String grp);
    public void smart___setOwner(String owner);

    public void smart___setIsNew(boolean n);
}

