package com.unitvectory.cfncustomresource;

public class CustomResourceOutcomeTestClient implements CustomResourceOutcome {

	private String responseURL;

	private String responseJson;

	public CustomResourceOutcomeTestClient() {
		this.responseURL = null;
		this.responseJson = null;
	}

	@Override
	public void putFile(String responseURL, String responseJson) {
		this.responseURL = responseURL;
		this.responseJson = responseJson;
	}

	public final String getResponseURL() {
		return responseURL;
	}

	public final String getResponseJson() {
		return responseJson;
	}
}
