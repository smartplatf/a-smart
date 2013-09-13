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
 * File:                org.anon.smart.base.dspace.FSMDataFilter
 * Author:              rsankar
 * Revision:            1.0
 * Date:                30-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data filter that only allows active objects through
 *
 * ************************************************************
 * */

package org.anon.smart.base.dspace;

import org.anon.smart.d2cache.DataFilter;

import org.anon.utilities.fsm.FiniteState;
import org.anon.utilities.fsm.StateEntity;

import org.anon.utilities.exception.CtxException;

public class FSMDataFilter implements DataFilter
{
    private DSpace _space;

    public FSMDataFilter()
    {
    }

    public void setupSpace(DSpace space)
    {
        _space = space;
    }

    public boolean filterObject(Object obj, DataFilter.dataaction action, boolean except)
        throws CtxException
    {
        //allow all objects other than the ones in the final state
        if (obj instanceof StateEntity)
        {
            StateEntity data = (StateEntity)obj;
            FiniteState state = data.utilities___currentState();
            if (state.finalState())
                return false;
        }

        return true;
    }

}

