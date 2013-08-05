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
 * File:                org.anon.smart.codegen.factory.GeneratorFactory
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
import java.io.Serializable;
import java.util.Properties;

import org.anon.smart.codegen.Generator;
import org.anon.smart.codegen.Type;
import org.anon.utilities.exception.CtxException;

public abstract class GeneratorFactory implements Serializable {

	// returns a factory based on nature. codenature or soanature
	public static GeneratorFactory getInstance(String nature)
			throws CtxException {
		Properties genProp = new Properties();
		try {
			genProp.load(GeneratorFactory.class.getClassLoader().getResourceAsStream("nature.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new CtxException("Error in loading nature property file", e1);
		}
		
		GeneratorFactory genfac = null;
		try {
			genfac = (GeneratorFactory) (Class.forName(genProp.getProperty(nature)).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new CtxException("Error in getting" + nature + "factory", e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CtxException("Error in getting" + nature + "factory", e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CtxException("Error in getting" + nature + "factory", e);
		}
		return genfac;
	}

	public abstract Generator getGenerator(Type type) throws CtxException;
}
