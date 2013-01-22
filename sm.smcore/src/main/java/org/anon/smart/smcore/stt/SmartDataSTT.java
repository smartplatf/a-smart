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

import org.anon.smart.base.stt.annot.MethodExit;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.annot.KeyAnnotate;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.DataLegend;

import org.anon.utilities.fsm.StateEntity;
import org.anon.utilities.fsm.FiniteState;
import org.anon.utilities.exception.CtxException;

public class SmartDataSTT implements SmartData, DSpaceObject
{
    private FiniteState ___smart_currentState___;
    private DataLegend ___smart_legend___;
    private transient String ___smart_name___;

    public SmartDataSTT()
    {
    }

    @MethodExit("constructor")
    private void smartdatastt___init()
        throws CtxException
    {
        ___smart_legend___ = new DataLegend();
        ___smart_name___ = objectName(this);
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
        KeyAnnotate kannot = (KeyAnnotate)reflect().getAnyAnnotation(this.getClass(), KeyAnnotate.class.getName());
        assertion().assertNotNull(kannot, "No keys defined for SmartData. Error.");
        assertion().assertNotNull(kannot.keys(), "No keys defined for SmartData. Error.");
        assertion().assertTrue((kannot.keys().length() > 0), "No keys defined for SmartData. Error.");
        List<Object> ret = new ArrayList<Object>();
        ret.add(smart___id());
        String[] flds = value().listAsString(kannot.keys());
        for (int i = 0; i < flds.length; i++)
        {
            Object val = reflect().getAnyFieldValue(this.getClass(), this, flds[i]);
            assertion().assertNotNull(val, "Key Value: " + flds[i] + " cannot be null. ");
            ret.add(val);
        }

        return ret;
    }

    public String smart___objectGroup()
        throws CtxException
    {
        return ___smart_name___;
    }
}

