# uvy-cfn-custom-resource
Java library AWS Lambda for CloudFormation custom resources

This provides a class `AbstractCustomResourceHandler` to extend that simplifies the process of creating custom resources.
Three abstract methods are provided that provide the opportunity to handle the create, update, and delete lifecycle signals provided by CloudFormation.
Builders are provided to construct responses simplifying the implementation.
The library takes care of parsing the request, forming the response, and sending the outcome to S3 for processing by CloudFormation.

```
public class ExampleHandler extends AbstractCustomResourceHandler {

	@Override
	public CustomResourceCreateResponse processCreate(String resourceType, String logicalResourceId, String stackId,
			CustomResourceRequestProperties customResourceRequestProperties) {

		String physicalResourceId = UUID.randomUUID().toString();

		// TODO: Implement your create logic here

		return CustomResourceCreateResponse.Builder.createSuccess(physicalResourceId).build();
	}

	@Override
	public CustomResourceUpdateResponse processUpdate(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties,
			CustomResourceRequestProperties customResourceRequestOldProperties) {

		// TODO: Implement your update logic here

		return CustomResourceUpdateResponse.Builder.createSuccess().build();
	}

	@Override
	public CustomResourceDeleteResponse processDelete(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties) {
		// TODO: Implement your delete logic here

		return CustomResourceDeleteResponse.Builder.createSuccess().build();
	}
}
```

