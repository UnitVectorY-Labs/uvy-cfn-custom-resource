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

public class CustomResourceUpdateResponse {

	private final ResponseStatus responseStatus;

	private final String responseReason;

	private final Boolean responseNoEcho;

	private final Map<String, String> dataString;

	private CustomResourceUpdateResponse(ResponseStatus responseStatus, String responseReason, Boolean responseNoEcho,
			Map<String, String> dataString) {
		this.responseStatus = responseStatus;
		this.responseReason = responseReason;
		this.responseNoEcho = responseNoEcho;
		this.dataString = dataString;
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

		public static Builder createSuccess() {
			return new Builder(ResponseStatus.SUCCESS, null);
		}

		public static Builder createError(String responseReason) {
			if (responseReason == null || responseReason.trim().length() == 0) {
				throw new IllegalArgumentException("Reason is required");
			}

			return new Builder(ResponseStatus.FAILED, responseReason);
		}

		public Builder withNoEcho() {
			if (!ResponseStatus.SUCCESS.equals(this.responseStatus)) {
				throw new IllegalStateException("Only allowed for SUCCESS");
			}

			this.responseNoEcho = true;
			return this;
		}

		public Builder withDataString(String key, String value) {
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

		public CustomResourceUpdateResponse build() {
			Map<String, String> dataStringMap = new TreeMap<String, String>();
			dataStringMap.putAll(this.dataString);

			return new CustomResourceUpdateResponse(responseStatus, responseReason, responseNoEcho,
					Collections.unmodifiableMap(dataStringMap));
		}
	}
}
