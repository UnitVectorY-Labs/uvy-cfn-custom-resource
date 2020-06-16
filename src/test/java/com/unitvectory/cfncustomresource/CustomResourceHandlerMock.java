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
