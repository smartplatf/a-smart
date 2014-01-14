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
 * File:                org.anon.smart.smcore.data.SmartDataTruth
 * Author:              rsankar
 * Revision:            1.0
 * Date:                27-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A truth for smart data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.smart.atomicity.TruthData;
import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.smcore.anatomy.SMCoreContext;
import org.anon.smart.smcore.transition.TransitionContext;
import org.anon.smart.smcore.data.datalinks.DataLinker;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.lock.LockScheme;
import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class SmartDataTruth implements TruthData
{
    private transient SmartData _truthData;
    private transient boolean _isNew;
    private transient LockScheme _dataLock;
    private transient Map<UUID, SmartDataED> _allEmpirical;
    private transient Map<UUID, SmartDataED> _confirmed;

    public SmartDataTruth(SmartData data)
        throws CtxException
    {
        this(data, false);
    }

    public SmartDataTruth(SmartData data, boolean newobject)
        throws CtxException
    {
        assertion().assertNotNull(data, "Cannot create truth object for a null data.");
        _truthData = data;
        _isNew = newobject;
        initMe();
    }

    protected String representObjectAs()
        throws CtxException
    {
        String name = flowFor(_truthData.getClass());
        name += "|" + objectName(_truthData);
        name += "|" + _truthData.smart___id().toString();
        return name;
    }

    protected void initMe()
        throws CtxException
    {
        String represent = representObjectAs();
        _dataLock = anatomy().jvmEnv().createLock("SmartData|Lock|" + represent);
        _allEmpirical = anatomy().jvmEnv().mapFor("SmartData|Empirical|" + represent);
        _confirmed = new ConcurrentHashMap<UUID, SmartDataED>();
    }

    protected void lockData()
        throws CtxException
    {
        assertion().assertNotNull(_dataLock, "The smartdatatruth is not initialized correctly.");
        _dataLock.writeLock();
    }

    protected void unlockData()
        throws CtxException
    {
        assertion().assertNotNull(_dataLock, "This smart data truth is not initialized correctly.");
        _dataLock.writeUnlock();
    }

    public SmartData smartData() { return _truthData; }

    public UUID truthID()
    {
        return _truthData.smart___id();
    }

    public boolean start(UUID txnid)
        throws CtxException
    {
        assertion().assertTrue(_allEmpirical.containsKey(txnid), "Transaction not recorded. Cannot start transaction.");
        lockData();
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        ctx.transaction().startTransaction(_truthData);
        return true;
    }

    public boolean simulate(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        //TODO: for now nothing??
        _confirmed.put(txnid, (SmartDataED)edata);
        _allEmpirical.remove(txnid);
        return true;
    }

    public boolean accept(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        //need to copy dirty fields from edata to truthdata
        assertion().assertTrue(_confirmed.containsKey(txnid), "Cannot be accepted. The data is not present in the confirmed list.");
        TransitionContext ctx = (TransitionContext)threads().threadContext();
        assertion().assertNotNull(ctx, "The object is not in an transition context to be accepted.");
        //In this scenario the prime object from which it is linked is locked, hence we 
        //need not lock the linkeddata. TODO: if this is not true, then the data has to
        //be locked.
        //DataLinker linker = new DataLinker();
        DataLinker linker = ctx.getLinker();
        linker.createLinks(ctx, (SmartDataED)edata, _truthData, _isNew);

        //ctx.transaction().addToTransaction(_truthData);
        ctx.transaction().addToTransaction((SmartDataED)edata);

        //call the commit function if any present
        String commit = commitFor(_truthData.getClass());
        System.out.println("Got commit as: " + commit + ":" + _truthData.getClass());
        if ((commit != null) && (commit.length() > 0))
        {
            System.out.println("Invoking commit?" + commit);
            CrossLinkAny clany = new CrossLinkAny(_truthData);
            clany.invoke(commit);
        }
        _confirmed.remove(txnid);
        //need to add me to the cache.
        _isNew = false;
        SMCoreContext.coreContext().putTruthFor(_truthData, this);
        return true;
    }

    public void discard(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        _allEmpirical.remove(txnid);
        if (_isNew)
            SMCoreContext.coreContext().invalidate(_truthData);
    }

    public boolean end(UUID txnid)
        throws CtxException
    {
        unlockData();
        return true;
    }

    public void recordEmpiricalData(UUID txnid, EmpiricalData edata)
        throws CtxException
    {
        _allEmpirical.put(txnid, (SmartDataED)edata);
    }
}

