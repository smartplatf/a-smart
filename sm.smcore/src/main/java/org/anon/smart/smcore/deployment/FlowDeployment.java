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
 * File:                org.anon.smart.smcore.deployment.FlowDeployment
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

package org.anon.smart.smcore.deployment;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

import org.anon.smart.deployment.Artefact;
import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.deployment.Deployment;

public class FlowDeployment extends Deployment
{
    private transient Map<Class<? extends Annotation>, List<String>> _mapTypes;

    private String primeData;
    private List<String> data;
    private List<String> events;
    private List<String> responses;
    private List<String> messages;

    public FlowDeployment()
    {
        super();
        initialize();
    }

    public FlowDeployment(String nm, Artefact[] a)
    {
        super(nm, a);
        data = new ArrayList<String>();
        events = new ArrayList<String>();
        responses = new ArrayList<String>();
        messages = new ArrayList<String>();
        initialize();
    }

    private void initialize()
    {
        _mapTypes = new HashMap<Class<? extends Annotation>, List<String>>();
        _mapTypes.put(DeploymentService.dataRecognizer(), data);
        _mapTypes.put(DeploymentService.eventRecognizer(), events);
        _mapTypes.put(DeploymentService.responseRecognizer(), responses);
        _mapTypes.put(DeploymentService.messageRecognizer(), messages);
    }

    @Override
    public boolean belongsToMe(Class cls)
    {
        String clsname = cls.getName();
        boolean ret = primeData.equals(clsname);
        ret = ret || (data.contains(clsname));
        ret = ret || (events.contains(clsname));
        ret = ret || (responses.contains(clsname));
        ret = ret || (messages.contains(clsname));

        return ret;
    }

    @Override
    public void addArtefacts(Artefact[] artefacts)
    {
        for (int i = 0; i < artefacts.length; i++)
        {
            ArtefactType t = artefacts[i].getType();
            String nm = artefacts[i].getClazz().getName();
            List<String> lst = _mapTypes.get(t.getRecognizer());
            if ((lst != null) && (!lst.contains(nm)))
                lst.add(nm);
        }
    }

    @Override
    public List<String> artefacts()
    {
        List<String> ret = new ArrayList<String>();
        ret.addAll(data);
        ret.addAll(events);
        ret.addAll(responses);
        ret.addAll(messages);
        return ret;
    }

}

