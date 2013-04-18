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
 * File:                org.anon.smart.atomicity.TruthData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Data that is transactable
 *
 * ************************************************************
 * */

package org.anon.smart.atomicity;

import java.util.UUID;

import org.anon.utilities.exception.CtxException;

public interface TruthData
{
    public UUID truthID();
    public boolean start(UUID txnid)
        throws CtxException;
    public boolean simulate(UUID txnid, EmpiricalData edata)
        throws CtxException;
    public boolean accept(UUID txnid, EmpiricalData edata)
        throws CtxException;
    public void discard(UUID txnid, EmpiricalData edata)
        throws CtxException;
    public boolean end(UUID txnid)
        throws CtxException;
    public void recordEmpiricalData(UUID txnid, EmpiricalData edata)
        throws CtxException;
}

