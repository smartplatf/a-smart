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
 * File:                org.anon.smart.codegen.factory.SoaFactory
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
package org.anon.smart.codegen.factory;

import java.io.IOException;
import java.util.Properties;

import org.anon.smart.codegen.Generator;
import org.anon.smart.codegen.Type;
import org.anon.smart.codegen.code.CodeGenerator;
import org.anon.smart.codegen.soa.FlowSoaGenerator;
import org.anon.utilities.exception.CtxException;

public class SoaFactory extends GeneratorFactory {

	@Override
	public Generator getGenerator(Type type) throws CtxException {
		String codeType = type.getType();
		Properties genProp = new Properties();
		try {
			genProp.load(this.getClass().getClassLoader()
					.getResourceAsStream("codegen.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new CtxException("Error in loading codegen properties file",
					e1);
		}
		Generator genfac = null;
		try {
			genfac = (Generator) (Class.forName(genProp.getProperty(codeType))
					.newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CtxException("Error in getting" + type + "generator", e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CtxException("Error in getting" + type + "generator", e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CtxException("Error in getting" + type + "generator", e);
		}
		return genfac;
	}
}
