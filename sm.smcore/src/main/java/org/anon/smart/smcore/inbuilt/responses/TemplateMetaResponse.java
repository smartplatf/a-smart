package org.anon.smart.smcore.inbuilt.responses;

import java.io.Serializable;
import java.util.List;

public class TemplateMetaResponse implements Serializable {

	public TemplateMetaResponse() {
		// TODO Auto-generated constructor stub
	}
	
	private String className;
	private List<MetaAttributes> attributes;

	public TemplateMetaResponse(String className, List<MetaAttributes> attributes) {
		super();
		this.className = className;
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "TemplateMetaResponse [className="
				+ className + ", attributes=" + attributes + "]";
	}

}
