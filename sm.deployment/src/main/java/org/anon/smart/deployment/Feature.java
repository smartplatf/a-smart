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
 * File:                org.anon.smart.deployment.Feature
 * Author:              rsankar
 * Revision:            1.0
 * Date:                12-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A feature of Artefacts
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.List;
import java.util.ArrayList;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.verify.VerifiableObject;
import org.anon.utilities.exception.CtxException;

public class Feature implements Deployable, VerifiableObject
{
    private String name;
    private String depends;
    private List<String> artefacts;
    private List<String> subFeatures;
    private boolean _verified;
    private Deployment _belongsTo;

    private Feature()
    {
    }

    public String getName()
    {
        return name;
    }

    void setBelongsTo(Deployment dep)
    {
        _belongsTo = dep;
    }

    public void deployedURI(String nm, String relative, List<String> addTo)
    {
        String retURI = "";
        if (artefacts.contains(nm))
        {
            String uri = relative + "/" + name;
            addTo.add(uri);
        }

        for (int i = 0; (subFeatures != null) && (i < subFeatures.size()); i++)
        {
            Feature f = _belongsTo.featureFor(subFeatures.get(i));
            if (f != null)
                f.deployedURI(nm, relative + "/" + name, addTo);
        }
    }

    public String deployedURI()
    {
        return _belongsTo.deployedURI() + "/" + name;
    }

    public String deployedName()
    {
        return name;
    }

    public List<String> myartefacts() { return artefacts; }
    public List<String> mysubs() { return subFeatures; }

    public List<String> allArtefacts()
        throws CtxException
    {
        List<String> ret = new ArrayList<String>();
        ret.addAll(artefacts);
        for (int i = 0; (subFeatures != null) && (i < subFeatures.size()); i++)
        {
            Feature f = _belongsTo.featureFor(subFeatures.get(i));
            assertion().assertNotNull(f, "Invalid feature deployed. " + subFeatures.get(i));
            ret.addAll(f.allArtefacts());
        }

        return ret;
    }

    public boolean belongsToMe(Class cls)
    {
        String clsname = cls.getName();
        boolean ret = artefacts.contains(clsname);
        //we have assumed that sub features are also a part of the same deployment
        for (int i = 0; (!ret) && (i < subFeatures.size()); i++)
        {
            Feature f = _belongsTo.featureFor(subFeatures.get(i));
            if (f != null)
                ret = f.belongsToMe(cls);
        }

        return ret;
    }

    public boolean isVerified() { return _verified; }

    public boolean verify()
        throws CtxException
    {
        //TODO:
        _verified = true;
        return _verified;
    }
}

