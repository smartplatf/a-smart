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
 * File:                org.anon.smart.atomicity.Atomicity
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An atomic operation provided for all included hypothesis
 *
 * ************************************************************
 * */

package org.anon.smart.atomicity;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class Atomicity
{
    private UUID _atomicID;
    private Map<String, Hypothesis> _hypothesis;
    private List<String> _orderedKeys;

    public Atomicity()
    {
        _atomicID = UUID.randomUUID();
        _hypothesis = new ConcurrentHashMap<String, Hypothesis>();
        _orderedKeys = new ArrayList<String>();
    }

    protected void addDataType(String dt)
    {
        //call it in the order in which it shd be accepted
        _orderedKeys.add(dt);
    }

    public EmpiricalData recordException(EmpiricalData edata)
        throws CtxException
    {
        EmpiricalData eret = edata;
        String dt = edata.dataType();
        Hypothesis hypo = _hypothesis.get(dt);
        if (hypo == null)
            hypo = new DeductiveHypothesis(_atomicID, edata, true);
        else
            eret = hypo.collectError(edata);

        _hypothesis.put(dt, hypo);
        return eret;
    }

    public EmpiricalData includeEmpiricalData(EmpiricalData edata)
        throws CtxException
    {
        EmpiricalData eret = edata;
        String dt = edata.dataType();
        Hypothesis hypo = _hypothesis.get(dt);    
        if (hypo == null)
            hypo = new DeductiveHypothesis(_atomicID, edata);
        else
            eret = hypo.collect(edata);

        _hypothesis.put(dt, hypo);
        return eret;
    }

    public EmpiricalData dataFor(String dt, TruthData truth)
        throws CtxException
    {
        EmpiricalData ret = null;
        Hypothesis h = _hypothesis.get(dt);
        if (h != null)
            ret = h.dataFor(truth);

        return ret;
    }

    public List<EmpiricalData> dataFor(String dt, String tag)
        throws CtxException
    {
        Hypothesis hypo = _hypothesis.get(dt);
        return hypo.empiricalDataFor(tag);
    }

    public List<EmpiricalData> searchDataFor(String dt, String[] regex)
        throws CtxException
    {
        Hypothesis hypo = _hypothesis.get(dt);
        return hypo.searchEmpiricalData(regex);
    }

    public UUID atomicID() { return _atomicID; }

    protected void discardAllHypothesis()
        throws CtxException
    {
        for (Hypothesis h : _hypothesis.values())
            h.discard(_atomicID);
    }

    protected boolean endTxn()
        throws CtxException
    {
        boolean ended = true;
        try
        {
            for (Hypothesis h : _hypothesis.values())
                ended = h.endTxn(_atomicID) && ended;
        }
        catch (Exception e)
        {
            ended = false;
            except().rt(e, new CtxException.Context("Error starting:", "Exception"));
        }
        return ended;
    }

    protected void rollback()
        throws CtxException
    {
        discardAllHypothesis();
        endTxn();
    }

    protected boolean startTxn()
        throws CtxException
    {
        boolean started = true;
        try
        {
            for (Hypothesis h : _hypothesis.values())
                started = started && h.startTxn(_atomicID);
        }
        catch (Exception e)
        {
            started = false;
            except().rt(e, new CtxException.Context("Error starting:", "Exception"));
        }
        finally
        {
            if (!started)
                rollback();
        }
        return started;
    }

    protected boolean tryAcceptAllHypothesis()
        throws CtxException
    {
        boolean simulated = true;
        try
        {
            //not worried about order, hence not setup, so just
            //setup any order
            if (_orderedKeys.size() <= 0)
                _orderedKeys.addAll(_hypothesis.keySet());

            for (int i = 0; (simulated) && (i < _orderedKeys.size()); i++)
            {
                Hypothesis h = _hypothesis.get(_orderedKeys.get(i));
                if (h != null)
                    simulated = simulated && h.simulate(_atomicID);
            }
        }
        catch (Exception e)
        {
            simulated = false;
            except().rt(e, new CtxException.Context("Error in accepting", "Exception"));
        }
        finally
        {
            if (!simulated)
                rollback();
        }

        assertion().assertTrue(simulated, "Simulation had an error, cannot continue to commit transaction.");
        for (int i = 0; i < _orderedKeys.size(); i++)
        {
            Hypothesis h = _hypothesis.get(_orderedKeys.get(i));
            if (h != null)
                h.accept(_atomicID);
        }
        return true;
    }

    public void finish()
        throws CtxException
    {
        try
        {
            boolean started = startTxn();
            assertion().assertTrue(started, "Cannot start Transaction for: " + _atomicID);

            boolean outcome = true;
            for (Hypothesis h : _hypothesis.values())
                outcome = (outcome && h.outcome());

            starting(outcome);

            if (outcome)
                outcome = tryAcceptAllHypothesis();
            else
                discardAllHypothesis();

            ending(outcome);
        }
        catch (CtxException e)
        {
            discardAllHypothesis();
            throw e;
        }
        finally
        {
            endTxn();
        }
    }

    protected void starting(boolean outcome)
        throws CtxException
    {
        //nothing to be done.
    }

    protected void ending(boolean outcome)
        throws CtxException
    {
        //nothing to be done
    }
}

