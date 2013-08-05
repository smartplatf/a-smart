package org.anon.smart.smcore.inbuilt.responses;

public class PrimeDefinition implements java.io.Serializable 
{
	private String message;
	private String rootPath;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
}