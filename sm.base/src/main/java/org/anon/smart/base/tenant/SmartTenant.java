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
 * File:                org.anon.smart.base.tenant.SmartTenant
 * Author:              rsankar
 * Revision:            1.0
 * Date:                04-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a tenant for the platform
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

import org.anon.smart.base.flow.FlowModel;
import org.anon.smart.base.flow.FlowAdmin;
import org.anon.smart.base.loader.LoaderVars;
import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.dspace.DSpaceObject;
import org.anon.smart.base.tenant.shell.CrossLinkDataShell;
import org.anon.smart.base.tenant.shell.CrossLinkRuntimeShell;
import org.anon.smart.base.tenant.shell.DeploymentShell;
import org.anon.smart.deployment.Deployment;
import org.anon.smart.deployment.Artefact;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.loader.RelatedObject;
import org.anon.utilities.exception.CtxException;

public class SmartTenant implements RelatedObject, TenantConstants,
		DSpaceObject, java.io.Serializable {

	private static final String FLOW_FEATURE_SEPARATOR = ":";

    private static final String LINK_SEPARATOR = "|";

	static {
		/*
		 * GROUP_MAPPING.put(USERS_SPACE, STANDARD_GROUP);
		 * GROUP_MAPPING.put(SESSIONS_SPACE, STANDARD_GROUP);
		 * GROUP_MAPPING.put(ROLES_SPACE, STANDARD_GROUP);
		 * GROUP_MAPPING.put(CONFIG_SPACE, STANDARD_GROUP);
		 * GROUP_MAPPING.put(MONITOR_SPACE, STANDARD_GROUP);
		 * GROUP_MAPPING.put(WORKING_SPACE, WORKING_GROUP);
		 */
	}

	private String _name;
	private boolean _platformOwner;
	private List<FeatureName> _enabledFeatureList;
    private List<String> _domain;
    private String _clientOf;
    private String _controlsAdmin;
	

	private transient SmartLoader _loader;
	private transient Map<String, CrossLinkDataShell> _data;
	private transient CrossLinkRuntimeShell _runtimeShell;
	private transient DeploymentShell _deploymentShell;
	private transient TenantAdmin _admin;
    private transient boolean _isNew = false;

	public SmartTenant(String name) throws CtxException {
		_name = name;
		_enabledFeatureList = new ArrayList<FeatureName>();
		initTenant();
        _isNew = true;
	}

	private void initTenant() throws CtxException {
		if ((_name.equals(PLATFORMOWNER))
				&& (this.getClass().getClassLoader() instanceof SmartLoader)) {
			SmartLoader ldr = (SmartLoader) this.getClass().getClassLoader();
			ldr.setRelatedTo(this);
			_loader = ldr;
			_platformOwner = true;
		} else if (this.getClass().getClassLoader() instanceof SmartLoader) {
			SmartLoader ldr = (SmartLoader) this.getClass().getClassLoader();
			LoaderVars vars = new LoaderVars(_name);
			_loader = ldr.repeatMe(vars);
			_loader.setRelatedTo(this);
			_platformOwner = false;
		} else {
			except().te(
					"Cannot create this object using any other classloader other than smart classloader");
		}

		createShells();
		// _admin = new TenantAdmin(name, this);
	}

	public TenantAdmin getAdmin() {
		return _admin;
	}

	public void setAdmin(TenantAdmin admin) {
		_admin = admin;
	}

    public void setDomain(String domain)
        throws CtxException
    {
        String[] d = value().listAsString(domain);
        _domain = new ArrayList<String>();
        for (int i = 0; i < d.length; i++)
            _domain.add(d[i]);
    }

    public String[] getDomain()
    {
        if (_domain != null)
            return _domain.toArray(new String[0]);
        else
            return null;
    }

    public void setClientOf(String clnt)
    {
        _clientOf = clnt;
    }

	private void createShells() throws CtxException {
		_data = new HashMap<String, CrossLinkDataShell>();
		int start = 0;
		// CrossLinkDataShell std = new CrossLinkDataShell(start, _loader);
		// start = std.addStandardSpaces();
		// _data.put(STANDARD_GROUP, std);
		// CrossLinkDataShell work = new CrossLinkDataShell(start, _loader);
		// start = work.addWorkingSpaces();
		// _data.put(WORKING_GROUP, work);
		CrossLinkDataShell flow = new CrossLinkDataShell(start, _loader);
		_data.put(FLOW_GROUP, flow);
		 CrossLinkDataShell std = new CrossLinkDataShell(start, _loader);
		start = std.addStandardSpaces();
		_data.put(STANDARD_GROUP, std);
		GROUP_MAPPING.put(MONITOR_SPACE, STANDARD_GROUP);
        GROUP_MAPPING.put(CONFIG_SPACE, STANDARD_GROUP);
		_runtimeShell = new CrossLinkRuntimeShell(_loader);
		_deploymentShell = new DeploymentShell(this, _loader);
	}

	public String getName() {
		return _name;
	}

	public ClassLoader getRelatedLoader() {
		return _loader;
	}

	public boolean isPlatformOwner() {
		return _platformOwner;
	}

	public Object dataShellFor(String spacemodel) {
		Object ret = null;
		CrossLinkDataShell shell = null;
		if (GROUP_MAPPING.containsKey(spacemodel))
			shell = _data.get(GROUP_MAPPING.get(spacemodel));
		else
			shell = _data.get(FLOW_GROUP);

		if (shell != null)
			ret = shell.link();

		return ret;
	}

	public Object runtimeShell() {
		return _runtimeShell.link();
	}

    public CrossLinkRuntimeShell rshell()
    {
        return _runtimeShell;
    }

	public DeploymentShell deploymentShell() {
		return _deploymentShell;
	}

	public void cleanup() throws CtxException {
		if (_runtimeShell != null)
			_runtimeShell.cleanup();
		if (_data != null) {
			for (CrossLinkDataShell shell : _data.values())
				shell.cleanup();
		}
		if (_deploymentShell != null)
			_deploymentShell.cleanup();
	}

	public void enableFlow(Object amodel, Artefact[] artefacts, Deployment d)
			throws CtxException {
		// for now. If anything more need to add here
		CrossLinkDataShell shell = _data.get(FLOW_GROUP);
		shell.addSpace(amodel);
		List<Class> deployed = new ArrayList<Class>();
		for (int i = 0; i < artefacts.length; i++) {
			Class clazz = artefacts[i].getClazz(_loader, d);
			deployed.add(clazz);
		}
		// for now
		try {
			deployed.add(_loader.loadClass(FlowAdmin.class.getName()));
		} catch (Exception e) {
			except().rt(e, new CtxException.Context("", ""));
		}
		_runtimeShell
				.enabledFlowClazzez(amodel, deployed.toArray(new Class[0]));
	}

	public List<Object> smart___keys() throws CtxException {
		List<Object> keys = new ArrayList<Object>();
		keys.add(_name);
		return keys;
	}

	public String smart___objectGroup() throws CtxException {
		return TENANTGROUP;
	}

    public boolean smart___isNew()
        throws CtxException
    {
        return _isNew;
    }

	@Override
	public void smart___initOnLoad() throws CtxException {
		System.out.println("Initializing Tenant after loading from store.."+_name+":Features::"+_enabledFeatureList);
		initTenant();
		enableFlows();

	}

	private void enableFlows() throws CtxException {
		Map<String, List<String>> flowFeatureMap = new HashMap<String, List<String>>();
        Map<String, Map<String, String>> links = new HashMap<String, Map<String, String>>();
		
		for(FeatureName feature: _enabledFeatureList)
		{
			String[] flowFeatureStr = feature.getName().split(FLOW_FEATURE_SEPARATOR, 2);
			assertion().assertTrue((flowFeatureStr.length == 2), feature.toString()+" is NOT WELL FORMED");
			String flowName = flowFeatureStr[0];
			String featureName = flowFeatureStr[1];
			if(flowFeatureMap.get(flowName) != null)
			{
				flowFeatureMap.get(flowName).add(featureName);
			}
			else
			{
				List<String> featureList = new ArrayList<String>();
				featureList.add(featureName);
				flowFeatureMap.put(flowName, featureList);
			}

            if ((feature.getLinks() != null) && (feature.getLinks().length() > 0))
            {
                String[] alllnks = feature.getLinks().split(";");
                Map<String, String> lnks = links.get(flowName);
                if (lnks == null)
                    lnks = new HashMap<String, String>();
                for (int i = 0; (alllnks != null) && (i < alllnks.length); i++)
                {
                    String[] vals = alllnks[i].split("\\" + LINK_SEPARATOR);
                    assertion().assertTrue((vals.length == 2), "Not stored correctly: " + alllnks[i]);
                    lnks.put(vals[0], vals[1]);
                }
                links.put(flowName, lnks);
            }
		}
		
		System.out.println("Reenabling Flows:"+flowFeatureMap + ":" + links);
		
		for(Map.Entry<String, List<String>> me : flowFeatureMap.entrySet())
		{
			//System.out.println("Enabling:"+me.getKey()+"::"+me.getValue());
            //TODO: need to check how to reenable the links
            Map<String, String> lnks = links.get(me.getKey());
            if (lnks == null)
                lnks = new HashMap<String, String>();
			_deploymentShell.enableForMe(me.getKey(), me.getValue().toArray(new String[0]), lnks);
		}
		
	}

	public void registerEnabledFlow(String name, String[] features, Map<String, String> lnks) {
		for(String feature : features)
        {
            FeatureName nm = new FeatureName(name+FLOW_FEATURE_SEPARATOR+feature);
            if (!_enabledFeatureList.contains(nm))
            {
                nm.addLink(lnks);
                _enabledFeatureList.add(nm);
            }
        }
        System.out.println("Currently enabled flows are: " + _enabledFeatureList);
	}

    public void registerLinks(String name, Map<String, String> lnks)
    {
        for (int i = 0; i < _enabledFeatureList.size(); i++)
        {
            FeatureName nm = _enabledFeatureList.get(i);
            if (nm.getName().startsWith(name))
                nm.addLink(lnks);
        }
    }
	
	public Set<String> listEnableFlows()
	    throws CtxException
	{
	    Set<String> enabledFlows = new HashSet<String>();
	    for(FeatureName feature: _enabledFeatureList)
        {
            String[] flowFeatureStr = feature.toString().split(FLOW_FEATURE_SEPARATOR, 2);
            assertion().assertTrue((flowFeatureStr.length == 2), feature.toString()+" is NOT WELL FORMED");
            String flowName = flowFeatureStr[0];
            enabledFlows.add(flowName);
        }
	    return enabledFlows;
	}

    public boolean controlsAdmin()
    {
        return ((_controlsAdmin != null) && (_controlsAdmin.equals("YES")));
    }

    public void setControlsAdmin(String val)
    {
        _controlsAdmin = val;
    }
	
	private class FeatureName{
		private String _name;
        private String _links;
		FeatureName(String name)
		{
			_name = name;
		}

        void addLink(Map<String, String> lnks)
        {
            for(String nm : lnks.keySet())
            {
                String val = lnks.get(nm);
                String lnk = nm + LINK_SEPARATOR + val;
                if ((_links == null) || (_links.length() <= 0))
                    _links = lnk;
                else
                    _links = _links + ";" + lnk;
            }
        }

        @Override
        public boolean equals(Object o)
        {
            boolean ret = false;
            if (o instanceof FeatureName)
            {
                ret = ((FeatureName)o)._name.equals(_name);
            }
            return ret;
        }

        @Override
        public int hashCode()
        {
            return _name.hashCode();
        }

        public String getName() { return _name; }
        public String getLinks() { return _links; }
		
		public String toString() { return _name; }
	}
}


