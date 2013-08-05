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
 * File:                org.anon.smart.d2cache.test.MyTestClass
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 5, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.d2cache.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.anon.smart.d2cache.CacheableObject;
import org.anon.utilities.exception.CtxException;
import org.apache.commons.collections.CollectionUtils;

public class MyTestClass implements CacheableObject, Serializable{
	
	private String _name;
	//private MyAnotherTestClass at;
	private List<MyAnotherTestClass> nonPrimList;
	private List<String> primList;
	private List<UUID> uuidList;
	public MyTestClass()
	{
		_name = "default name";
		MyAnotherTestClass at = new MyAnotherTestClass();
		
		nonPrimList = new ArrayList<MyAnotherTestClass>();
		nonPrimList.add(at);
		
		primList = new ArrayList<String>();
		primList.add("String1");
		primList.add("String2");
		
		uuidList = new ArrayList<UUID>();
		uuidList.add(UUID.randomUUID());
		uuidList.add(UUID.randomUUID());
		
	}
	
	
	public String toString(){
		//return "MyTestClass:"+at.getDate()+":"+_name+"::"+"NonPrimList size:"+nonPrimList.size()+":: primList size:"+primList.size();
		return "MyTestClass:"+":"+_name+"::"+"NonPrimList size:"+nonPrimList.toString()+":: primList size:"+primList.toString()+"::"+uuidList.toString();
	}

	@Override
	public void smart___initOnLoad() throws CtxException {
		// TODO Auto-generated method stub
		
	}
}
