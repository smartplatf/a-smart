package org.anon.smart.smcore.inbuilt.events;

public class AttributeDefinition  implements java.io.Serializable 
{
	private String attributeName;
	private String attributeType;
	private Boolean isKey;
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public String getAttributeType() {
		return attributeType;
	}
	
	public Boolean getIsKey() {
		return isKey;
	}

	@Override
	public String toString() {
		return "AttributeDefinition [attributeName=" + attributeName
				+ ", attributeType=" + attributeType + ", isKey=" + isKey + "]";
	}
	
}
