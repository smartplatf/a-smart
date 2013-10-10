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
 * File:                org.anon.smart.smcore.transition.plugin.PluginManager
 * Author:              rsankar
 * Revision:            1.0
 * Date:                05-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A manager for plugins
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.plugin;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.events.SmartEvent;

import org.anon.utilities.exception.CtxException;

public class PluginManager
{
    private static PluginManager PLUGINS = new PluginManager();

    public static void releasePlugins()
    {
        PLUGINS._plugins.clear();
        PLUGINS = null;
    }

    private List<TransitionPlugin> _plugins;

    private PluginManager()
    {
        _plugins = new ArrayList<TransitionPlugin>();
    }

    public static void registerPlugin(TransitionPlugin plugin)
    {
        PLUGINS._plugins.add(plugin);
    }

    public static void eventProcessed(SmartEvent evt)
        throws CtxException
    {
        System.out.println("Event processed.. " + evt + ":" + PLUGINS._plugins.size());
        for (int i = 0; i < PLUGINS._plugins.size(); i++)
            PLUGINS._plugins.get(i).eventProcessed(evt);
    }

    public static void objectCreated(SmartData obj)
        throws CtxException
    {
        System.out.println("Object Created.. " + obj + ":" + PLUGINS._plugins.size());
        for (int i = 0; i < PLUGINS._plugins.size(); i++)
            PLUGINS._plugins.get(i).objectCreated(obj);
    }

    public static void objectModified(SmartData obj)
        throws CtxException
    {
        System.out.println("Object Modified.. " + obj + ":" + PLUGINS._plugins.size());
        for (int i = 0; i < PLUGINS._plugins.size(); i++)
            PLUGINS._plugins.get(i).objectModified(obj);
    }

    public static void primeObjectCreated(SmartPrimeData obj)
        throws CtxException
    {
        System.out.println("Prome Object Created.. " + obj + ":" + PLUGINS._plugins.size());
        for (int i = 0; i < PLUGINS._plugins.size(); i++)
            PLUGINS._plugins.get(i).primeObjectCreated(obj);
    }

    public static void stateTransitioned(SmartData obj, String from, String to)
        throws CtxException
    {
        System.out.println("State Changed.. " + obj + ":" + PLUGINS._plugins.size());
        for (int i = 0; i < PLUGINS._plugins.size(); i++)
            PLUGINS._plugins.get(i).stateTransitioned(obj, from, to);
    }
}

