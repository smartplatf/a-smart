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
 * File:                org.anon.smart.d2cache.store.repository.hbase.SilentObjectCreator
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Mar 26, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.store.repository.hbase;

import sun.reflect.ReflectionFactory;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.ServiceLoader;

import org.anon.utilities.exception.CtxException;
import org.anon.utilities.services.ServiceLocator;
import org.springframework.core.CollectionFactory;

public class SilentObjectCreator {
  public static <T> T create(Class<T> clazz) {
    return create(clazz, Object.class);
  }

  public static <T> T create(String cls) throws ClassNotFoundException, CtxException {
	  ServiceLocator.assertion().assertNotNull(cls, "SilentObjectCreation: Class is null");
	  return (T) create(Class.forName(cls));
  }
  
  public static <T> T create(Class<T> clazz,
                             Class<? super T> parent) {
    try {
    	if(Collection.class.isAssignableFrom(clazz))
    	{
    		return (T)CollectionFactory.createCollection(clazz, 10);
    		
    	}
      ReflectionFactory rf =
          ReflectionFactory.getReflectionFactory();
      Constructor objDef = parent.getDeclaredConstructor();
      Constructor intConstr = rf.newConstructorForSerialization(
          clazz, objDef
      );
      return clazz.cast(intConstr.newInstance());
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException("Cannot create object", e);
    }
  }
}
