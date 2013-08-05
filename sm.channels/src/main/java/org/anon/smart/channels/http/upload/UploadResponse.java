package org.anon.smart.channels.http.upload;

public class UploadResponse {

	/**
	 * Status can be any one of the following success: if the upload request was
	 * completed successfully failed: if the request failed error: if something
	 * else happened
	 */
	public String status;

	// the object describes the addtional information associated with
	// the status
	public Object response;
	
	public UploadResponse(String s, Object o){
		status = s;
		response = o;
	}

	public String toString() {
		return "{ status:" + status + "{ response:" + response.toString() + " }" + " }";

	}
}
