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

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class CustomResourceOutcomeUrl implements CustomResourceOutcome {

	CustomResourceOutcomeUrl() {
	}

	@Override
	public void putFile(String responseURL, String responseJson) {
		try {
			URL url = new URL(responseURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("PUT");
			OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
			out.write(responseJson);
			out.close();
			int responseCode = httpConnection.getResponseCode();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
