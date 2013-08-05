/**
 * 
 */
package org.anon.smart.codegen.factory.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.anon.smart.codegen.AttributeDefinition;
import org.anon.smart.codegen.Generator;
import org.anon.smart.codegen.GeneratorMeta;
import org.anon.smart.codegen.Nature;
import org.anon.smart.codegen.Type;
import org.anon.smart.codegen.code.JSCodeGenerator;
import org.anon.smart.codegen.code.JavaCodeGenerator;
import org.anon.smart.codegen.factory.CodeGenFactory;
import org.anon.smart.codegen.factory.GeneratorFactory;
import org.anon.smart.codegen.factory.SoaFactory;
import org.anon.smart.codegen.pack.Packager;
import org.anon.smart.codegen.soa.FlowSoaGenerator;
import org.anon.utilities.exception.CtxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Arjun
 * 
 */
public class GeneratorFactoryTest {
	String rootPath = "/C:/Codegentemp/";
	String packageName = "org.smart.comp";
	String objectName = "ContactRecord";
	String tenant = "abcde";
	String flow = "Testflow";

	
	//@Test
	public void testGetInstance() {
		GeneratorMeta meta = new GeneratorMeta(packageName, "Contact", objectName, rootPath);
		AttributeDefinition attr1 = new AttributeDefinition("id", "Integer",
				true);
		AttributeDefinition attr2 = new AttributeDefinition("firstName",
				"String", false);
		AttributeDefinition attr3 = new AttributeDefinition("lastName",
				"String", false);
		List<AttributeDefinition> userAttributes = new ArrayList();
		userAttributes.add(attr1);
		userAttributes.add(attr2);
		userAttributes.add(attr3);
		meta.setUserAttributes(userAttributes);
		try {
			GeneratorFactory generatorFac = GeneratorFactory.getInstance(Nature.CODE.getNature());
			System.out.println("The class instantiated --> " + generatorFac.getClass().getName());
			assertTrue(generatorFac instanceof CodeGenFactory);
			Generator gen = generatorFac.getGenerator(Type.JAVA);
			System.out.println("getting generator--->" + gen.getClass().getCanonicalName());
			assertTrue(gen instanceof JavaCodeGenerator);
			//generate src file
			gen.generateCode(meta);
			//compile thus created file
			gen.compileCode(meta);
			
			GeneratorFactory generFac = GeneratorFactory
					.getInstance(Nature.SOA.getNature());
			System.out.println("The class instantiated --> " + generFac.getClass().getName());
			assertTrue(generFac instanceof SoaFactory);
			Generator genr = generFac.getGenerator(Type.FLOWSOA);
			System.out.println("getting generator--->" + genr.getClass().getCanonicalName());
			assertTrue(genr instanceof FlowSoaGenerator);
			
			//generate src file
			genr.generateCode(meta);
			//compile thus created file
			genr.compileCode(meta);
			
			Packager.packageCode(meta);

		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//@Test
	public void ScriptTest(){
		GeneratorMeta meta = new GeneratorMeta(flow, tenant);
		String snippet; 
		try {
			GeneratorFactory generatorFac = GeneratorFactory.getInstance(Nature.CODE.getNature());
			System.out.println("The class instantiated --> " + generatorFac.getClass().getName());
			assertTrue(generatorFac instanceof CodeGenFactory);
			Generator gen = generatorFac.getGenerator(Type.JSCRIPT);
			System.out.println("getting generator--->" + gen.getClass().getCanonicalName());
			assertTrue(gen instanceof JSCodeGenerator);
			//generate src file
			snippet  = gen.generateCode(meta);
			System.out.println(snippet);
		}
		catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
