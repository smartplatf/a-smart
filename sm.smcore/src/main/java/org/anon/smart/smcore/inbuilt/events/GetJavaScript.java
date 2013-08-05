package org.anon.smart.smcore.inbuilt.events;

import java.io.Serializable;

public class GetJavaScript implements Serializable {
	
	private String tenantID;
	private String flowName;

	public GetJavaScript() {
		// TODO Auto-generated constructor stub
	}
	
	public GetJavaScript(String tenantID, String flowName) {
		super();
		this.tenantID = tenantID;
		this.flowName = flowName;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	@Override
	public String toString() {
		return "GetJavaScript [tenantID=" + tenantID + ", flowName=" + flowName
				+ "]";
	}
	
}
