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
 * File:                org.anon.smart.d2cache.store.index.solr.SolrContainerSingleton
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A singleton using which the solr instance is loaded
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

import java.io.File;

import org.apache.solr.core.CoreContainer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;

import org.anon.utilities.utils.VMSingleton;
import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class SolrContainerSingleton extends VMSingleton implements Constants
{
    private static final String STTREGNAME = "org.anon.smart.d2cache.store.index.solr.SolrContainerSingleton";

    private static SolrContainerSingleton SINGLE_INSTANCE = null;

    private CoreContainer _container;

    private static void setSingleInstance(Object obj)
    {
        if (SINGLE_INSTANCE == null)
            SINGLE_INSTANCE = (SolrContainerSingleton)obj;
    }

    private static SolrContainerSingleton getSingleInstance()
    {
        return SINGLE_INSTANCE;
    }

    public static CoreContainer getContainer(String homefile)
        throws CtxException
    {
        String nm = SolrContainerSingleton.class.getName();
        String creator = ContainerCreator.class.getName();
        SolrContainerSingleton single = (SolrContainerSingleton)getInstance(nm, creator, "createContainer", homefile);
        assertion().assertNotNull(single, "Cannot create a container.");
        return single._container;
    }

    public static EmbeddedSolrServer getServer(String homefile, String core)
        throws CtxException
    {
        CoreContainer container = getContainer(homefile);
        return new EmbeddedSolrServer(container, core);
    }

    private SolrContainerSingleton(String homefile)
        throws CtxException
    {
        try
        {
            File home = new File(homefile);
            assertion().assertTrue(home.exists(), "The solr home directory does not exist. Please setup solr correctly." + homefile);
            File solr = new File(home, CORE_CONFIG);
            assertion().assertTrue(solr.exists(), "The solr config file does not exist. Please setup solr correctly." + CORE_CONFIG);
            _container = new CoreContainer(homefile, solr);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("Exception", e.getMessage()));
        }
    }

    public static class ContainerCreator
    {
        public static Object createContainer(String homefile)
            throws CtxException
        {
            return new SolrContainerSingleton(homefile);
        }
    }
}

