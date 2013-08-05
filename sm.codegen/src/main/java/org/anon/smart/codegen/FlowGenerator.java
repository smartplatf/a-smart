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
 * File:                org.anon.smart.codegen.FlowGenerator
 * Author:              arjun
 * Revision:            1.0
 * Date:                03-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An abstract implementation of the Hypothesis
 *
 * ************************************************************
 * */
package org.anon.smart.codegen;

import org.apache.log4j.Logger;

public class FlowGenerator implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private GeneratorMeta meta;
	static Logger log = Logger.getLogger(FlowGenerator.class.getName());

	public FlowGenerator(GeneratorMeta meta) {
		this.meta = meta;
	}

	/*public void createFlow() throws CtxException {
		// prepare requisites
		String flowName = meta.getFlowName();
		String rootPath = meta.getRootPath();
		String jarName = flowName + JavaCodeGenConstants.JAVA_ARCHIVE_EXTENSION;

		
		// Create the java code
	try {
			Properties codeGenProps = new Properties();
			codeGenProps.load(this.getClass().getClassLoader()
					.getResourceAsStream("codegen.properties"));
			Generator codeGen = (Generator) (Class.forName(codeGenProps
					.getProperty(meta.getLanguage()))).newInstance();
			codeGen.generateCode(meta);
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			throw new CtxException("Error in generating flow", e1);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			throw new CtxException("Error in generating flow", e1);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new CtxException("Error in generating flow", e1);
		} catch (CtxException e) {
			e.printStackTrace();
			throw new CtxException("Error in generating flow", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CtxException("Error in generating flow", e);
		}

	}*/

}
