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
 * File:                org.anon.smart.smcore.stt.SmartDataSTT
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An stt for data objects in smart
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.smart.base.monitor.MonitorableObject;
import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.annot.KeyAnnotate;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartDataTruth;
import org.anon.smart.smcore.data.DataLegend;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.anatomy.SMCoreContext;
import org.anon.smart.smcore.transition.TransitionContext;

import org.anon.utilities.fsm.StateEntity;
import org.anon.utilities.fsm.FiniteState;
import org.anon.utilities.fsm.FiniteStateMachine;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

public class SmartDataSTT implements SmartData, DSpaceObject, MonitorableObject
{
    private FiniteState ___smart_currentState___;
    private DataLegend ___smart_legend___;
    private String ___smart_name___;

    public SmartDataSTT()
    {
    }

    @MethodExit("constructor")
    private void smartdatastt___init()
        throws CtxException
    {
        ___smart_legend___ = new DataLegend();
        ___smart_name___ = objectName(this);
	    startFSM();
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        if (ctx != null)
            ctx.atomicity().includeNewData(this);
    }

    private void startFSM()
        throws CtxException
    {
        FiniteStateMachine mc = fsm().fsm(___smart_name___);
        assertion().assertNotNull(mc, "The finite state machine for " + ___smart_name___ + " has not been setup correctly.");
        mc.start(this);
    }

    public String smart___name()
        throws CtxException
    {
        if (___smart_name___ == null)
            ___smart_name___ = objectName(this);

        return ___smart_name___;
    }

    public UUID smart___id()
    {
        return ___smart_legend___.id();
    }

    public String smart___owner()
    {
        return ___smart_legend___.ownedBy();
    }

    public String smart___group()
    {
        return ___smart_legend___.group();
    }

    public String[] smart___tags()
        throws CtxException
    {
        List<String> tags = new ArrayList<String>();
        tags.add(___smart_name___);
        return tags.toArray(new String[0]);
    }

    public String utilities___stateEntityType()
    {
        return ___smart_name___;
    }

    public void utilities___setCurrentState(FiniteState state)
    {
        ___smart_currentState___ = state;
    }

    public FiniteState utilities___currentState()
    {
        return ___smart_currentState___;
    }

    public StateEntity utilities___parent()
        throws CtxException
    {
        //TODO:
        return null;
    }

    public StateEntity[] utilities___children(String setype)
        throws CtxException
    {
        //TODO:
        return null;
    }

    public List<Object> smart___keys()
        throws CtxException
    {
        Object[] keys = reflect().getAnnotatedFieldValues(this, KeyAnnotate.class);
        assertion().assertNotNull(keys, "No keys defined for SmartData. Error.");
        assertion().assertTrue((keys.length > 0), "No keys defined for SmartData. Error.");
        List<Object> ret = new ArrayList<Object>();
        ret.add(smart___id());
        for (int i = 0; i < keys.length; i++)
        {
            assertion().assertNotNull(keys[i], "Key Value:  cannot be null. ");
            ret.add(keys[i]);
        }

        return ret;
    }

    public String smart___objectGroup()
        throws CtxException
    {
    	return ___smart_name___;
    }

    public void smart___transition(String tostate)
        throws CtxException
    {
        FiniteStateMachine mc = fsm().fsm(___smart_name___);
        assertion().assertNotNull(mc, "Finite state machine for " + ___smart_name___ + " has not been setup correctly. Cannot transition.");
        mc.transition(this, tostate);
    }

    public SmartDataTruth smart___myTruth()
        throws CtxException
    {
        SMCoreContext ctx = SMCoreContext.coreContext();
        SmartDataTruth truth = ctx.getTruthFor(this);
        return truth;
    }

	@Override
	public void smart___initOnLoad() throws CtxException {
		startFSM();
		if(this instanceof SmartPrimeData)
			((SmartPrimeData)this).initPrimeObject();
		
	}

	@Override
	public void cleanup() throws CtxException {
		// TODO Auto-generated method stub
		
	}
}

