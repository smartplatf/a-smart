package org.anon.smart.smcore.inbuilt.transition;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.anon.smart.smcore.inbuilt.events.GetTemplateMeta;
import org.anon.smart.smcore.inbuilt.responses.MetaAttributes;
import org.anon.smart.smcore.inbuilt.responses.MetaResponse;
import org.anon.utilities.exception.CtxException;

public class TemplateMetaManager implements Serializable {

	public TemplateMetaManager() {
		// TODO Auto-generated constructor stub
	}
	
	// gets canonical class name from the GetTemplate meta to send back the meta
		// data of the class
	public void getTemplateMeta(GetTemplateMeta event) throws CtxException {
			System.out.println("<------IN GET-TEMPLATE-META transition------>GOT CLASS NAME AS : "
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
					if (!fieldName.startsWith("___")) {
						String fieldType = fields[i].getType().getName();
						MetaAttributes attribute = new MetaAttributes(fieldName,
								fieldType, false);
						metaAttributes.add(attribute);
					}
				}
			} catch (Throwable e) {
				System.out.println(e);
			}

			return metaAttributes;
		}

}
