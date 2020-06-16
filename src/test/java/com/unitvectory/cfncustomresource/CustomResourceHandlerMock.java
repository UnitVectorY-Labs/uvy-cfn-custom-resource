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

public class CustomResourceHandlerMock extends CustomResourceHandler {

	private boolean success;

	public CustomResourceHandlerMock(CustomResourceOutcome customResourceOutcome) {
		super(customResourceOutcome);
		this.success = true;
	}

	public void setFail() {
		this.success = false;
	}

	@Override
	public CustomResourceResponseCreate processCreate(String resourceType, String logicalResourceId, String stackId,
			CustomResourceRequestProperties customResourceRequestProperties) {

		String physicalResourceId = "testResourceId";

		if (success) {
			CustomResourceResponseCreate.Builder builder = CustomResourceResponseCreate.Builder
					.createSuccess(physicalResourceId);

			for (String key : customResourceRequestProperties.getKeys()) {
				if ("ServiceToken".equals(key)) {
					continue;
				}

				builder.withDataString(key, customResourceRequestProperties.getStringProperty(key));
			}

			return builder.build();
		} else {
			return CustomResourceResponseCreate.Builder.createError(physicalResourceId, "failed").build();
		}
	}

	@Override
	public CustomResourceResponseUpdate processUpdate(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties,
			CustomResourceRequestProperties customResourceRequestOldProperties) {

		if (success) {
			CustomResourceResponseUpdate.Builder builder = CustomResourceResponseUpdate.Builder.createSuccess();

			for (String key : customResourceRequestProperties.getKeys()) {
				if ("ServiceToken".equals(key)) {
					continue;
				}

				builder.withDataString(key, customResourceRequestProperties.getStringProperty(key));
			}

			return builder.build();
		} else {
			return CustomResourceResponseUpdate.Builder.createError("failed").build();
		}
	}

	@Override
	public CustomResourceResponseDelete processDelete(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties) {

		if (success) {
			return CustomResourceResponseDelete.Builder.createSuccess().build();
		} else {
			return CustomResourceResponseDelete.Builder.createError("failed").build();
		}
	}

}
