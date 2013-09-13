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
 * File:                org.anon.smart.smcore.data.SmartDataED
 * Author:              rsankar
 * Revision:            1.0
 * Date:                27-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An empirical data for smart data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.data;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.atomicity.TruthData;
import org.anon.smart.smcore.transition.atomicity.AtomicityConstants;

import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SmartDataED implements EmpiricalData, AtomicityConstants
{
    private SmartDataTruth _truth;
    private SmartData _empirical;
    private SmartData _original;
    

	private boolean _isNew;

    public SmartDataED(SmartDataTruth data)
        throws CtxException
    {
        _truth = data;
        _empirical = (SmartData)serial().clone(data.smartData());
        _original = (SmartData)serial().clone(_empirical);
        _isNew = false;
        data.smartData().smart___setIsNew(_isNew);
    }

    public SmartDataED(SmartData data)
        throws CtxException
    {
        //this is the new object
        _empirical = data;
        _original = data;
        _isNew = true;
        data.smart___setIsNew(_isNew);
    }

    public void setNew()
    {
        _isNew = true;
        _empirical.smart___setIsNew(_isNew);
    }

    public TruthData truth()
        throws CtxException
    {
        if (_isNew)
            _truth = new SmartDataTruth(_empirical);

        return _truth;
    }

    public boolean isErrorData()
    {
        return false;
    }

    public String dataType()
        throws CtxException
    {
        return SMARTDATA;
    }

    public List<String> tags()
        throws CtxException
    {
        String[] tags = _empirical.smart___tags();
        List<String> ret = new ArrayList<String>();
        for (int i = 0; (tags != null) && (i < tags.length); i++)
            ret.add(tags[i]);
        return ret;
    }

    public SmartData empirical() { return _empirical; }
    public SmartData original() { return _original; }
		
}

