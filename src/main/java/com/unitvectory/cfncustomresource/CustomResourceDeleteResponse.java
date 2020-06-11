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

public class CustomResourceDeleteResponse {

	private final ResponseStatus responseStatus;

	private final String responseReason;

	private CustomResourceDeleteResponse(ResponseStatus responseStatus, String responseReason) {
		this.responseStatus = responseStatus;
		this.responseReason = responseReason;
	}

	final ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	final String getResponseReason() {
		return responseReason;
	}

	public static class Builder {

		private ResponseStatus responseStatus;

		private String responseReason;

		private Builder(ResponseStatus responseStatus, String responseReason) {
			this.responseStatus = responseStatus;
			this.responseReason = responseReason;
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
		 *            required error reason
		 * @return the builder
		 */
		public static Builder createError(String responseReason) {
			if (responseReason == null || responseReason.trim().length() == 0) {
				throw new IllegalArgumentException("Reason is required");
			}

			return new Builder(ResponseStatus.FAILED, responseReason);
		}

		/**
		 * Build the response.
		 * 
		 * @return the response
		 */
		public CustomResourceDeleteResponse build() {
			return new CustomResourceDeleteResponse(responseStatus, responseReason);
		}
	}
}
