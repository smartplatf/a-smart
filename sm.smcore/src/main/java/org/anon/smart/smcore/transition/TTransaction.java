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
 * File:                org.anon.smart.smcore.transition.TTransaction
 * Author:              rsankar
 * Revision:            1.0
 * Date:                27-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A transaction started which is used during commit
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.transition;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.anon.smart.atomicity.EmpiricalData;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.TenantConstants;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.base.dspace.TransactDSpace;
import org.anon.smart.d2cache.D2CacheTransaction;
import org.anon.smart.base.dspace.DSpaceService;
import org.anon.smart.smcore.data.FileItem;
import org.anon.smart.smcore.data.SmartData;
import org.anon.smart.smcore.data.SmartPrimeData;
import org.anon.smart.smcore.data.SmartDataED;
import org.anon.smart.smcore.data.SmartDataTruth;
import org.anon.smart.smcore.data.ConfigData;
import org.anon.smart.smcore.data.datalinks.LinkedData;
import org.anon.smart.smcore.transition.plugin.PluginManager;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class TTransaction
{
    private Map<String, D2CacheTransaction> _transactions;
    private UUID _txnID;

    public TTransaction(UUID txnid)
    {
        _transactions = new ConcurrentHashMap<String, D2CacheTransaction>();
    }

    public D2CacheTransaction startTransaction(SmartData object)
        throws CtxException
    {
        String flow = flowFor(object.getClass());
        if ((object.smart___group() != null) && (object.smart___group().length() > 0))
            flow = object.smart___group();
        System.out.println("Adding: " + object + ":" + flow + ":" + object.smart___group());
        if (!_transactions.containsKey(flow))
        {
        	createTransaction(flow);
        }

        return _transactions.get(flow);
    }

    public D2CacheTransaction getMonitorTransaction()
        throws CtxException
    {
        if(!_transactions.containsKey(TenantConstants.MONITOR_SPACE))
        {
        	createTransaction(TenantConstants.MONITOR_SPACE);
        }

        return _transactions.get(TenantConstants.MONITOR_SPACE);
    }
    
    public D2CacheTransaction startTransaction(FileItem object)
			throws CtxException 
    {

		if (!_transactions.containsKey(object.getFlowName() + "-files")) 
        {
			CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
			RuntimeShell rshell = (RuntimeShell) tenant.runtimeShell();
			TransactDSpace space = rshell.getSpaceFor(object.getFlowName());
			System.out.println("Got flow for: " + object.getFlowName() + ":"
					+ space + ":" + tenant.getName());
			D2CacheTransaction txn = DSpaceService.startFSTransaction(space,
					_txnID);
			_transactions.put(object.getFlowName() + "-files", txn);
		}

		return _transactions.get(object.getFlowName() + "-files");
	}

    public D2CacheTransaction startTransaction(ConfigData object)
        throws CtxException
    {
        if (!_transactions.containsKey(TenantConstants.CONFIG_SPACE))
            createTransaction(TenantConstants.CONFIG_SPACE);

        return _transactions.get(TenantConstants.CONFIG_SPACE);
    }
    
    public D2CacheTransaction getTransaction(String space)
    	throws CtxException
    {
    	if (!_transactions.containsKey(space))
    		createTransaction(space);
    	
    	return _transactions.get(space);
    }
    
    private void createTransaction(String flow)
    		throws CtxException
    {
    	   CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
           RuntimeShell rshell = (RuntimeShell)tenant.runtimeShell();
           TransactDSpace space = rshell.getSpaceFor(flow);
           D2CacheTransaction txn = DSpaceService.startTransaction(space, _txnID);
           _transactions.put(flow, txn);
    }
    

    public void addToTransaction(SmartData object)
        throws CtxException
    {
        String flow = flowFor(object.getClass());
        if ((object.smart___group() != null) && (object.smart___group().length() > 0))
            flow = object.smart___group();
        System.out.println("Adding: " + object + ":" + flow + ":" + object.smart___group());
        D2CacheTransaction txn = _transactions.get(flow);
        assertion().assertNotNull(txn, "No transaction has been started for: " + flow);
        DSpaceService.addObject(txn, object);
    }

    public void addToTransaction(LinkedData data, String flow)
        throws CtxException
    {
        D2CacheTransaction txn = _transactions.get(flow);
        assertion().assertNotNull(txn, "No transaction has been started for: " + flow); //assumption this is the from flow, hence shd be started
        DSpaceService.addObject(txn, data);
    }

    public void addToTransaction(ConfigData config)
        throws CtxException
    {
        D2CacheTransaction txn = _transactions.get(TenantConstants.CONFIG_SPACE);
        assertion().assertNotNull(txn, "No transaction has been started for config space");
        DSpaceService.addObject(txn, config);
    }
    
    public void addToTransaction(FileItem object) throws CtxException {
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		D2CacheTransaction txn = _transactions.get(object.getFlowName()
				+ "-files");
		assertion().assertNotNull(txn,
				"No transaction has been started for FILE space");
		String destFl = tenant.getName() + "/" + object.getFlowName() + "/" + object.getDestFileName();
		DSpaceService.addFSObject(txn, object.getSrcFile(),destFl,
				object.getFlowName());
	}
    
    public void addToTransaction(SmartDataED object)
            throws CtxException
    {
        String flow = null;
        flow = flowFor(object.empirical().getClass());
        if ((object.empirical().smart___group() != null) && (object.empirical().smart___group().length() > 0))
            flow = object.empirical().smart___group();
        System.out.println("Adding: " + object.empirical() + ":" + flow + ":" + object.empirical().smart___group());
        D2CacheTransaction txn = _transactions.get(flow);
        assertion().assertNotNull(txn, "No transaction has been started for: " + flow);
        DSpaceService.addObject(txn, ((SmartDataTruth)object.truth()).smartData(), object.empirical(), object.original());

        //hooks for plugins
        SmartData modified = object.empirical();
        if (modified.smart___isNew())
        {
            if (modified instanceof SmartPrimeData)
                PluginManager.primeObjectCreated((SmartPrimeData)modified);
            else
                PluginManager.objectCreated(modified);
        }
        else
        {
            PluginManager.objectModified(modified);
            SmartData truth = ((SmartDataTruth)object.truth()).smartData();
            if ((truth != null) && 
                    !truth.utilities___currentState().stateName().equals(modified.utilities___currentState().stateName()))
            {
                PluginManager.stateTransitioned(modified, truth.utilities___currentState().stateName(), modified.utilities___currentState().stateName());
            }
        }
    }

    public void commit()
        throws CtxException
    {
    	for (D2CacheTransaction txn : _transactions.values())
            txn.commit();
    }

    public void rollback()
        throws CtxException
    {
        for (D2CacheTransaction txn : _transactions.values())
            txn.rollback();
    }
}

