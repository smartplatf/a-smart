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
 * File:                org.anon.smart.base.tenant.shell.RuntimeShell
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A shell within which tenant flows execute
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant.shell;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.anon.smart.base.dspace.TransactDSpace;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.annot.StatesAnnotate;
import org.anon.smart.base.annot.StateAnnotate;
import org.anon.smart.base.flow.FlowService;
import org.anon.smart.base.flow.FlowModel;
import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantConstants;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.pool.Pool;
import org.anon.utilities.pool.PoolEntity;
import org.anon.utilities.fsm.FiniteStateMachine;
import org.anon.utilities.exception.CtxException;

public class RuntimeShell implements SmartShell, TenantConstants
{
    private transient ShellContext _context;
    private transient Map<Class, Pool> _transitionPool;
    private ExecutorService _transitionExecutor;

    public RuntimeShell()
        throws CtxException
    {
        initializeShell();
    }

    public void initializeShell()
        throws CtxException
    {
        _context = new ShellContext();
        _transitionPool = new ConcurrentHashMap<Class, Pool>();
        _transitionExecutor = anatomy().jvmEnv().executorFor("Transition", _context.name());
    }

    public void cleanup()
        throws CtxException
    {
    }

    public Object lookupFor(String spacemodel, String group, Object key)
        throws CtxException
    {
        //no need to search different spaces to find out which space has it
        //it is searched directly on the shell that has to have it. IF not 
        //present, then it is not accessible or not present.
        DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
        Object ret = shell.lookup(spacemodel, group, key);
        return ret;
    }
    
    public boolean exists(String spacemodel, String group, Object key)
            throws CtxException
        {
            //no need to search different spaces to find out which space has it
            //it is searched directly on the shell that has to have it. IF not 
            //present, then it is not accessible or not present.
            DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
            boolean res = shell.exists(spacemodel, group, key);
            return res;
        }
    
    public Object lookupConfigFor(String group, Object key)
        throws CtxException
    {
        return lookupFor(CONFIG_SPACE, group, key);
    }

    public List<Object> searchFor(String spacemodel, Class clz, Map<String, String> query)
        throws CtxException
    {
        DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
        return shell.search(spacemodel, clz, query);
    }
    
    public List<Object> listAll(String spacemodel, String group, int size)
    	throws CtxException
    {
    	DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
        return shell.listAll(spacemodel, group, size);
    }

    public TransactDSpace getSpaceFor(String spacemodel)
        throws CtxException
    {
        DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
        return shell.getSpaceFor(spacemodel);
    }

    public void commitToSpace(String spacemodel, DSpaceObject[] objects)
        throws CtxException
    {
        //Please DO NOT USE THIS ANYWHERE EXCEPT FOR TESTING
        DataShell shell = (DataShell)_context.tenant().dataShellFor(spacemodel);
        shell.commitTo(spacemodel, objects);
    }

    public void commitInternalObjects(String spacemodel, Object[] objects)
        throws CtxException
    {
        //again is used only in tenant setup, nowhere else
        DSpaceObject[] sobjs = new DSpaceObject[objects.length];
        for (int i = 0; i < objects.length; i++)
        {
            sobjs[i] = (DSpaceObject)objects[i];
        }
        commitToSpace(spacemodel, sobjs);
    }

    public <T extends PoolEntity> T getTransition(Class<T> cls)
        throws CtxException
    {
        Pool tpool = _transitionPool.get(cls);
        if (tpool == null)
        {
            tpool = (Pool)pool().createPool(cls);
            _transitionPool.put(cls, tpool);
        }

        return cls.cast(tpool.lockone());
    }

    public void releaseTransition(PoolEntity transition)
        throws CtxException
    {
        assertion().assertNotNull(transition, "Error cannot release null objects into pool");
        Pool tpool = _transitionPool.get(transition.getClass());
        tpool.unlockone(transition);
    }

    public ExecutorService transitionExecutor()
    {
        return _transitionExecutor;
    }

    public void enabledFlowClazzez(Object model, Class[] clazzez)
        throws CtxException
    {
        for (int i = 0; i < clazzez.length; i++)
        {
            if (FlowService.isData(clazzez[i]))
                createFSM(clazzez[i]);
        }

        //create a flow admin
        FlowModel m = (FlowModel)model;
        String flow = m.name();
        Object admin = new FlowAdmin(flow);
        if (admin instanceof DSpaceObject) //will only do if it has been stereotyped?
        {
            //System.out.println("Creating flow for: " + flow + ":" + ((DSpaceObject)admin).smart___objectGroup());
            commitToSpace(flow, new DSpaceObject[] { (DSpaceObject)admin });
        }
    }

    public void createFSM(Class cls)
        throws CtxException
    {
        String name = className(cls);
        FiniteStateMachine mc = fsm().fsm(name);
        if (mc == null)
        {
            assertion().assertNotNull(name, "Cannot create finite state machine for non-hosted objects: " + cls.getName());
            assertion().assertTrue((name.length() > 0), "Cannot create finite state machine for non-hosted objects: " + cls.getName());
            StatesAnnotate states = (StatesAnnotate)reflect().getAnyAnnotation(cls, StatesAnnotate.class.getName());
            assertion().assertNotNull(states, "No states specified for the given class: ", cls.getName());
            StateAnnotate[] state = states.states();
            assertion().assertNotNull(state, "Not states specified for the given class: " + cls.getName());
            assertion().assertTrue((state.length > 0), "No states specified for the given class: " + cls.getName());
            String startstate = null;
            for (int i = 0; (startstate == null) && (i < state.length); i++)
            {
                //System.out.println(state[i]);
                if (state[i].startState())
                    startstate = state[i].name();
            }
            assertion().assertNotNull(startstate, "No start state specified for the class: " + cls.getName());
            mc = fsm().create(name, startstate);
            for (int i = 0; i < state.length; i++)
            {
                //System.out.println(state[i]);
                boolean start = state[i].startState();
                boolean end = state[i].endState();
                if ((!start) && (!end))
                    mc.addState(state[i].name());
                else if (end)
                    mc.addEndState(state[i].name());
            }
            //TODO: create parent and child transition trees
        }
    }

    public static RuntimeShell currentRuntimeShell()
        throws CtxException
    {
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        return (RuntimeShell)tenant.runtimeShell();
    }
}

