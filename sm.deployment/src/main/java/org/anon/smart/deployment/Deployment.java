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
 * File:                org.anon.smart.deployment.Deployment
 * Author:              rsankar
 * Revision:            1.0
 * Date:                12-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A representation of a single deployment
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStream;

import static org.anon.utilities.services.ServiceLocator.*;
import static org.anon.utilities.objservices.ObjectServiceLocator.*;

import org.anon.utilities.config.Format;
import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class Deployment implements Deployable, VerifiableObject
{
    private ClassLoader _loader;
    private String name;
    private String initializer;
    private String defaultEnable;
    private List<Feature> features;
    //private List<Relation> relations;

    private List<String> _jars;
    private String _runClass;
    private String _runMethod;
    private boolean _verified;
    private Map<String, Feature> _features;

    public Deployment()
    {
        _verified = false;
        _features = new HashMap<String, Feature>();
    }

    protected Deployment(String nm, Artefact[] a)
    {
        _verified = true;
        name = nm;
        features = new ArrayList<Feature>();
        //relations = new ArrayList<Relation>();
        _features = new HashMap<String, Feature>();
        addArtefacts(a);
    }

    protected Deployment(Deployment dep, String[] feats)
        throws CtxException
    {
        //by default enable the default feature
        name = dep.name;
        initializer = dep.initializer;
        defaultEnable = dep.defaultEnable;
        //relations = dep.relations;
        _runClass = dep._runClass;
        _runMethod = dep._runMethod;
        _verified = true;

        features = new ArrayList<Feature>();
        _features = new HashMap<String, Feature>();
        addFeatureFrom(dep, dep.defaultEnable);
        for (int i = 0; (feats != null) && (i < feats.length); i++)
            addFeatureFrom(dep, feats[i]);

        _jars = new ArrayList<String>();
        _jars.addAll(dep._jars);
    }

    public List<String> myJars() { return _jars; }

    public void addFeatureFrom(Deployment dep, String feature)
        throws CtxException
    {
        if (_features.containsKey(feature))
            return;

        Feature f = dep._features.get(feature);
        assertion().assertNotNull(f, "Cannot find the feature: " + feature + " to enable.");
        features.add(f);
        _features.put(f.getName(), f);
        List<String> sub = f.mysubs();
        for (int i = 0; (sub != null) && (i < sub.size()); i++)
            addFeatureFrom(dep, sub.get(i));
    }

    public <T extends Deployment> T createDefault(String[] features, Class<T> cls)
        throws CtxException
    {
        return cls.cast(new Deployment(this, features));
    }

    public void addArtefacts(Artefact[] artefacts)
    {
    }

    public String[] deployedURI(String nm)
    {
        //sub feature?
        List<String> uris = new ArrayList<String>();
        String myuri = deployedURI();
        for (Feature f : features)
        {
            System.out.println("Searching in feature: " + f.getName());
            f.deployedURI(nm, myuri, uris);
        }

        return uris.toArray(new String[0]);
    }

    public String deployedURI()
    {
        return "/" + name;
    }

    public String deployedName()
    {
        return name;
    }

    public boolean belongsToMe(Class cls)
    {
        return false;
    }

    public boolean isVerified()
    {
        return _verified;
    }

    public boolean verify()
        throws CtxException
    {
        //TODO: verifications on data
        _verified = true;
        return _verified;
    }

    public Feature featureFor(String fn)
    {
        return _features.get(fn);
    }

    public Feature[] getFeatures()
    {
        return features.toArray(new Feature[0]);
    }

    protected void setup()
        throws CtxException
    {
        if (initializer != null)
        {
            String[] run = initializer.split(".");
            if ((run != null) && (run.length > 0))
                _runClass = run[0];
            if ((run != null) && (run.length > 1))
                _runMethod = run[1];
        }

        if (_features == null)
            _features = new HashMap<String, Feature>();

        for (Feature f : features)
        {
            f.setBelongsTo(this);
            _features.put(f.getName(), f);
        }
    }

    public List<String> featureArtefacts(String nm)
        throws CtxException
    {
        Feature f = featureFor(nm);
        return f.allArtefacts();
    }

    public List<String> featureArtefacts()
        throws CtxException
    {
        List<String> ret = new ArrayList<String>();
        for (Feature f : features)
            ret.addAll(f.allArtefacts());

        return ret;
    }

    public List<String> artefacts()
    {
        return null; //default does not have any. Must be implemented in others
    }

    public static <T extends Deployment> T deploymentFor(InputStream str, Class<T> cls, ClassLoader ldr, String[] jars)
        throws CtxException
    {
        Format fmt = config().readYMLConfig(str);
        Map vals = fmt.allValues();
        Deployment dep = (Deployment)convert().mapToVerifiedObject(cls, vals);
        dep._loader = ldr;
        dep._jars = new ArrayList<String>();
        for (int i = 0; (jars != null) && (i < jars.length); i++)
            dep._jars.add(jars[i]);
        dep.setup();
        return cls.cast(dep);
    }

    public static <T extends Deployment> T deploymentFor(String dep, Artefact[] a, Class<T> cls)
        throws CtxException
    {
        CrossLinkAny cla = new CrossLinkAny(cls.getName(), cls.getClassLoader());
        return cls.cast(cla.create(new Class[] { String.class, Artefact[].class }, new Object[] { dep, a }));
    }

    public static <T extends Deployment> T deploymentFrom(T dep, String[] features, Class<T> cls)
        throws CtxException
    {
        return dep.createDefault(features, cls);
    }
}

