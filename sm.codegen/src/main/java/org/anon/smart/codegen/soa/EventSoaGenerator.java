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
 * File:                org.anon.smart.codegen.soa.EventSoaGenerator
 * Author:              arjun
 * Revision:            1.0
 * Date:                03-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * 
 *
 * ************************************************************
 * */
package org.anon.smart.codegen.soa;

import java.io.Serializable;

import org.anon.smart.codegen.GeneratorMeta;
import org.anon.utilities.exception.CtxException;

public class EventSoaGenerator extends SoaGenerator implements Serializable {

	public EventSoaGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String generateCode(GeneratorMeta meta) throws CtxException {
		// TODO Auto-generated method stub
		return "success";
		
	}

	@Override
	public String compileCode(GeneratorMeta meta) throws CtxException {
		// TODO Auto-generated method stub
		return "success";
	}

}
