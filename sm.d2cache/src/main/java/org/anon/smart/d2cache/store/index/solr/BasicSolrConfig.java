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
 * File:                org.anon.smart.d2cache.store.index.solr.BasicSolrConfig
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 12, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

public class BasicSolrConfig implements SolrConfig {

	//private String _solrHome = "/home/vjaasti/mywork/solr";
	private String _solrHome ;
	
	public BasicSolrConfig(String home)
	{
		_solrHome = home;
        if ((home == null) || (home.length() <= 0))
        {
            String path = System.getenv("SMART_PATH");
            if ((path == null) || (path.length() <= 0))
            {
            	path = System.getProperty("SMART_PATH");
            }
            if ((path == null) || (path.length() <= 0))
            {
                path = System.getenv("HOME") + "/solr/";
            }
            _solrHome = path + "/solr-datastore/";
        }
	}
	@Override
	public String getIndexHome() {
		// TODO Auto-generated method stub
		return _solrHome;
	}

}
