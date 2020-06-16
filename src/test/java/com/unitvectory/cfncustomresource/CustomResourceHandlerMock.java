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
			return CustomResourceResponseCreate.Builder.createSuccess(physicalResourceId).build();
		} else {
			return CustomResourceResponseCreate.Builder.createError(physicalResourceId, "failed").build();
		}
	}

	@Override
	public CustomResourceResponseUpdate processUpdate(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties,
			CustomResourceRequestProperties customResourceRequestOldProperties) {

		if (success) {
			return CustomResourceResponseUpdate.Builder.createSuccess().build();
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
