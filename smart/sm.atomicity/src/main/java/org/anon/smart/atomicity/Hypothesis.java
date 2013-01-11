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
 * File:                org.anon.smart.atomicity.Hypothesis
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An hypothesis that implies the certainity or uncertainity of the atomicity
 *
 * ************************************************************
 * */

package org.anon.smart.atomicity;

import java.util.List;

import org.anon.utilities.exception.CtxException;

public interface Hypothesis
{
    public EmpiricalData collect(EmpiricalData data)
        throws CtxException;
    public boolean outcome()
        throws CtxException;
    public List<EmpiricalData> searchEmpiricalData(String[] regextag)
        throws CtxException;
    public List<EmpiricalData> empiricalDataFor(String tag)
        throws CtxException;
}

