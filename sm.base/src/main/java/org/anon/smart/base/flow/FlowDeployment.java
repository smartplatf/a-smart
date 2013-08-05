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
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

import org.anon.smart.deployment.Artefact;
import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.deployment.Deployment;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class FlowDeployment extends Deployment implements FlowConstants
{
    private transient Map<Class<? extends Annotation>, List<String>> _mapTypes;

    private List<String> _internalData;
    private List<String> primeData;
    private List<String> data;
    private List<String> events;
    private List<String> responses;
    private List<String> messages;
    private List<String> transitions;
    private List<String> config;
    private List<Key> keys;
    private List<Link> links;
    private List<Link> needlinks;

    private Map<String, String> _classToName;
    private Map<String, String> _nameToClass;
    private Map<String, String> _keyMap;
    private Map<String, Link> _needLinks;

    public FlowDeployment()
    {
        super();
        initialize();
        _keyMap = new HashMap<String, String>();
        _needLinks = new HashMap<String, Link>();
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
        config = new ArrayList<String>();
        keys = new ArrayList<Key>();
        links = new ArrayList<Link>();
        needlinks = new ArrayList<Link>();
        _internalData = new ArrayList<String>();
        _classToName = new HashMap<String, String>();
        _nameToClass = new HashMap<String, String>();
        _keyMap = new HashMap<String, String>();
        _needLinks = new HashMap<String, Link>();
        initialize();
    }

    private void copy(List from, List to)
    {
        if ((from != null) && (to != null))
        {
            for (int i = 0; i < from.size(); i++)
                to.add(from.get(i));
        }
    }

    public FlowDeployment(FlowDeployment dep, String[] features)
        throws CtxException
    {
        super(dep, features);
        initialize();
        _classToName = new HashMap<String, String>();
        _nameToClass = new HashMap<String, String>();
        _keyMap = new HashMap<String, String>();
        _needLinks = new HashMap<String, Link>();
        for (String k : dep._keyMap.keySet())
            _keyMap.put(k, dep._keyMap.get(k));

        for (String k : dep._needLinks.keySet())
            _needLinks.put(k, new Link(dep._needLinks.get(k)));

        primeData = new ArrayList<String>();
        data = new ArrayList<String>();
        events = new ArrayList<String>();
        responses = new ArrayList<String>();
        messages = new ArrayList<String>();
        transitions = new ArrayList<String>();
        keys = new ArrayList<Key>();
        config = new ArrayList<String>();
        links = new ArrayList<Link>();
        needlinks = new ArrayList<Link>();

        copy(dep.primeData, primeData);
        copy(dep.data, data);
        copy(dep.events, events);
        copy(dep.responses, responses);
        copy(dep.transitions, transitions);
        copy(dep.messages, messages);
        copy(dep.keys, keys);
        copy(dep.links, links);
        copy(dep.config, config);
        copy(dep.needlinks, needlinks);

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
        //System.out.println("Found data: " + primeData + ":" + data + ":" + events + ":" + responses + ":" + transitions + ":" + links);
        _classToName = new HashMap<String, String>();
        _nameToClass = new HashMap<String, String>();
        _keyMap = new HashMap<String, String>();
        _needLinks = new HashMap<String, Link>();
        if (keys != null)
        {
            for (Key k : keys)
                _keyMap.put(k.getData(), k.getKey());
        }

        if (links != null)
        {
            for (Link l : links)
                l.setup(deployedName());
        }

        if (needlinks != null)
        {
            for (Link l : needlinks)
            {
                l.setup(deployedName());
                if (l.getName() != null)
                    _needLinks.put(l.getName(), l);
            }
        }
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
        _mapTypes.put(FlowService.configRecognizer(), config);
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
        ret = ret || ((config != null) && (config.contains(clsname)));

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

        if (config != null)
            ret.addAll(config);

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
        ret = ret || ((config != null) && config.contains(clsname));


        return ret;
    }

    public String getDataType(String clsname)
    {
        System.out.println("Config is: " + config + ":" + clsname);
        if ((primeData != null) && primeData.contains(clsname))
            return PRIMETYPE;
        else if ((data != null) && data.contains(clsname))
            return DATATYPE;
        else if ((events != null) && events.contains(clsname))
            return EVENTTYPE;
        else if ((responses != null) && responses.contains(clsname))
            return RESPONSETYPE;
        else if ((transitions != null) && transitions.contains(clsname))
            return TRANSITIONTYPE;
        else if ((messages != null) && messages.contains(clsname))
            return MESSAGETYPE;
        else if ((config != null) && config.contains(clsname))
            return CONFIGTYPE;

        return null;
    }

    public String[] getParmsFor(String dtype, String cls)
    {
        if (dtype.equals(PRIMETYPE) || dtype.equals(DATATYPE))
            return new String[] { _keyMap.get(cls) };

        return new String[0];
    }

    public List<String> getDeploymentFor(String dtype)
    {
        if (dtype.equals(PRIMETYPE))
            return primeData;
        else if (dtype.equals(DATATYPE))
            return data;
        else if (dtype.equals(EVENTTYPE))
            return events;
        else if (dtype.equals(RESPONSETYPE))
            return responses;
        else if (dtype.equals(TRANSITIONTYPE))
            return transitions;
        else if (dtype.equals(MESSAGETYPE))
            return messages;
        else if (dtype.equals(CONFIGTYPE))
            return config;

        return new ArrayList<String>();
    }

    public List<Link> getLinks() { return links; }

    public List<Link> linksFor(String flow, String name) 
    {
        List<Link> lnks = new ArrayList<Link>();
        for (Link l : links)
        {
            if (l.linkFor(flow, name))
                lnks.add(l);
        }

        return lnks;
    }

    public List<Link> toLinksFor(String flow, String name)
    {
        List<Link> lnks = new ArrayList<Link>();
        for (Link l : links)
        {
            if (l.getFromObject().linkFor(flow, name))
                lnks.add(l);
        }

        return lnks;
    }

    public void setupLinkFor(String name, String to)
        throws CtxException
    {
        if (_needLinks.containsKey(name))
        {
            Link lnk = _needLinks.get(name);
            _needLinks.remove(name);
            lnk.setTo(to, deployedName(), true);
            links.add(lnk); //add it to the links so we can use it.
        }
        else
        {
            except().te(this, "Cannot add a link for: " + name  + ":" + to + " Does not require a link.");
        }
    }

    public int getNeedLinks() 
    { 
        if (_needLinks != null)
            return _needLinks.size(); 

        return 0;
    }

    public Set<String> getNeedLinkNames()
    {
        if (_needLinks != null)
            return _needLinks.keySet();

        return null;
    }
}

