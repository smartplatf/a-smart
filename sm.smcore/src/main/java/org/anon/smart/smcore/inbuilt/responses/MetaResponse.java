package org.anon.smart.smcore.inbuilt.responses;

import java.util.List;

public class MetaResponse {

	public MetaResponse() {
		// TODO Auto-generated constructor stub
	}

	private String flowName;
	private String className;
	private List<MetaAttributes> attributes;

	public MetaResponse(String flowName, String className,
			List<MetaAttributes> attributes) {
		super();
		this.flowName = flowName;
		this.className = className;
		this.attributes = attributes;
	}

	public MetaResponse(String className, List<MetaAttributes> attributes) {
		super();
		this.className = className;
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "MetaResponse [flowName=" + flowName + ", className="
				+ className + ", attributes=" + attributes + "]";
	}


}
