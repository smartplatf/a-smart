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
 * File:                org.anon.smart.smcore.stt.TransitionSTT
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An stt for transition classes
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt;

import org.anon.utilities.pool.Pool;
import org.anon.utilities.pool.PoolEntity;

public class TransitionSTT implements PoolEntity
{
    private PoolEntity _nextEntity;
    private Pool _pool;

    public TransitionSTT()
    {
    }

    public PoolEntity nextEntity()
    {
        return _nextEntity;
    }

    public void setNextEntity(PoolEntity entity)
    {
        _nextEntity = entity;
    }

    public Pool pool()
    {
        return _pool;
    }

    public void storePool(Pool p)
    {
        _pool = p;
    }
}

