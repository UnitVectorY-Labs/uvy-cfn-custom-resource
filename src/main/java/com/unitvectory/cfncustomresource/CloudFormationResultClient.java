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

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

class CloudFormationResultClient implements CloudFormationResult {

	private final CloseableHttpClient httpclient;

	CloudFormationResultClient() {
		httpclient = HttpClients.createDefault();
	}

	@Override
	public void putFile(String responseURL, String json) {
		CloseableHttpResponse httpResponse = null;
		try {
			HttpPut httpPut = new HttpPut(responseURL);
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Content-type", "application/json");

			StringEntity stringEntity = new StringEntity(json);
			httpPut.setEntity(stringEntity);

			httpResponse = httpclient.execute(httpPut);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

}
