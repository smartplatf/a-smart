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
 * File:                org.anon.smart.smcore.channel.distill.alteration.AlteredData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                21-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Altered data to be used
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.alteration;

import java.util.List;

import org.anon.smart.channels.distill.Isotope;

public class AlteredData extends Isotope
{
    public static class FlowEvent
    {
        Object flowObject;
        Object evtObject;

        public FlowEvent(Object f, Object e)
        {
            flowObject = f;
            evtObject = e;
        }

        public Object flow() { return flowObject; }
        public Object event() { return evtObject; }
    }

    private List<FlowEvent> _events;

    public AlteredData(Isotope parent, List<FlowEvent> evts)
    {
        super(parent);
        _events = evts;
    }

    public List<FlowEvent> events() { return _events; }
}

