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
 * File:                org.anon.smart.secure.dspace.SecureDataFilter
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A filter for securing data
 *
 * ************************************************************
 * */

package org.anon.smart.secure.dspace;

import java.util.List;

import org.anon.smart.d2cache.DataFilter;
import org.anon.smart.base.dspace.DSpace;
import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.inbuilt.data.SmartUser;
import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.secure.sdomain.SmartSecureData;
import org.anon.smart.base.dspace.FSMDataFilter;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SecureDataFilter extends FSMDataFilter
{
    public static final String SETTING_VISITOR = "visitorAccess";
    public static final String YES = "yes";
    public static final String NO = "no";

    private DSpace _space;

    public SecureDataFilter()
    {
    }

    public void setupSpace(DSpace space)
    {
        _space = space;
    }

    public boolean filterObject(Object obj, DataFilter.dataaction action, boolean except)
        throws CtxException
    {
        //if not accessible because of state, then just return false.
        boolean ret = super.filterObject(obj, action, except);
        if (!ret)
            return ret;
        System.out.println("Checking for access of: " + obj + ":" + action);
        //allow reading of session objects if the sessionid is present.
        if (obj instanceof Session)
            return true;

        String val = (String)threads().contextLocal(SETTING_VISITOR);
        if ((val != null) && (val.equals(YES)) && (obj instanceof SmartUser)) //even here only allow a set of objects
            return true; 

        if (obj instanceof SmartSecureData)
        {
            Access access = null;
            SmartSecureData data = (SmartSecureData)obj;
            if (action.equals(DataFilter.dataaction.read))
                access = Access.read;
            else if (action.equals(DataFilter.dataaction.create))
                access = Access.create;
            else
                access = Access.write;

            try
            {
                List<Object> lobj = data.___smart_checkPermission___(access);
                ret = true;
            }
            catch (Exception e)
            {
                ret = false;
                if (except)
                {
                    except().rt(e, new CtxException.Context("Security", "Cannot Access."));
                }
            }
        }

        return ret;
    }
}

