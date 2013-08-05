package org.anon.smart.template.inbuilt.responses;

import java.io.Serializable;
import java.util.List;

import org.anon.smart.template.Template;

public class TemplateResponse implements Serializable {

	private List<Template> templateList;
	
	public TemplateResponse() {
		// TODO Auto-generated constructor stub
	}

	public TemplateResponse(List<Template> templateList) {
		super();
		this.templateList = templateList;
	}

	@Override
	public String toString() {
		return "TemplateResponse [templateList=" + templateList + "]";
	}

	
}
