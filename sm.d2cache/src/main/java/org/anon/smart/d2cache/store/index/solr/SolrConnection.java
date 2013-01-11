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
 * File:                org.anon.smart.d2cache.store.index.solr.SolrConnection
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A connection to solr indexing
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.List;

import org.apache.solr.core.CoreContainer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;

import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreConfig;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class SolrConnection implements StoreConnection, Constants
{
    private EmbeddedSolrServer _server;
    private CoreContainer _container;
    private StoreConfig _config;
    private File _home;

    public SolrConnection()
    {
    }

    public void connect(StoreConfig cfg)
        throws CtxException
    {
        assertion().assertTrue((cfg instanceof SolrConfig), "Cannot create solr indexing with no solr configuration");
        try
        {
            SolrConfig scfg = (SolrConfig)cfg;
            _home = new File(scfg.getSolrHome());
            assertion().assertTrue(_home.exists(), "The solr home directory does not exist. Please setup solr correctly.");
            File solr = new File(_home, CORE_CONFIG);
            assertion().assertTrue(solr.exists(), "The solr config file does not exist. Please setup solr correctly.");
            _container = new CoreContainer();
            _container.load(scfg.getSolrHome(), solr);
            _config = cfg;

        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("SolrConnection.connect", "Exception"));
        }
    }

    public void open(String related, String name)
        throws CtxException
    {
        try
        {
            String corename = related + "_" + name;
            File f = new File(_home, corename);
            if (!f.exists())
            {
                //has not yet been created. Create again.
                URL url = getClass().getClassLoader().getResource(SOLR_CONFIG);
                assertion().assertTrue((url != null), "The solr config and schema jars are not a part of the classpath. Please include it.");
                url = getClass().getClassLoader().getResource(SCHEMA_CONFIG);
                assertion().assertTrue((url != null), "The solr config and schema jars are not a part of the classpath. Please include it.");
                f.mkdirs(); //create the directory

                //have to test if there needs to be synchronization
                _server = new EmbeddedSolrServer(_container, DEFAULT_CORE);
                CoreAdminRequest.createCore(corename, corename, _server, SOLR_CONFIG, SCHEMA_CONFIG);
                CoreAdminRequest.persist(SOLR_CONFIG, _server);
                _server = new EmbeddedSolrServer(_container, corename);
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("SolrConnection.open", "Exception"));
        }
    }

    public EmbeddedSolrServer server() { return _server; }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        return new SolrConnection();
    }

    public void createMetadata(String name, Class cls)
        throws CtxException
    {
        //no specific metadata
    }

    public StoreTransaction startTransaction(UUID txn)
        throws CtxException
    {
        return new SolrTransaction(txn, this);
    }

    public void close()
        throws CtxException
    {
        if (_server != null)
            _server.shutdown();
    }

    public Object find(String group, Object key)
        throws CtxException
    {
        //find is not implemented here.
        //
        return null;
    }

    public List<Object> search(String group, String query)
        throws CtxException
    {
        //search is impleneted here?
        //TODO
        return null;
    }
}

