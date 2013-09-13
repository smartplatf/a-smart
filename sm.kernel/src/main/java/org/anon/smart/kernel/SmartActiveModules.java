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
 * File:                org.anon.smart.kernel.SmartActiveModules
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of modules that are used in the kernel
 *
 * ************************************************************
 * */

package org.anon.smart.kernel;

import java.net.URL;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.kernel.config.SmartConfig;
import org.anon.smart.kernel.config.InstallConfig;
import org.anon.smart.kernel.config.EnableFlags;
import org.anon.smart.kernel.config.ModuleConfig;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SmartActiveModules
{
    private static final String PROTOCOL = "file://";

    private String _installDir;
    private String _installVersion;
    private Map<String, ModuleConfig> _modules;
    private List<URL> _urls;
    private String _classloader;
    private String[] _loadModules;
    private String[] _startModules;

    public SmartActiveModules(SmartConfig config)
        throws CtxException
    {
        InstallConfig install = config.getInstall();
        _installDir = install.getPath();
        _installVersion = install.getVersion();
        EnableFlags flags = config.getFlags();

        _modules = new HashMap<String, ModuleConfig>();
        _urls = new ArrayList<URL>();

        List<ModuleConfig> modules = config.getModules();
        int currload = Integer.MAX_VALUE;
        _loadModules = new String[modules.size()];
        _startModules = new String[modules.size()];
        for (ModuleConfig module : modules)
        {
            boolean enable = ((module.getEnableFlag() == null) || (module.getEnableFlag().length() <= 0));
            enable = enable || (flags.getEnabled(module.getEnableFlag()));
            if (enable) 
            {
                _modules.put(module.getName(), module);
                String[] jars = module.getSmartJars();
                for (int i = 0; i < jars.length; i++)
                    _urls.add(getSmartJar(jars[i]));

                String[] depjars = module.getDependantJars();
                for (int i = 0; i < depjars.length; i++)
                    _urls.add(getDependantJar(depjars[i]));

                assertion().assertTrue((module.getLoadOrder() < _loadModules.length), "Not a valid load order for module");

                if ((module.getLoadOrder() < currload) && (module.getClassLoader() != null))
                {
                    _classloader = module.getClassLoader();
                    currload = module.getLoadOrder();
                }

                //loadorder should be 0 index number
                _loadModules[module.getLoadOrder()] = module.getModule();
                _startModules[module.getStartOrder()] = module.getModule();
            }
        }
        List<String> comps = new ArrayList<String>();
        List<String> start = new ArrayList<String>();
        for (int i = 0; i < _loadModules.length; i++)
        {
            if ((_loadModules[i] != null) && (_loadModules[i].length() > 0))
                comps.add(_loadModules[i]);

            if ((_startModules[i] != null) && (_startModules[i].length() > 0))
                start.add(_startModules[i]);
        }
        _loadModules = comps.toArray(new String[0]);
        _startModules = start.toArray(new String[0]);

        assertion().assertNotNull(_classloader, "Cannot find a valid classloader to use in the modules.");
    }

    private URL getSmartJar(String mod)
        throws CtxException
    {
        String[] desc = mod.split(";");
        assertion().assertTrue((desc.length >= 2), "The module configuration is not correct. " + mod);
        String group = "/org/anon/" + desc[0] + "/";
        String jar = _installDir + group + desc[1] + "/" + _installVersion + "/" + desc[1] + "-" + _installVersion + ".jar";
        try
        {
            URL ret = new URL(PROTOCOL + jar);
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().rt(e, new CtxException.Context("Error creating URL: " + mod, "Exception"));
        }
        return null;
    }

    private URL getDependantJar(String depjar)
        throws CtxException
    {
        String jar = _installDir + depjar;
        try
        {
            URL ret = new URL(PROTOCOL + jar);
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().rt(e, new CtxException.Context("Error creating URL: " + depjar, "Exception"));
        }
        return null;
    }

    public String[] startOrder()
    {
        return _startModules;
    }

    public ClassLoader activeLoader()
        throws CtxException
    {
        try
        {
            Class cls = Class.forName(_classloader);
            ClassLoader ldr = (ClassLoader)cls.getDeclaredConstructor(URL[].class, String[].class).newInstance(
                                                     _urls.toArray(new URL[0]), _loadModules);
            return ldr;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            except().rt(e, new CtxException.Context("Error creating classloader: " + _classloader, "Exception"));
        }

        return this.getClass().getClassLoader();
    }
}

