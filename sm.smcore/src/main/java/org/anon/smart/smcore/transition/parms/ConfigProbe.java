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
 * File:                org.anon.smart.smcore.transition.parms.ConfigProbe
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A probe that retrieves config object and passes as parameter
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.parms;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.anon.smart.base.utils.AnnotationUtils;
import org.anon.smart.smcore.data.ConfigData;
import org.anon.smart.smcore.config.ConfigService;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.gconcurrent.execute.PProbe;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.PDescriptor;
import org.anon.utilities.exception.CtxException;

public class ConfigProbe implements PProbe
{
    public ConfigProbe()
    {
        super();
    }

    public Object valueFor(Class cls, Type type, ProbeParms parms, PDescriptor desc)
        throws CtxException
    {
        Object ret = valueFor(cls, type, parms);
        if (ret != null)
        {
            String attribute = desc.attribute();
            if (attribute != null)
            {
                Field fld = reflect().getAnyField(ret.getClass(), attribute);
                if (fld != null)
                {
                    try
                    {
                        fld.setAccessible(true);
                        ret = fld.get(ret);
                    }
                    catch(Exception e)
                    {
                        except().rt(e, new CtxException.Context("ConfigProbe.valueFor" + attribute, "Exception"));
                    }
                }
                else
                    except().te(this, "Problem accessing attribute: " + attribute + ":" + ret);
            }
        }

        if ((ret != null) && type().isAssignable(ret.getClass(), cls))
            return ret;

        return null;
    }

    public Object valueFor(Class cls, Type type, ProbeParms parms)
        throws CtxException
    {
        if (!AnnotationUtils.isConfig(cls))
            return null;

        Object ret = null;
        Class<? extends ConfigData> ccls = (Class<? extends ConfigData>)cls;
        TransitionProbeParms tparms = (TransitionProbeParms)parms;
        ret = ConfigService.configFor(tparms.primeData(), ccls);
        if ((ret != null) && type().isAssignable(ret.getClass(), cls))
            return ret;

        return null;
    }

    public Object valueFor(ProbeParms parms, Type type, PDescriptor desc)
        throws CtxException
    {
        return null;
    }

    public void releaseValues(Object[] val)
        throws CtxException
    {
    }
}

