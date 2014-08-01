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
import java.util.UUID;
import java.util.HashMap;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrDocument;

import org.anon.smart.d2cache.store.AbstractStoreRecord;
import org.anon.smart.d2cache.store.StoreConnection;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.exception.CtxException;

public class SolrRecord extends AbstractStoreRecord implements Constants
{
    public  static Map<Class, String> SUFFIXES = new HashMap<Class, String>();
    public  static Map<Class, String> LIST_SUFFIXES = new HashMap<Class, String>();

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
        SUFFIXES.put(UUID.class, "_s");

        LIST_SUFFIXES.put(Integer.TYPE, "_mi");
        LIST_SUFFIXES.put(Integer.class, "_mi");
        LIST_SUFFIXES.put(String.class, "_ms");
        LIST_SUFFIXES.put(Long.TYPE, "_ml");
        LIST_SUFFIXES.put(Long.class, "_ml");
        LIST_SUFFIXES.put(Double.class, "_md");
        LIST_SUFFIXES.put(Double.TYPE, "_md");
        LIST_SUFFIXES.put(Float.class, "_mf");
        LIST_SUFFIXES.put(Float.TYPE, "_mf");
        LIST_SUFFIXES.put(UUID.class, "_ms");
    }

    private SolrInputDocument _document;
    private SolrDocument _exist;
    private SolrConnection _connection;
    private String _idKey;
    private boolean _setup;

    public SolrRecord(String group, Object primaryKey, Object curr, Object orig, StoreConnection conn)
    {
        super(group, primaryKey, curr, orig);
        _document = new SolrInputDocument();
        //_document.addField(ID_COLUMN, group + PART_SEPARATOR + primaryKey.toString());
        _document.addField(ID_COLUMN, primaryKey.toString());
        //System.out.println("---> ID:"+ID_COLUMN+"::"+ primaryKey.toString());
        _idKey = primaryKey.toString();
        _connection = (SolrConnection)conn;
        _setup = false;
    }


    public void append(DataContext ctx, boolean update)
        throws CtxException
    {
        try
        {
            if (update && !_setup)
            {
                _exist = _connection.lookup(ID_COLUMN, _idKey);
                if (_exist != null)
                {
                    for (String key : _exist.keySet())
                    {
                        if ((!key.equals(ID_COLUMN)) && (!key.equals("_version_")))
                            _document.addField(key, _exist.get(key));
                    }
                    System.out.println("Got a document for the key: " + _idKey );
                }

                _setup = true;
            }
            if (ctx.field() != null)
            {
                if (ctx.field().isAnnotationPresent(NoIndex.class))
                    return;

            	String fieldPath = ctx.fieldpath();
            	String type = ctx.getType();
                //list hence use the list suffix instead
                Map<Class, String> usesuffix = SUFFIXES;
            	//remove type from path
            	if((type != null) && (type.length()>0))
            	{
                    usesuffix = LIST_SUFFIXES;
            		fieldPath = fieldPath.replaceAll("."+type, "");
                    System.out.println("----->Is a multi-value field Indexing:"+type+":"+ctx.field().getType());
            	}
                String key = _group + PART_SEPARATOR + fieldPath ;
                Object fldval = ctx.fieldVal();
                if ((fldval != null) && (usesuffix.containsKey(ctx.field().getType())))
                {
                    key = key + usesuffix.get(ctx.field().getType());
                    System.out.println("----->Indexing:"+key+":"+fldval);

                    //Just read the document and change it and write it back. Doing an update
                    //has a lot of problems esp when there are list items which are new.
                    /*if ((update) && ((ctx.getType() == null) || (ctx.getType().length() <= 0)))
                    {
                        Map<String, Object> partialUpdate = new HashMap<String, Object>();
                        partialUpdate.put("set", fldval);
                        _document.addField(key, partialUpdate);
                    }
                    else*/
                    if (update && ((ctx.getType() == null) || (ctx.getType().length() <= 0)))
                    {
                        //need to replace. cannot add, it will just append to the existing
                        //if it is a list, then append. Again can be a problem when data is
                        //removed, have to fix that bug.
                        _document.setField(key, fldval);
                    }
                    else
                        _document.addField(key, fldval);
                }
                //else
                 //   System.out.println("Not Indexing... " + ctx.field().getType() + ":" + fldval);
            }
            /*else
            {
                System.out.println("Indexing..." + ctx.traversingClazz().getName());
            }*/
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("SolrRecord.append", "Exception"));
        }
    }

    SolrInputDocument document() { return _document; }
}

