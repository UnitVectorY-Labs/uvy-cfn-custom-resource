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

public class CustomResourceConfig {

	private final CustomResourceOutcome customResourceOutcome;

	private final boolean printRequest;

	private final boolean printResponse;

	private CustomResourceConfig(CustomResourceOutcome customResourceOutcome, boolean printRequest,
			boolean printResponse) {
		this.customResourceOutcome = customResourceOutcome;
		this.printRequest = printRequest;
		this.printResponse = printResponse;
	}

	final CustomResourceOutcome getCustomResourceOutcome() {
		return customResourceOutcome;
	}

	final boolean isPrintRequest() {
		return printRequest;
	}

	final boolean isPrintResponse() {
		return printResponse;
	}

	public static class Builder {

		private CustomResourceOutcome customResourceOutcome;

		private boolean printRequest;

		private boolean printResponse;

		private Builder() {
			// Default values
			this.customResourceOutcome = new CustomResourceOutcomeUrlConnection();
			this.printRequest = false;
			this.printResponse = false;
		}

		/**
		 * Create a new configuration
		 * 
		 * @return the builder
		 */
		public static Builder create() {
			return new Builder();
		}

		/**
		 * Use a CustomResourceOutcome to override how the JSON file is uploaded to S3.
		 * 
		 * @param customResourceOutcome
		 *            the CustomResourceOutcome
		 * @return the builder
		 */
		public Builder withCustomResourceOutcome(CustomResourceOutcome customResourceOutcome) {
			if (customResourceOutcome == null) {
				throw new IllegalArgumentException("customResourceOutcome must not be null");
			}

			this.customResourceOutcome = customResourceOutcome;
			return this;
		}

		/**
		 * Print the JSON request to the console for debugging.
		 * 
		 * @return the builder
		 */
		public Builder withPrintRequest() {
			this.printRequest = true;
			return this;
		}

		/**
		 * Print the JSON response to the console for debugging.
		 * 
		 * @return the builder
		 */
		public Builder withPrintResponse() {
			this.printResponse = true;
			return this;
		}

		/**
		 * Builds the config
		 * 
		 * @return the config
		 */
		public CustomResourceConfig build() {
			return new CustomResourceConfig(customResourceOutcome, printRequest, printResponse);
		}
	}
}
