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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;

import org.anon.smart.d2cache.QueryObject;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreConfig;
import org.anon.smart.d2cache.ListParams;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.objservices.ConvertService.translator;
import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.logger.Logger;

public class SolrConnection implements StoreConnection, Constants
{
    private EmbeddedSolrServer _server;
    //private static CoreContainer _container;
    private StoreConfig _config;
    private File _home;
    private String _homeFile;
    private String _corename;
    
    private static SolrConnection _connection;
    
    public static final String CORENAME_DELIM = "-";
    private transient Logger _logger;
    public SolrConnection()
    {
    	_logger = logger().rlog(this);
    }

    public void connect(StoreConfig cfg)
        throws CtxException
    {
    	
        assertion().assertTrue((cfg instanceof SolrConfig), "Cannot create solr indexing with no solr configuration");
        _config = cfg;
        try
        {
        	//if(_container == null)
        	{
            SolrConfig scfg = (SolrConfig)cfg;
            _home = new File(scfg.getIndexHome());
            _homeFile = scfg.getIndexHome();
            assertion().assertTrue(_home.exists(), "The solr home directory does not exist. Please setup solr correctly." + scfg.getIndexHome());
            File solr = new File(_home, CORE_CONFIG);
            assertion().assertTrue(solr.exists(), "The solr config file does not exist. Please setup solr correctly." + CORE_CONFIG);
            //_container = new CoreContainer(scfg.getIndexHome(), solr);
            //_container.load(scfg.getIndexHome(), solr);
        	}
        
        }
        catch (Exception e)
        {
            System.out.println(">>>>>>>>>>>>>>>>>exceptionconnecting to: ");
            e.printStackTrace();
            except().rt(e, new CtxException.Context("SolrConnection.connect", "Exception"));
        }
    }

    public void open(String name)
        throws CtxException
    {
        try
        {
        	if(_server == null)
        	{
        	String corename = name;
        	if(name != null)
        	{
        		corename = name.split(CORENAME_DELIM, 2)[0];
        	}

            _corename = corename;
            
        	//System.out.println("CORE NAME is :"+corename);
            CoreContainer container = SolrContainerSingleton.getContainer(_homeFile);
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
                _server = new EmbeddedSolrServer(container, DEFAULT_CORE);
                CoreAdminRequest.createCore(corename, corename, _server, SOLR_CONFIG, SCHEMA_CONFIG);
                CoreAdminRequest.persist(CORE_CONFIG, _server);
                _server = new EmbeddedSolrServer(container, corename);
                _logger.info("Solr server opened for connections:"+_server+"::"+corename);
             }
            else
            {
                _logger.info("Core "+corename+" exists:reloading........");
                
                /*** WorkAround till correct solution ***/
                SolrCore core = container.getCore(corename);
                try
                {
                    if (core != null)
                    {
                        String iDir = core.getIndexDir();
                        String indexLock = iDir+File.separator+"write.lock";
                        File iFile = new File(indexLock);
                        if(iFile.isFile())
                        {
                            System.out.println("********* Deleting unremoved index lock file **********");
                            iFile.delete();
                        }
                    }
                }
                catch (Exception e)
                {
                    throw e;
                }
                finally
                {
                    core.close();//close to release references
                    System.out.println(">>>>>>>>>>>>>>>>>Solr core closed:"+corename + ":" + core.getOpenCount());
                }
                /*** WorkAround till correct solution END ***/
                
                _server = new EmbeddedSolrServer(container, corename);
                _logger.info(">>>>>>>>>>>>>>>>>Solr server opened for connections:"+_server+"::"+corename);
            }
        	}
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
    	System.out.println("!!!!!!!!!!!!!!!!!  Closing Solr Server !!!!!!!!!!!!!!!!!" + _server);
        if (_server != null)
        {
        	//_container.shutdown();
            //_server.shutdown();
            //_container = null;
            CoreContainer container = _server.getCoreContainer();
            if (container != null)
            {
                //checking if this releases the resources of the core.
                //container.reload(_corename);
            }
            _server = null;
        }

        _connection = null;
    }

    public Object find(String group, Object key)
        throws CtxException
    {
        //find is not implemented here.
        //
        return null;
    }

   

	@Override
	public void remove(String group, Object key) throws CtxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Object> search(String group, Object query, int size, int pn, int ps, String sby, boolean asc) 
        throws CtxException 
    {
		List<Object> resultSet = new ArrayList<Object>();
		QueryObject qo = (QueryObject)query;
		SolrQuery solrQuery = SolrQueryConstructor.getQuery(group, qo, size, pn, ps, sby, asc);
		try {
			perf().startHere("SolrSearch");
			QueryResponse qr = _server.query(solrQuery);
			SolrDocumentList docList = qr.getResults();
            qo.setNumFound(docList.getNumFound());
			perf().checkpointHere("SolrSearch");
			for(SolrDocument doc : docList)
			{
			    if(doc.getFieldValue(ID_COLUMN) != null)
			    {
			        UUID id = UUID.fromString((String)doc.getFieldValue(ID_COLUMN));
			        resultSet.add(id);
			    }
			}
			perf().dumpHere(_logger);
			System.out.println("Got after search: " + resultSet.size());
			
		} catch (SolrServerException e) {
			except().rt(e, new CtxException.Context("SolrConnection.search", "Exception while querying"));
		}
		return resultSet;
	}

    public SolrDocument lookup(String idcol, String id)
        throws CtxException
    {
        SolrDocument doc = null;
        try
        {
            SolrQuery query = new SolrQuery();
            query.setQuery( idcol + ":" + id );
            QueryResponse response = _server.query( query );
            SolrDocumentList docList = response.getResults();
            if (docList.size() > 0)
                doc = docList.get(0);
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("SolrConnection.lookup", "Exception while lookup: "));
        }

        return doc;
    }

    public Iterator<Object> list(ListParams parms)
        throws CtxException
    {
        //NO IMPLEMENTATION
        return null;
    }
	
	public static SolrConnection getConnection()
	{
		if(_connection == null)
		{
			_connection = new SolrConnection();
		}
		return _connection;
	}

    @Override
    public boolean exists(String group, Object key) 
        throws CtxException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<Object> getListings(String group, String sortBy,
            int listingsPerPage, int pageNum)
    {
        // TODO Auto-generated method stub
        return null;
    }
}

