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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomResourceResponseUpdate {

	private final ResponseStatus responseStatus;

	private final String responseReason;

	private final Boolean responseNoEcho;

	private final Map<String, String> dataString;

	private CustomResourceResponseUpdate(Builder builder) {
		this.responseStatus = builder.responseStatus;
		this.responseReason = builder.responseReason;
		this.responseNoEcho = builder.responseNoEcho;

		Map<String, String> dataStringMap = new TreeMap<String, String>();
		dataStringMap.putAll(builder.dataString);

		this.dataString = Collections.unmodifiableMap(dataStringMap);
	}

	final ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	final String getResponseReason() {
		return responseReason;
	}

	final Boolean getResponseNoEcho() {
		return responseNoEcho;
	}

	final Map<String, String> getDataString() {
		return dataString;
	}

	public static class Builder {

		private ResponseStatus responseStatus;

		private String responseReason;

		private Boolean responseNoEcho;

		private Map<String, String> dataString;

		private Builder(ResponseStatus responseStatus, String responseReason) {
			this.responseStatus = responseStatus;
			this.responseReason = responseReason;
			this.dataString = new HashMap<String, String>();
		}

		/**
		 * Create a new successful response
		 * 
		 * @return the builder
		 */
		public static Builder createSuccess() {
			return new Builder(ResponseStatus.SUCCESS, null);
		}

		/**
		 * Create a new error response
		 * 
		 * @param responseReason
		 *            required error reason; must not be null
		 * @return the builder
		 */
		public static Builder createError(String responseReason) {
			if (responseReason == null || responseReason.trim().length() == 0) {
				throw new IllegalArgumentException("Reason is required");
			}

			return new Builder(ResponseStatus.FAILED, responseReason);
		}

		/**
		 * Indicates whether to mask the output of the custom resource when retrieved by
		 * using the Fn::GetAtt function. If set to true, all returned values are masked
		 * with asterisks (*****). The default value is false. For more information
		 * about using NoEcho to mask sensitive information, see the Do Not Embed
		 * Credentials in Your Templates best practice.
		 * 
		 * Only allowed to be used on successful responses.
		 * 
		 * @return the builder
		 */
		public final Builder withNoEcho() {
			if (!ResponseStatus.SUCCESS.equals(this.responseStatus)) {
				throw new IllegalStateException("Only allowed for SUCCESS");
			}

			this.responseNoEcho = true;
			return this;
		}

		/**
		 * The custom resource provider-defined name-value pairs to send with the
		 * response. You can access the values provided here by name in the template
		 * with Fn::GetAtt. Important: If the name-value pairs contain sensitive
		 * information, you should use the NoEcho field to mask the output of the custom
		 * resource. Otherwise, the values are visible through APIs that surface
		 * property values (such as DescribeStackEvents).
		 * 
		 * Only allowed to be used on successful responses.
		 * 
		 * @param key
		 *            the property name
		 * @param value
		 *            the property value
		 * @return the builder
		 */
		public final Builder withDataString(String key, String value) {
			if (!ResponseStatus.SUCCESS.equals(this.responseStatus)) {
				throw new IllegalStateException("Only allowed for SUCCESS");
			}

			if (key == null) {
				return this;
			}

			if (value == null) {
				this.dataString.remove(key);
			} else {
				this.dataString.put(key, value);
			}

			return this;
		}

		/**
		 * Build the response.
		 * 
		 * @return the response
		 */
		public final CustomResourceResponseUpdate build() {

			return new CustomResourceResponseUpdate(this);
		}
	}
}
