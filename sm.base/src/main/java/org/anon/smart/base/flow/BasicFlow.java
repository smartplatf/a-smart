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
 * File:                org.anon.smart.base.flow.BasicFlow
 * Author:              rsankar
 * Revision:            1.0
 * Date:                12-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A basic flow object that will be used for deployed flows
 *
 * ************************************************************
 * */

package org.anon.smart.base.flow;

import java.util.UUID;

public class BasicFlow implements FlowObject, java.io.Serializable
{
    private FlowModel _model;
    private UUID _id;

    public BasicFlow(FlowModel model)
    {
        _model = model;
        _id = UUID.randomUUID();
    }

    public UUID flowID() { return _id; }
    public String flowName() { return _model.name(); }
    public FlowModel model() { return _model; }
}

