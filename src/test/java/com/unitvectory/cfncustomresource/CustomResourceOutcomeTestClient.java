/*
 * Copyright 2020 Jared Hatfield, UnitVectorY Labs
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
