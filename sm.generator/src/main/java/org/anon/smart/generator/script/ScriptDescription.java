package org.anon.smart.generator.script;

public class ScriptDescription {
	
	private String tenant;
	private String flow;

	public ScriptDescription() {
		// TODO Auto-generated constructor stub
	}

	public ScriptDescription(String tenant, String flow) {
		super();
		this.tenant = tenant;
		this.flow = flow;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}
	
	

}
