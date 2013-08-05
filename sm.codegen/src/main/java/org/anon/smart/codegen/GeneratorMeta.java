package org.anon.smart.codegen;

import java.io.Serializable;
import java.util.List;

public class GeneratorMeta implements Serializable {
	public String packageName;
	public String flowName;
	public String objectName;
	public String rootPath;
	public List<AttributeDefinition> userAttributes;
	public String TenantId;

	public GeneratorMeta() {
	}

	public GeneratorMeta(String packageName, String flowName,
			String primeObjectName, String rootPath) {
		this.packageName = packageName;
		this.flowName = flowName;
		this.objectName = primeObjectName;
		this.rootPath = rootPath;
	}

	public GeneratorMeta(String packageName, String flowName, String nature,
			String primeObjectName, String rootPath,
			List<AttributeDefinition> userAttributes) {
		this.packageName = packageName;
		this.flowName = flowName;
		this.objectName = primeObjectName;
		this.rootPath = rootPath;
		this.userAttributes = userAttributes;
	}
	
	public GeneratorMeta(String flowName, String tenantId) {
		super();
		this.flowName = flowName;
		TenantId = tenantId;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public String getFlowName() {
		return this.flowName;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public void setUserAttributes(List<AttributeDefinition> userAttributes) {
		this.userAttributes = userAttributes;
	}

	public String getRootPath() {
		return this.rootPath;
	}

	public List<AttributeDefinition> getUserAttributes() {
		return this.userAttributes;
	}

	public String getTenantId() {
		return TenantId;
	}

	public void setTenantId(String tenantId) {
		TenantId = tenantId;
	}
	
}