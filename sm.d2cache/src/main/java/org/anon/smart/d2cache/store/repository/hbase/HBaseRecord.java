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
 * File:                org.anon.smart.d2cache.store.repository.hbase.HBaseRecord
 * Author:              rsankar
 * Revision:            1.0
 * Date:                02-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A record for hbase
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;


import java.util.Collection;

import org.apache.hadoop.hbase.client.Put;

import org.anon.smart.d2cache.annot.CacheKeyAnnotate;
import org.anon.smart.d2cache.store.AbstractStoreRecord;

import static org.anon.utilities.services.ServiceLocator.*;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.exception.CtxException;

public class HBaseRecord extends AbstractStoreRecord implements Constants
{
    private Put _putRecord;
    private String _table;
    private HBaseConnection _conn;
    private int _keyCount;
    private String _simpleName;
    
    public HBaseRecord(String group, Object primarykey, Object curr, Object orig, HBaseConnection conn)
        throws CtxException
    {
        super(group, primarykey, curr, orig);
        
        if(curr instanceof RelatedObject)
        {
        	setGroup(RelatedObject.class.getSimpleName());
            _simpleName = RelatedObject.class.getSimpleName();
        }
        else
            _simpleName = curr.getClass().getSimpleName();

        try
        {
        	_conn = conn;
        	String cls = curr.getClass().getName();
        	//_table = _conn.getTableName(group);
        	_table = group;
            HBaseCRUD crud = conn.getCRUD();
            //RS -- change it from to String to nothing.
            //if it is an object, then this has to convert. TODO. Will come back
            _putRecord = crud.newRecord(primarykey, SYNTHETIC_COL_FAMILY, CLASSNAME, cls);
            _keyCount = 0;
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("HBaseRecord.init", "Exception"));
        }
    }

    public void append(DataContext ctx, boolean update)
        throws CtxException
    {
        try
        {
        	if (ctx.field() != null)
            {
                //String key = _group + PART_SEPARATOR + ctx.fieldpath();
                String key = _simpleName + PART_SEPARATOR + ctx.fieldpath();
                Object fldval = ctx.fieldVal();
                if (fldval != null)
                {
                   _conn.getCRUD().addTo(_putRecord, DATA_COL_FAMILY, key, fldval);
                   if(HBaseUtil.isFieldTypeNeeded(ctx.fieldType()))
                   {
                    	_conn.getCRUD().addTo(_putRecord, SYNTHETIC_COL_FAMILY, key+FIELDTYPE, fldval.getClass().getName());
                   }
                   if(Collection.class.isAssignableFrom(ctx.fieldType()))
                   {
                	   _conn.getCRUD().addTo(_putRecord, SYNTHETIC_COL_FAMILY, key+SIZE, ((Collection)fldval).size());
                   }
                    if (ctx.field().isAnnotationPresent(CacheKeyAnnotate.class))
                    {
                        _conn.getCRUD().addTo(_putRecord, DATA_COL_FAMILY, KEY_NAME + _keyCount, fldval);
                        _keyCount++;
                    }
                }
            }
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("HBaseRecord.append", "Exception"));
        }
    }

    Put putRecord() { return _putRecord; }
    String getTable() { return _table; }
}

