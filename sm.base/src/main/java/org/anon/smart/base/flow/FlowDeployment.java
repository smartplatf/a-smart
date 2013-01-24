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
 * File:                org.anon.smart.base.flow.FlowDeployment
 * Author:              rsankar
 * Revision:            1.0
 * Date:                13-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A deployment for flows
 *
 * ************************************************************
 * */

package org.anon.smart.base.flow;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

import org.anon.smart.deployment.Artefact;
import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.deployment.Deployment;

import org.anon.utilities.exception.CtxException;

public class FlowDeployment extends Deployment
{
    private transient Map<Class<? extends Annotation>, List<String>> _mapTypes;

    private List<String> primeData;
    private List<String> data;
    private List<String> events;
    private List<String> responses;
    private List<String> messages;
    private List<String> transitions;

    private Map<String, String> _classToName;
    private Map<String, String> _nameToClass;

    public FlowDeployment()
    {
        super();
        initialize();
    }

    public FlowDeployment(String nm, Artefact[] a)
    {
        super(nm, a);
        primeData = new ArrayList<String>();
        data = new ArrayList<String>();
        events = new ArrayList<String>();
        responses = new ArrayList<String>();
        messages = new ArrayList<String>();
        transitions = new ArrayList<String>();
        _classToName = new HashMap<String, String>();
        _nameToClass = new HashMap<String, String>();
        initialize();
    }

    public FlowDeployment(FlowDeployment dep, String[] features)
        throws CtxException
    {
        super(dep, features);
        initialize();
        _classToName = new HashMap<String, String>();
        _nameToClass = new HashMap<String, String>();
        primeData = dep.primeData;
        for (String p : primeData)
        {
            String n = dep._classToName.get(p);
            _classToName.put(p, n);
            _nameToClass.put(n, p);
        }
    }

    @Override
    public <T extends Deployment> T createDefault(String[] features, Class<T> cls)
        throws CtxException
    {
        return cls.cast(new FlowDeployment(this, features));
    }

    @Override
    protected void setup()
        throws CtxException
    {
        super.setup();
        initialize();
        _classToName = new HashMap<String, String>();
        _nameToClass = new HashMap<String, String>();
    }

    private void initialize()
    {
        _mapTypes = new HashMap<Class<? extends Annotation>, List<String>>();
        _mapTypes.put(FlowService.primeDataRecognizer(), primeData);
        _mapTypes.put(FlowService.dataRecognizer(), data);
        _mapTypes.put(FlowService.eventRecognizer(), events);
        _mapTypes.put(FlowService.responseRecognizer(), responses);
        _mapTypes.put(FlowService.messageRecognizer(), messages);
        _mapTypes.put(FlowService.transitionRecognizer(), transitions);
    }

    @Override
    public boolean belongsToMe(Class cls)
    {
        String clsname = cls.getName();
        boolean ret = primeData.contains(clsname);
        ret = ret || ((data != null) && data.contains(clsname));
        ret = ret || ((events != null) && events.contains(clsname));
        ret = ret || ((responses != null) && responses.contains(clsname));
        ret = ret || ((messages != null) && messages.contains(clsname));
        ret = ret || ((transitions != null) && transitions.contains(clsname));

        return ret;
    }

    @Override
    public void addArtefacts(Artefact[] artefacts)
    {
        for (int i = 0; i < artefacts.length; i++)
        {
            ArtefactType t = artefacts[i].getType();
            String nm = artefacts[i].getClazz().getName();
            String anm = artefacts[i].getName();
            List<String> lst = _mapTypes.get(t.getRecognizer());
            if ((lst != null) && (!lst.contains(nm)))
                lst.add(nm);
            _classToName.put(nm, anm);
            _nameToClass.put(anm, nm);
        }
    }

    @Override
    public List<String> artefacts()
    {
        List<String> ret = new ArrayList<String>();
        ret.addAll(primeData);
        if (data != null)
            ret.addAll(data);

        if (events != null)
            ret.addAll(events);

        if (responses != null)
            ret.addAll(responses);

        if (messages != null)
            ret.addAll(messages);

        if (transitions != null)
            ret.addAll(transitions);

        return ret;
    }

    public Object model(ClassLoader ldr) 
        throws CtxException
    { 
        CrossLinkFlowModel m = new CrossLinkFlowModel(deployedName(), ldr);
        return m.link(); 
    }

    public List<String> getPrimeData() { return primeData; }
    public String nameFor(String cls)
    {
        return _classToName.get(cls);
    }

    public String classFor(String name)
    {
        return _nameToClass.get(name);
    }

    public boolean belongs(String clsname)
    {
        boolean ret = primeData.contains(clsname);
        ret = ret || ((data != null) && data.contains(clsname));
        ret = ret || ((events != null) && events.contains(clsname));
        ret = ret || ((responses != null) && responses.contains(clsname));
        ret = ret || ((messages != null) && messages.contains(clsname));
        ret = ret || ((transitions != null) && transitions.contains(clsname));

        return ret;
    }
}

