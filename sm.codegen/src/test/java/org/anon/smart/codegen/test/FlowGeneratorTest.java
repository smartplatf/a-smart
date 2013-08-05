package org.anon.smart.codegen.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.anon.smart.codegen.AttributeDefinition;
import org.anon.smart.codegen.FlowGenerator;
import org.anon.smart.codegen.GeneratorMeta;
import org.anon.smart.codegen.code.JavaCodeGenConstants;
import org.anon.smart.codegen.code.JavaCodeGenerator;
import org.anon.smart.generator.flowgen.FlowDescription;
import org.anon.smart.generator.flowgen.KeysAttributes;
import org.anon.smart.generator.flowgen.SoaFileGen;
import org.anon.utilities.exception.CtxException;
import org.anon.utilities.services.TypeService;

public class FlowGeneratorTest {
	String rootPath = "/E:/1234/";
	String packageName = "org.smart.comp";
	String objectName = "ContactRecord";

	// Create the meta data
	GeneratorMeta meta = new GeneratorMeta(packageName, "Contact", objectName,
			rootPath);/*
					 * "Contact", packageName, objectName,
					 * JavaCodeGenConstants.LANGUAGE_JAVA,
					 * JavaCodeGenConstants.JAVA_ARCHIVE_EXTENSION, rootPath);
					 */

	// @Test
	public void testJavaCodeGen() {
		JavaCodeGenerator codeGen = new JavaCodeGenerator();

		try {
			codeGen.generateCode(meta);
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assert (false);
		}

		File file = new File(rootPath + TypeService.convertToPath(packageName)
				+ objectName + JavaCodeGenConstants.JAVA_CODE_EXTENSION);
		boolean fileCreated = file.exists();
		assert (fileCreated);

		file = new File(rootPath + TypeService.convertToPath(packageName)
				+ objectName + JavaCodeGenConstants.JAVA_CLASS_EXTENSION);
		boolean fileCompiled = file.exists();
		assert (fileCompiled);

	}

	// @Test
	public void testFlowGen() {
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

		/*
		 * FlowGenerator flowGen = new FlowGenerator(meta); try {
		 * flowGen.createFlow(); } catch (CtxException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); assert (false); }
		 */
	}

	// @Test
	public void soaGenTest() {
		List<String> dEnable = new ArrayList<String>();
		dEnable = Arrays.asList("all", "notall");

		List<String> pData = new ArrayList<String>();
		pData = Arrays.asList("com.smart.anon.data1", "com.smart.anon.data2",
				"com.smart.anon.data3");

		List<String> data = new ArrayList<String>();
		data = Arrays.asList("com.smart.anon.data1", "com.smart.anon.data2",
				"com.smart.anon.data3");

		KeysAttributes keys = new KeysAttributes(
				"org.smart.comp.ContactRecord", "Name");
		List<KeysAttributes> key = new ArrayList<KeysAttributes>();
		key.add(keys);
		key.add(keys);

		FlowDescription flow = new FlowDescription("contact", dEnable, pData,
				data, key);
		SoaFileGen.generateSoa(flow, "e:/testingsoa/FlowSoa");
	}

	// @Test
	/*
	 * public void templateTest() { File target = new File("e:/template");
	 * AttributeDefinition attr1 = new AttributeDefinition("id", "Integer",
	 * true); AttributeDefinition attr2 = new AttributeDefinition("firstName",
	 * "String", false); AttributeDefinition attr3 = new
	 * AttributeDefinition("lastName", "String", false);
	 * List<AttributeDefinition> userAttributes = new ArrayList();
	 * userAttributes.add(attr1); userAttributes.add(attr2);
	 * userAttributes.add(attr3); meta.setUserAttributes(userAttributes);
	 * FlowGenerator flowGen = new FlowGenerator(meta);
	 * TemplateGenerator.makeTemplate(meta,target); }
	 */

}
