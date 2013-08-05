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
 * File:                org.anon.smart.codegen.pack.Packager
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
package org.anon.smart.codegen.pack;

import java.io.File;
import java.io.IOException;

import org.anon.smart.codegen.GeneratorMeta;
import org.anon.smart.codegen.code.JavaCodeGenConstants;
import org.anon.utilities.exception.CtxException;
import org.apache.log4j.Logger;

public class Packager {
	static Logger log = Logger.getLogger(Packager.class.getName());
	
	public static void packageCode(GeneratorMeta meta) throws CtxException {
		
		String flowName = meta.getFlowName();
		String rootPath = meta.getRootPath();
		String jarName = flowName + JavaCodeGenConstants.JAVA_ARCHIVE_EXTENSION;
		
		// Package the content from the root path
		try {
			packageContent(jarName, rootPath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CtxException("Error in Creating Archive", e);
		}
	}
	
	// archive the contents
		public static void packageContent(String jarName, String parentDir)
				throws IOException {
			log.debug("Creating Jar Package...");
			File jarFile = new File(parentDir + jarName);
			File jaredFiles = new File(parentDir);
			JarUtil.jar(jaredFiles, jarFile, jarName, false);
		}
}
