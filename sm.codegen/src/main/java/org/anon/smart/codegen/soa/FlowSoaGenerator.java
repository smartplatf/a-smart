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
 * File:                org.anon.smart.codegen.soa.FlowSoaGenerator
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
import java.util.ArrayList;
import java.util.List;

import org.anon.smart.codegen.AttributeDefinition;
import org.anon.smart.codegen.GeneratorMeta;
import org.anon.utilities.exception.CtxException;
import org.apache.log4j.Logger;
import org.anon.smart.generator.flowgen.FlowDescription;
import org.anon.smart.generator.flowgen.KeysAttributes;
import org.anon.smart.generator.flowgen.SoaFileGen;

public class FlowSoaGenerator extends SoaGenerator implements Serializable {
	static Logger log = Logger.getLogger(SoaGenerator.class.getName());

	public FlowSoaGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String generateCode(GeneratorMeta meta) throws CtxException {

		String flowName = meta.getFlowName();
		String rootPath = meta.getRootPath();
		List<AttributeDefinition> attributes = meta.getUserAttributes();

		// create Soa file
		log.debug("Generating soa file in root path....");
		String soaString = meta.getPackageName() + "." + meta.getObjectName();

		List<String> dEnable = new ArrayList();
		dEnable.add("all");

		List<String> pData = new ArrayList();
		pData.add(soaString);

		List<String> data = new ArrayList();
		// data.add(soaString);

		// Iterate through attribute definition to find key
		List<KeysAttributes> key = new ArrayList();
		for (AttributeDefinition attr : attributes) {
			if (attr.getIskey()) {
				KeysAttributes keys = new KeysAttributes(soaString, makeMixedCase(attr.getAttributeName()));
				key.add(keys);
			}
		}

		FlowDescription flow = new FlowDescription(flowName, dEnable, pData, data, key);

		SoaFileGen.generateSoa(flow, rootPath);
		return "soa creation success";
	}

	@Override
	public String compileCode(GeneratorMeta meta) throws CtxException {
		// TODO Auto-generated method stub
		return "success";
	}
	
	 private static String makeMixedCase(String name)
	    {
	        StringBuffer sb = new StringBuffer();
	        char ch = name.charAt(0);
	        sb.append(Character.toLowerCase(ch));
	        sb.append(name.substring(1));
	        return sb.toString();
	    }

}
