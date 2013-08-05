package org.anon.smart.smcore.inbuilt.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefinePrime implements Serializable {
	private String flowName;
	private String className;
	private String tenantID;
	private List<AttributeDefinition> attrDefinitions;

	public DefinePrime() {
		this.attrDefinitions = new ArrayList();
	}

	public String getFlowName() {
		return this.flowName;
	}

	public String getClassName() {
		return this.className;
	}

	public List<AttributeDefinition> getAttrDefinitions() {
		return this.attrDefinitions;
	}

	public String getTenantID() {
		return this.tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String toString() {
		return "DefinePrime [flowName=" + this.flowName + ", className="
				+ this.className + ", attrDefinitions=" + this.attrDefinitions
				+ "]";
	}
}