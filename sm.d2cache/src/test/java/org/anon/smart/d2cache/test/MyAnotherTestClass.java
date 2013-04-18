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
 * File:                org.anon.smart.d2cache.test.MyAnotherTestClass
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
import java.text.DateFormat;
import java.util.Date;

public class MyAnotherTestClass implements Serializable{
	private Date _date;
	
	public MyAnotherTestClass()
	{
		_date = new Date(System.currentTimeMillis());
	}
	
	/*public String toString() { 
		return "MyAnotherTestClass:"+_date;
	}*/
	
	public Date getDate() { return _date; }
}
