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
 * File:                org.anon.smart.atomicity.AbstractHypothesis
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An abstract implementation of the Hypothesis
 *
 * ************************************************************
 * */

package org.anon.smart.atomicity;

import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.utilities.exception.CtxException;

public abstract class AbstractHypothesis implements Hypothesis
{
    private final static String SEPARATOR = ";__;__;";
    protected UUID _id;
    protected Map<TruthData, EmpiricalData> _collected;
    protected Map<TruthData, EmpiricalData> _errorCollected;
    protected Map<TruthData, String> _taggedCollection;

    public AbstractHypothesis(UUID id)
    {
        _id = id;
        _collected = new ConcurrentHashMap<TruthData, EmpiricalData>();
        _errorCollected = new ConcurrentHashMap<TruthData, EmpiricalData>();
        _taggedCollection = new ConcurrentHashMap<TruthData, String>();
    }

    public EmpiricalData dataFor(TruthData truth)
        throws CtxException
    {
        return _collected.get(truth);
    }

    private String createKey(List<String> tags)
    {
        String ret = "";
        for (String tag : tags)
            ret += SEPARATOR + tag + SEPARATOR;

        return ret;
    }

    private EmpiricalData addTo(TruthData tdata, EmpiricalData edata, Map<TruthData, EmpiricalData> data)
        throws CtxException
    {
        EmpiricalData ret = data.get(tdata);
        if (ret == null)
        {
            ret = edata;
            tdata.recordEmpiricalData(_id, edata);
            data.put(tdata, edata);
            if (edata.tags() != null)
            {
                List<String> tags = edata.tags();
                _taggedCollection.put(tdata, createKey(tags));
            }
        }

        return ret;
    }

    public EmpiricalData collect(EmpiricalData edata)
        throws CtxException
    {
        TruthData td = edata.truth();
        if (td == null)
        {
            //this is a new data that has to become truth
            edata.setNew();
            //assumption is that the empiricaldata will provide an empty
            //truth data here for new data once the flag is set.
            td = edata.truth();
        }

        EmpiricalData ret = null;
        if (!edata.isErrorData())
            ret = addTo(td, edata, _collected);
        else
            ret = addTo(td, edata, _errorCollected);

        return ret;
    }

    public EmpiricalData collectError(EmpiricalData edata)
        throws CtxException
    {
        TruthData td = edata.truth();
        if (td == null)
        {
            //this is a new data that has to become truth
            edata.setNew();
            //assumption is that the empiricaldata will provide an empty
            //truth data here for new data once the flag is set.
            td = edata.truth();
        }

        EmpiricalData present = _collected.get(td);
        if (present != null)
            _collected.remove(td);

        EmpiricalData ret = addTo(td, edata, _errorCollected);
        return ret;
    }

    public List<EmpiricalData> empiricalDataFor(String tag)
        throws CtxException
    {
        return searchEmpiricalData(new String[] { tag });
    }

    private String getSearchString(String[] regextag)
    {
        final String allowedChars = "[a-zA-Z0-9]";
        String ret = ".*"; //match anything in the beginning
        for (int i = 0; i < regextag.length; i++)
        {
            String onereg = regextag[i];
            if (onereg.length() > 0)
            {
                onereg = onereg.replaceAll("\\*", allowedChars + "*");
                onereg = onereg.replaceAll("\\?", allowedChars + "?");
                onereg = onereg.replaceAll("\\+", allowedChars + "+");
                ret += SEPARATOR + onereg + SEPARATOR;
            }
        }
        ret += ".*"; //match anything at the end
        return ret;
    }

    public List<EmpiricalData> searchEmpiricalData(String[] regextag)
        throws CtxException
    {
        List<EmpiricalData> ret = new ArrayList<EmpiricalData>();
        String srch = getSearchString(regextag);
        Pattern p = Pattern.compile(srch);
        for (TruthData td : _taggedCollection.keySet())
        {
            Matcher m = p.matcher(_taggedCollection.get(td));
            if (m.matches())
                ret.add(_collected.get(td));
        }
        return ret;
    }

    protected Map<TruthData, EmpiricalData> workWith(boolean outcome)
    {
        if (outcome)
            return _collected;
        else
            return _errorCollected;
    }

    public boolean startTxn(UUID txnid)
        throws CtxException
    {
        boolean ret = true;
        Map<TruthData, EmpiricalData> workwith = workWith(true);
        for (TruthData td : workwith.keySet())
            ret = ret && td.start(txnid); //if one fails all others needs not start, hence do this.

        workwith = workWith(false);
        for (TruthData td : workwith.keySet())
            ret = ret && td.start(txnid); //if one fails all others needs not start, hence do this.

        return ret;
    }

    public boolean endTxn(UUID txnid)
        throws CtxException
    {
        boolean ret = true;

        Map<TruthData, EmpiricalData> workwith = workWith(true);
        for (TruthData td : workwith.keySet())
            ret = td.end(txnid) && ret; //need to end for all, hence do this.

        workwith = workWith(false);
        for (TruthData td : workwith.keySet())
            ret = td.end(txnid) && ret; //need to end for all, hence do this. Need to end even for errors

        return ret;
    }

    public boolean simulate(UUID txnid)
        throws CtxException
    {
        //simulate for only those in collected
        boolean ret = true;
        Map<TruthData, EmpiricalData> workwith = workWith(true);
        for (TruthData td : workwith.keySet())
        {
            EmpiricalData ed = workwith.get(td);
            ret = ret && td.simulate(txnid, ed); //one error and it will stop
        }

        return ret;
    }

    public void accept(UUID txnid)
        throws CtxException
    {
        //accept only those that are in collected
        Map<TruthData, EmpiricalData> workwith = workWith(true);
        for (TruthData td : workwith.keySet())
        {
            EmpiricalData ed = workwith.get(td);
            td.accept(txnid, ed);
        }
    }

    public void discard(UUID txnid)
        throws CtxException
    {
        //discard all that are in both errors and collected
        Map<TruthData, EmpiricalData> workwith = workWith(true);
        for (TruthData td : workwith.keySet())
        {
            EmpiricalData ed = workwith.get(td);
            td.discard(txnid, ed);
        }

        workwith = workWith(false);
        for (TruthData td : workwith.keySet())
        {
            EmpiricalData ed = workwith.get(td);
            td.discard(txnid, ed);
        }
    }
}

