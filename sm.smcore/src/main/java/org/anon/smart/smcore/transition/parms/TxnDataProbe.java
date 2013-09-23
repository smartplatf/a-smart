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
 * File:                org.anon.smart.smcore.transition.parms.TxnDataProbe
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-04-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a probe that retrieves txn related data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition.parms;

import java.util.List;
import java.lang.reflect.Type;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;

import java.util.List;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.gconcurrent.execute.PProbe;
import org.anon.utilities.gconcurrent.execute.ParamType;
import org.anon.utilities.gconcurrent.execute.PDescriptor;
import org.anon.utilities.gconcurrent.execute.ProbeParms;
import org.anon.utilities.gconcurrent.execute.AbstractProbe;
import org.anon.utilities.exception.CtxException;

public class TxnDataProbe extends AbstractProbe implements AtomicityConstants
{
    public TxnDataProbe()
    {
        super();
    }

    protected Object valueFor(ProbeParms parms)
        throws CtxException
    {
        return null; //this is an explicit param, and cannot be used with descriptor
    }

    private String evaluate(String[] lnks, ProbeParms parms, String attr)
        throws CtxException
    {
        ParamType t = ParamType.valueOf(lnks[0]);
        if (t != null)
        {
            String pstr = "(" + lnks[0] + "." + attr + ")";
            List<PDescriptor> desc = PDescriptor.parseParamDesc(pstr);
            PProbe p = t.myProbe();
            Object val = p.valueFor(parms, null, desc.get(0));
            System.out.println("Got the value for: " + pstr + ":" + p + ":" + val);
            if (val != null)
                return val.toString();
        }

        return null;
    }

    @Override
    public Object valueFor(ProbeParms parms, Type type, PDescriptor desc)
        throws CtxException
    {
        String attr = desc.attribute();
        boolean computeattr = false;
        if ((desc.links() != null) && (desc.links().length > 0))
        {
            //means we need to evaluate the tag
            ParamType t = ParamType.valueOf(desc.links()[0]);
            if (t != null)
            {
                attr = evaluate(desc.links(), parms, attr);
                if (attr == null)
                    return null;
            }
            else
            {
                attr = desc.links()[0];
                computeattr = true;
            }
        }
        assertion().assertNotNull(attr, "Cannot get parameter where attribute is null");
        TransitionContext ctx = (TransitionContext)parms.context();
        assertion().assertNotNull(ctx, "Cannot get parameter where context is null");
        //TODO: for now it is just smart data, shd we split?
        List<EmpiricalData> ret = ctx.atomicity().dataFor(SMARTDATA, attr);
        if ((ret !=  null) && (ret.size() > 0))
        {
            SmartDataED edata = (SmartDataED)ret.get(0);
            Object data = edata.empirical();
            if ((data != null) && (computeattr))
            {
                Object fval = reflect().getAnyFieldValue(data.getClass(), data, desc.attribute());
                return fval;
            }
            else
                return data;
        }

        return null;
    }

}

