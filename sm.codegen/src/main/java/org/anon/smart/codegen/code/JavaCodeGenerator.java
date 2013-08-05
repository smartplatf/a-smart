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
 * File:                org.anon.smart.codegen.JavaCodeGenerator
 * Author:              arjun
 * Revision:            1.0
 * Date:                03-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * Class to generate files with extension .java
 *
 * ************************************************************
 * */
package org.anon.smart.codegen.code;

import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.anon.smart.codegen.AttributeDefinition;
import org.anon.smart.codegen.GeneratorMeta;
import org.anon.smart.generator.code.Cardinality;
import org.anon.smart.generator.code.ClassDescription;
import org.anon.smart.generator.code.Codegen;
import org.anon.smart.generator.code.Style;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.services.TypeService;
import org.apache.log4j.Logger;


public class JavaCodeGenerator extends CodeGenerator {

	static Logger log = Logger.getLogger(JavaCodeGenerator.class.getName());

	public JavaCodeGenerator() {
		// TODO Auto-generated constructor stub
	}

	// to generate java file and compile
	public String generateCode(GeneratorMeta meta) throws CtxException {
		String packageName = meta.getPackageName();
		String objectClassName = meta.getObjectName();
		String rootPath = meta.getRootPath();
		List<AttributeDefinition> userAttr = meta.getUserAttributes();
		// Path where .java file has to be created
		String srcTargetPath = rootPath	+ TypeService.convertToPath(packageName) + "/";

		log.debug("Generating java file.... ");
		ClassDescription foo = new ClassDescription(packageName,
				objectClassName);

		// Iterate through attribute definition to populate object
		for (AttributeDefinition temp : userAttr) {
			foo.attribute(temp.getAttributeType(), temp.getAttributeName(),
					Cardinality.ZeroToOne);
		}

		// generates java file with the help of velocity template
		Codegen.generateSource(foo, srcTargetPath, Style.RichlyMutable);
		return "creation success";
	}

	// compile the java source file
	public String compileCode(GeneratorMeta meta) throws CtxException {
		log.debug("Compiling the java file...");
		String packageName = meta.getPackageName();
		String objectClassName = meta.getObjectName();
		String rootPath = meta.getRootPath();
		// Path where .java file has to be created
		String srcTargetPath = rootPath
				+ TypeService.convertToPath(packageName) + "/";
		String fileName = objectClassName
				+ JavaCodeGenConstants.JAVA_CODE_EXTENSION;

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		int compiled = compiler.run(null, null, null, srcTargetPath + fileName);
		if (compiled != 0)
			throw new CtxException("Error during java compilation");
		
		return "compile success";
	}

}
