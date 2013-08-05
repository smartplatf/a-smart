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
 * File:                org.anon.smart.d2cache.store.index.solr.SolrRecord
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A solr related record to index:

 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.index.solr;

import java.util.Map;
import java.util.HashMap;

import org.apache.solr.common.SolrInputDocument;

import org.anon.smart.d2cache.store.AbstractStoreRecord;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.exception.CtxException;

public class SolrRecord extends AbstractStoreRecord implements Constants
{
    public  static Map<Class, String> SUFFIXES = new HashMap<Class, String>();

    static
    {
        //all supported types which can be indexed
        //TODO: add all types here
        SUFFIXES.put(Integer.TYPE, "_i");
        SUFFIXES.put(Integer.class, "_i");
        SUFFIXES.put(String.class, "_s");
        SUFFIXES.put(Long.TYPE, "_l");
        SUFFIXES.put(Long.class, "_l");
        SUFFIXES.put(Double.class, "_d");
        SUFFIXES.put(Double.TYPE, "_d");
        SUFFIXES.put(Float.class, "_f");
        SUFFIXES.put(Float.TYPE, "_f");
    }

    private SolrInputDocument _document;

    public SolrRecord(String group, Object primaryKey, Object curr, Object orig)
    {
        super(group, primaryKey, curr, orig);
        _document = new SolrInputDocument();
        //_document.addField(ID_COLUMN, group + PART_SEPARATOR + primaryKey.toString());
        _document.addField(ID_COLUMN, primaryKey.toString());
        //System.out.println("---> ID:"+ID_COLUMN+"::"+ primaryKey.toString());
    }


    public void append(DataContext ctx)
        throws CtxException
    {
        try
        {
            if (ctx.field() != null)
            {
            	String fieldPath = ctx.fieldpath();
            	String type = ctx.getType();
            	//remove type from path
            	if((type != null) && (type.length()>0))
            	{
            		fieldPath = fieldPath.replaceAll("."+type, "");
            	}
                String key = _group + PART_SEPARATOR + fieldPath ;
                Object fldval = ctx.fieldVal();
                if ((fldval != null) && (SUFFIXES.containsKey(ctx.field().getType())))
                {
                    key = key + SUFFIXES.get(ctx.field().getType());
                    _document.addField(key, fldval);
                    //System.out.println("----->Indexing:"+key+":"+fldval);
                }
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("SolrRecord.append", "Exception"));
        }
    }

    SolrInputDocument document() { return _document; }
}

