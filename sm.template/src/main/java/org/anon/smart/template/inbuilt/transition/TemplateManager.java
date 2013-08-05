package org.anon.smart.template.inbuilt.transition;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.anon.smart.template.Template;
import org.anon.smart.template.inbuilt.events.GetTemplates;
import org.anon.smart.template.inbuilt.responses.TemplateResponse;
import org.anon.utilities.exception.CtxException;

public class TemplateManager implements Serializable {
	public void getTemplates(GetTemplates getTemplates)
			throws CtxException {
		System.out
				.println("<--------Got the response for the GETTEMPLATE event----------->");

		Properties templateList = new Properties();
		try {
			templateList.load(getClass().getClassLoader().getResourceAsStream(
					"Template.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new CtxException("Cannot find file template.properties");
		}

		List allTemplates = getAllTemplates(templateList);
		TemplateResponse response = new TemplateResponse(allTemplates);
	}

	private List<Template> getAllTemplates(Properties templateProp)
			throws CtxException {
		List tempList = new ArrayList();
		Enumeration allKeys = templateProp.keys();
		while (allKeys.hasMoreElements()) {
			String nextKey = (String) allKeys.nextElement();
			String classFullName = templateProp.getProperty(nextKey);
			System.out.println("<--------Got the class----------->" + nextKey
					+ "  :  " + classFullName);
			try {
				Template template = (Template) Class.forName(classFullName)
						.newInstance();
				Template responseTemplate = new Template(template.getName(),
						template.getDescription());
				responseTemplate
						.setFqcn(template.getClass().getCanonicalName());
				tempList.add(responseTemplate);
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new CtxException("class cannot be instantiated");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new CtxException("class illegal access");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new CtxException("class not found exception");
			}
		}
		return tempList;
	}
}
