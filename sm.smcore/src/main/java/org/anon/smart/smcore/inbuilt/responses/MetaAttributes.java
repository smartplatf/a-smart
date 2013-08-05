package org.anon.smart.smcore.inbuilt.responses;

public class MetaAttributes {
	
	private String attributeName;
	private String attributeType;
	private boolean isKey;
	
	public MetaAttributes(String attributeName, String attributeType,
			boolean isKey) {
		super();
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.isKey = isKey;
	}

	public MetaAttributes() {
		// TODO Auto-generated constructor stub
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		attributeType = attributeType;
	}

	public boolean getIsKey() {
		return isKey;
	}

	public void setIsKey(Boolean isKey) {
		this.isKey = isKey;
	}

}
