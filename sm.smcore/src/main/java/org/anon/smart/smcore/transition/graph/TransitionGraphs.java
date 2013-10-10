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
 * File:                org.anon.smart.smcore.transition.graph.TransitionGraphs
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * cached graphs for transitions
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.graph;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.smart.deployment.ArtefactType;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.gconcurrent.Graph;
import org.anon.utilities.exception.CtxException;

public class TransitionGraphs
{
    private static Map<String, TransitionGraphs> TRANSITIONS = new ConcurrentHashMap<String, TransitionGraphs>(); //transitions grouped by prime and event

    private Map<String, Graph> _graphs;         //graphs for each set of transitions grouped by fromstate
    private long _lastUpdated;                  //last updated time, so can be changed if deployment has changed

    private TransitionGraphs(String flow, String prime, String event, String extras)
        throws CtxException
    {
        GraphCreator creator = new GraphCreator();
        _graphs = creator.graphFor(flow, prime, event, extras);
        _lastUpdated = System.nanoTime();
        //TODO: have to invalidate and recreate if deployments changed
    }

    public static Graph getGraph(String flow, String prime, String event, String extras, String fromstate)
        throws CtxException
    {
        String groupKey = ArtefactType.createKey(flow, prime, event);
        if ((extras != null) && (extras.length() > 0))
            groupKey = ArtefactType.createKey(flow, prime, event, extras);

        if (!TRANSITIONS.containsKey(groupKey))
        {
            TransitionGraphs graphs = new TransitionGraphs(flow, prime, event, extras);
            TRANSITIONS.put(groupKey, graphs);
        }

        TransitionGraphs graphs = TRANSITIONS.get(groupKey);
        assertion().assertNotNull(graphs, "No transitions found for state: " + prime + ":" + event + ":" + fromstate);
        System.out.println("Got graph: " + graphs._graphs);
        return graphs._graphs.get(fromstate);
    }

    public static void cleanup()
        throws CtxException
    {
        TRANSITIONS = null;
    }
}

