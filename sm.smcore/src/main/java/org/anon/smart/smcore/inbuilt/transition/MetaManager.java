package org.anon.smart.smcore.inbuilt.transition;

import static org.anon.utilities.services.ServiceLocator.assertion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.anon.smart.base.annot.KeyAnnotate;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.inbuilt.events.GetPrimeMeta;
import org.anon.smart.smcore.inbuilt.events.GetTemplateMeta;
import org.anon.smart.smcore.inbuilt.responses.MetaAttributes;
import org.anon.smart.smcore.inbuilt.responses.MetaResponse;
import org.anon.utilities.exception.CtxException;

public class MetaManager {

	public MetaManager() {
		// TODO Auto-generated constructor stub
	}

	// sends back meta data back of a class in a deployed flow.
	public void getMetaData(GetPrimeMeta event) throws CtxException {
		Object o = event;
		SmartEvent sevt = (SmartEvent) o;
		String flow = sevt.smart___flowname();
		System.out.println("Got the flow as ----->" + flow);
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		CrossLinkDeploymentShell shell = tenant.deploymentShell();
		CrossLinkFlowDeployment dep = shell.deploymentFor(flow);
		assertion().assertNotNull(dep, "Cannot find the deployment for " + flow);
		String clsname = event.getFlowClass();
		System.out.println("Got the Class name as ----->" + clsname);
		assertion().assertNotNull(clsname,"Cannot find the deployment class for: " + clsname + " in " + flow);
		Class cls = shell.primeClass(flow, clsname);
		System.out.println("Got the Shell class as ----->" + cls);

		String nm = cls.getName().replaceAll("\\.", "/") + ".class";
		System.out.println("Got the flow for: " + clsname + ":" + flow + ":" + cls + ":" + nm + ":" + cls.getClassLoader().getResource(nm));

		assertion().assertNotNull(cls, "Cannot find the class:" + clsname);
		Annotation[] annotation = cls.getAnnotations();
		/*
		 * System.out.println("<---------annotations list------------>"); for
		 * (int i = 0; i < annotation.length; i++)
		 * System.out.println(annotation[i]);
		 */

		List<MetaAttributes> fields = getMetaAttributes(cls);
		MetaResponse response = new MetaResponse(flow, clsname, fields);

	}

	// gets canonical class name from the GetTemplate meta to send back the meta
	// data of the class
	public void getTemplateMeta(GetTemplateMeta event) throws CtxException {
		System.out
				.println("<------IN GET-TEMPLATE-META transition------>GOT CLASS NAME AS : "
						+ event.getFqcn());
		Class clas = null;
		try {
			clas = Class.forName(event.getFqcn());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CtxException("Cannot find class " + clas);
		}
		String nm = clas.getName().replaceAll("\\.", "/") + ".class";
		System.out.println("Got : " + clas + ":" + nm + ":"
				+ clas.getClassLoader().getResource(nm));

		// assertion().assertNotNull(clas, "Cannot find the class:" + clsname);

		List<MetaAttributes> fields = getMetaAttributes(clas);
		MetaResponse response = new MetaResponse(clas.toString(), fields);
	}

	// returns list of meta data in the given class
	private List<MetaAttributes> getMetaAttributes(Class<?> c) {
		List<MetaAttributes> metaAttributes = new ArrayList<MetaAttributes>();
		try {
			Field fields[] = c.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				// if a field name starts with ___ then it is a smart injected
				// field - hence ignore
				MetaAttributes attribute = new MetaAttributes();
				if (!fieldName.startsWith("___")) {
					String fieldType = fields[i].getType().getName();
					if (fields[i].getAnnotation(KeyAnnotate.class) == null) {
						attribute = new MetaAttributes(fieldName, fieldType,
								false);
					} else {
						attribute = new MetaAttributes(fieldName, fieldType,
								true);
					}
					metaAttributes.add(attribute);
				}
			}
		} catch (Throwable e) {
			System.out.println(e);
		}

		return metaAttributes;
	}

}
