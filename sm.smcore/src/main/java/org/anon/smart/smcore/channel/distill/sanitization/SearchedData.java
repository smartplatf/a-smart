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
 * File:                org.anon.smart.smcore.channel.distill.sanitization.SearchedData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                19-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An isotope that has all the searched data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.sanitization;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.flow.FlowDeployment;
import org.anon.smart.smcore.channel.distill.ChannelConstants;
import org.anon.utilities.exception.CtxException;

public class SearchedData extends Isotope implements ChannelConstants
{
    public class PrimeFlow
    {
        Object _primeObject;
        Object _flowObject;

        PrimeFlow(Object p, Object f)
        {
            _primeObject = p;
            _flowObject = f;
        }

        public Object flow() { return _flowObject; }
    }

    private CrossLinkSmartTenant _tenant;
    private String _flow;
    private FlowDeployment _flowDeployment;
    private CrossLinkFlowDeployment _clFlowDeployment;
    //private Object _session;
    private Map<String, List<Object>> _searchedData;
    private List<PrimeFlow> _primeSearch;
    private Class _eventClass;
    private Object _eventLegend;
    private Map<String, Object> _mappedData;

    private Map<String, Object> _values;

    public SearchedData(Isotope parent)
    {
        super(parent);
        _searchedData = new HashMap<String, List<Object>>();
        _primeSearch = new ArrayList<PrimeFlow>();
    }

    public void setupMappedData(Map<String, Object> m) { _mappedData = m; }
    public Map<String, Object> mappedData() { return _mappedData; }

    void setupTenant(CrossLinkSmartTenant tenant)
    {
        _tenant = tenant;
    }

    public CrossLinkSmartTenant tenant() { return _tenant; }

    void setupFlow(String flow)
    {
        _flow = flow;
    }

    public String getFlow() { return _flow; }

    void setupFlowDeployment(FlowDeployment flow)
    {
        _flowDeployment = flow;
    }
    
    void setupFlowDeployment(CrossLinkFlowDeployment flow)
    {
    	_clFlowDeployment = flow;
    }

    public FlowDeployment flowDeployment() { return _flowDeployment; }
    
    public CrossLinkFlowDeployment crossLinkFlowDeployment() { return _clFlowDeployment; }

    void setupEventClass(Class cls) { _eventClass = cls; }

    public Class eventClass() { return _eventClass; }

    void addSearch(String key, Object obj)
    {
        List<Object> lobj = null;
        if (_searchedData.containsKey(key))
        {
            lobj = _searchedData.get(key);
        }
        else
        {
            lobj = new ArrayList<Object>();
        }

        lobj.add(obj);
        _searchedData.put(key, lobj);
    }

    void addSearch(String key, List<Object> obj)
    {
        _searchedData.put(key, obj);
    }

    public List<Object> get(String key)
    {
        return _searchedData.get(key);
    }

    void addPrime(Object obj, Object flow)
    {
        PrimeFlow f = new PrimeFlow(obj, flow);
        _primeSearch.add(f);
    }

    public List<PrimeFlow> getPrimes()
    {
        return _primeSearch;
    }

    public void setupEventLegend(Object legend)
    {
        _eventLegend = legend;
    }

    public Object eventLegend()
    {
        return _eventLegend;
    }

    public void setupSearchMap(PrimeFlow flow) throws CtxException
    {
        _values = new HashMap<String, Object>();
        _values.put(EVENT_LEGEND_FLD, _eventLegend);
        _values.put(FLOW_FLD, flow._flowObject);
        _values.put(PRIMEDATA_FLD, flow._primeObject);
        if(_flowDeployment != null)
            _values.put(FLOW_NAME_FLD, _flowDeployment.deployedName());
        else
        	_values.put(FLOW_NAME_FLD, _clFlowDeployment.deployedName());
        //_values.put(SESSION_FLD, _session);
        for (String key : _searchedData.keySet())
        {
            //hmm have to check whether it is list or single object?
            //no question of having hosted objects within sub objects
            //we will just not support that.
            _values.put(key, _searchedData.get(key));
        }
    }

    public boolean isSearched(String fld)
    {
        return _values.containsKey(fld);
    }

    public Object searchedValue(String fld)
    {
        return _values.get(fld);
    }

    public void setupSearchMap(Map<String, Object> m) { _values = m; }
    public Map<String, Object> searchedMap() { return _values; }
}

