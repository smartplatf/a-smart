package org.anon.smart.smcore.inbuilt.transition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.anon.smart.base.tenant.TenantAdmin;
import org.anon.smart.codegen.Generator;
import org.anon.smart.codegen.GeneratorMeta;
import org.anon.smart.codegen.Nature;
import org.anon.smart.codegen.Type;
import org.anon.smart.codegen.factory.GeneratorFactory;
import org.anon.smart.codegen.pack.Packager;
import org.anon.smart.smcore.inbuilt.events.DefinePrime;
import org.anon.smart.smcore.inbuilt.responses.PrimeDefinition;
import org.anon.utilities.exception.CtxException;

public class CodeGenManager {

	public void generateCode(TenantAdmin owner, DefinePrime defineprime)
			throws CtxException {
		GeneratorMeta meta = new GeneratorMeta();
		String flowname = defineprime.getFlowName();
		String clsName = defineprime.getClassName();
		String packname = "org.smart." + defineprime.getTenantID();

		String rootpath = System.getProperty("Smart.Codegen.DumpPath");

		File rootPathF = new File(rootpath);
		String completePath = rootPathF.getAbsolutePath();
		if (!rootPathF.exists()) {
			try {
				rootPathF.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
				throw new CtxException("Cannot access the target directory" + e);
			}

		}

		// check for the rootpath
		rootpath = completePath + "/" + defineprime.getTenantID() + "/"
				+ defineprime.getFlowName() + "/";
		File targetpath = new File(rootpath);
		// File parent = targetpath.getParentFile();

		if (!targetpath.exists()) {
			targetpath.mkdirs();
		}

		meta.setFlowName(flowname);
		meta.setObjectName(clsName);
		meta.setPackageName(packname);
		meta.setUserAttributes(setAttributes(defineprime.getAttrDefinitions()));
		meta.setRootPath(rootpath);
		try {
			GeneratorFactory generatorFac = GeneratorFactory.getInstance(Nature.CODE.getNature());
			// System.out.println("The class instantiated --> " +
			// generatorFac.getClass().getName());

			Generator gen = generatorFac.getGenerator(Type.JAVA);
			// System.out.println("getting generator--->" +
			// gen.getClass().getCanonicalName());

			gen.generateCode(meta);

			gen.compileCode(meta);

			// create soa file
			GeneratorFactory generFac = GeneratorFactory.getInstance(Nature.SOA.getNature());
			// System.out.println("The class instantiated --> "+
			// generFac.getClass().getName());

			Generator genr = generFac.getGenerator(Type.FLOWSOA);
			//System.out.println("getting generator--->" + genr.getClass().getCanonicalName());

			genr.generateCode(meta);

			genr.compileCode(meta);

			// package the code
			Packager.packageCode(meta);
			// send event response
			PrimeDefinition response = new PrimeDefinition();
			response.setMessage("Success");
			response.setRootPath(rootpath);
		} catch (CtxException e) {
			e.printStackTrace();
			throw new CtxException("Error while trying to create the flow" + e);
		}
	}

	private List<org.anon.smart.codegen.AttributeDefinition> setAttributes(
			List<org.anon.smart.smcore.inbuilt.events.AttributeDefinition> userattrs) {
		List attribDef = new ArrayList();
		for (org.anon.smart.smcore.inbuilt.events.AttributeDefinition definition : userattrs) {

			org.anon.smart.codegen.AttributeDefinition attr = new org.anon.smart.codegen.AttributeDefinition(
					definition.getAttributeName(),
					definition.getAttributeType(), definition.getIsKey());

			attribDef.add(attr);
		}
		return attribDef;
	}
}
