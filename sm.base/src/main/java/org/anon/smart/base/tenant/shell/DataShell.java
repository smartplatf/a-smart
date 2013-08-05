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
 * File:                org.anon.smart.base.tenant.shell.DataShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A shell within which the tenant data exists
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.anon.smart.base.dspace.DSpaceService.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.smart.base.dspace.DSpace;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.dspace.TransactDSpace;
import org.anon.smart.base.dspace.SpaceFilter;
import org.anon.smart.base.monitor.MonitorableObject;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.d2cache.BrowsableReader;

import org.anon.utilities.exception.CtxException;

public class DataShell implements SmartShell, TenantConstants
{
    private transient Map<String, SpaceModel> _models;
    private transient Map<String, DSpace> _spaces;
    private transient List<String> _indexedSpaces; //this will not allow concurrency, but we shd synchronize enabling of flows
    private transient ShellContext _context;
    private transient int _startAperture;
    private transient SpaceFilter[] _spaceFilters;

    public DataShell(int start)
        throws CtxException
    {
        initializeShell();
        _startAperture = start;
        _spaceFilters = spaceFilters();
    }

    public int addStandardSpaces()
        throws CtxException
    {
        int count = 0;
        /*addStandardModel(USERS_SPACE, false);
        count++;
        addStandardModel(SESSIONS_SPACE, true);
        count++;
        addStandardModel(ROLES_SPACE, false);
        count++;*/
        addStandardModel(CONFIG_SPACE, false);
        count++;
        addStandardModel(MONITOR_SPACE, false);
        count++;
        return count;
    }

    public int addWorkingSpaces()
        throws CtxException
    {
        int count = 0;
        addStandardModel(WORKING_SPACE, false);
        count++;

        return count;
    }

    private void addStandardModel(String name, boolean browsable)
        throws CtxException
    {
        StandardSpaceModel model = new StandardSpaceModel(name, browsable);
        addSpaceFor(model);
    }

    public void initializeShell()
        throws CtxException
    {
        _context = new ShellContext();
        _spaces = new ConcurrentHashMap<String, DSpace>();
        _models = new ConcurrentHashMap<String, SpaceModel>();
        _indexedSpaces = new ArrayList<String>();
    }

    public Object addSpace(Object model)
        throws CtxException
    {
        assertion().assertTrue((model instanceof SpaceModel), "Cannot add space for a model that is not a spacemodel: " + model + ":" + model.getClass().getClassLoader() + ":" + this.getClass().getClassLoader() + ":" + _context.name());
        return addSpaceFor((SpaceModel)model);
    }

    private String spaceNameFor(String name)
    {
        return _context.name() + "-" + name;
    }

    public DSpace addSpaceFor(SpaceModel model)
        throws CtxException
    {
        String sname = spaceNameFor(model.name());
        DSpace space = spaceFor(sname, model.browsable());
        _spaces.put(model.name(), space);
        _indexedSpaces.add(model.name());
        int aper = _indexedSpaces.size() + _startAperture;
        model.setAperture(aper);
        _models.put(model.name(), model);
        return space;
    }

    public void cleanup()
        throws CtxException
    {
        for (DSpace s : _spaces.values())
        {
            if (!isBrowsable(s))
                continue; 

            BrowsableReader rdr = browsableReaderFor(s);
            Map<String, Set<Object>> keys = rdr.currentKeySet();
            for (String g : keys.keySet())
            {
                Set gkeys = keys.get(g);
                for (Object k : gkeys)
                {
                    Object val = rdr.lookup(g, k);
                    if (val instanceof MonitorableObject)
                        ((MonitorableObject)val).cleanup();
                }
            }
        }
        for (DSpace s : _spaces.values())
            s.cleanup();
    }

    public Object lookup(String spacemodel, String group, Object key)
        throws CtxException
    {
        //DSpace space = _spaces.get(spacemodel);
        DSpace space = getSpaceFor(spacemodel, SpaceFilter.spaceaction.read, true);
        assertion().assertNotNull(space, "Space is NULL for:"+spacemodel);
        Object ret = lookupIn(space, key, group, false);
        return ret;
    }
    
    public boolean exists(String spacemodel, String group, Object key)
            throws CtxException
        {
            //DSpace space = _spaces.get(spacemodel);
            DSpace space = getSpaceFor(spacemodel, SpaceFilter.spaceaction.read, true);
            assertion().assertNotNull(space, "Space is NULL for:"+spacemodel);
            boolean res = existsObject(space, key, group, false);
            return res;
        }

    public List<Object> search(String spacemodel, Class clz, Map<String, String> query)
        throws CtxException
    {
        //DSpace space = _spaces.get(spacemodel);
        DSpace space = getSpaceFor(spacemodel, SpaceFilter.spaceaction.read, true);
        List<Object> ret = searchIn(space, query, clz);
        return ret;
    }

    public List<Object> listAll(String spacemodel, String group, int size)
    	throws CtxException
    {
    	//DSpace space = _spaces.get(spacemodel);
        DSpace space = getSpaceFor(spacemodel, SpaceFilter.spaceaction.read, true);
    	assertion().assertNotNull(space, "ListAll:Space is NULL for:"+spacemodel);
        List<Object> ret = listAllIn(space, group, size);
        return ret;
    }
    void commitTo(String spacemodel, DSpaceObject[] sobjs)
        throws CtxException
    {
        //TransactDSpace space = (TransactDSpace)_spaces.get(spacemodel);
        TransactDSpace space = (TransactDSpace)getSpaceFor(spacemodel, SpaceFilter.spaceaction.read, true);
        assertion().assertNotNull(space, "Cannot find transact space for: " + spacemodel);
        addToSpace(space, sobjs);
    }

    private DSpace getSpaceFor(String spacemodel, SpaceFilter.spaceaction action, boolean except)
        throws CtxException
    {
        DSpace space = _spaces.get(spacemodel);    	
        for (int i = 0; (space != null) && (_spaceFilters != null) && (i < _spaceFilters.length); i++)
        {
            if (!_spaceFilters[i].filter(space, action, except))
                space = null;
        }

        return space;
    }

    public TransactDSpace getSpaceFor(String spacemodel)
        throws CtxException
    {
        return (TransactDSpace)getSpaceFor(spacemodel, SpaceFilter.spaceaction.write, false);
    }
}

