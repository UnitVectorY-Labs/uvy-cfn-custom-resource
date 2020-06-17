uvy-cfn-custom-resource
=======================

Java library to make AWS Lambda function for CloudFormation custom resources

[![](https://jitci.com/gh/UnitVectorY-Labs/uvy-cfn-custom-resource/svg)](https://jitci.com/gh/UnitVectorY-Labs/uvy-cfn-custom-resource)

AWS CloudFormation provides for the creation of [custom resources](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources.html) to extend and enhance its capabilities.
These resources can be implemented using AWS Lambda as [Lambda-backed custom resources](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources-lambda.html).
AWS provides an official [cfn-response module](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-lambda-function-code-cfnresponsemodule.html) for Node.js implementations.
This library, uvy-cfn-custom-resource, provides an alternative for Java based Lambda functions through a streamlined interface. 

To implement a custom resource in Java, the `CustomResourceHandler` is extend and implemented so it can be used as the handler for a Lambda function.
The library takes care of parsing the request, forming the response, and sending the outcome to S3 for processing by CloudFormation.

The class provides three abstract methods for the custom resource the opportunity to handle the `processCreate`, `processUpdate`, and `processDelete` life cycle signals provided by CloudFormation with the most basic example shown below.

```java
public class ExampleHandler extends CustomResourceHandler {

	@Override
	public CustomResourceResponseCreate processCreate(String resourceType, String logicalResourceId, String stackId,
			CustomResourceRequestProperties customResourceRequestProperties) {

		String physicalResourceId = UUID.randomUUID().toString();

		// TODO: Implement your create logic here

		// Return success
		return CustomResourceResponseCreate.Builder.createSuccess(physicalResourceId).build();
	}

	@Override
	public CustomResourceResponseUpdate processUpdate(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties,
			CustomResourceRequestProperties customResourceRequestOldProperties) {

		// TODO: Implement your update logic here

		// Return success
		return CustomResourceResponseUpdate.Builder.createSuccess().build();
	}

	@Override
	public CustomResourceResponseDelete processDelete(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties) {
		// TODO: Implement your delete logic here

		// Return success
		return CustomResourceResponseDelete.Builder.createSuccess().build();
	}
}
```

For more advanced use cases the constructor for the `CustomResourceHandler` can accept a `CustomResourceConfig` which allows for overriding default behaviors.

 - `withPrintRequest` - Print the JSON request to the console for debugging.
 - `withPrintResponse` - Print the JSON response to the console for debugging.
 - `withCustomResourceOutcome` - Use a CustomResourceOutcome to override how the JSON file is uploaded to S3.

```java
	public ExampleHandler() {
		// CustomResourceConfig allows behaviors and implementation to be customized
		super(CustomResourceConfig.Builder.create().withPrintRequest().withPrintResponse()
				.withCustomResourceOutcome(new CustomResourceOutcome() {
					@Override
					public void putFile(String responseURL, String responseJson) {
						// TODO: Post the JSON to S3 URL
					}
				}).build());
	}
```


The create and update response builders allow for values to be returned as shown in the following example.
These values can be referenced in the CloudFormation stack and used as inputs for other resources.
If the values are sensitive and contain secrets `withNoEcho` should be called to prevent the values from being shown in the AWS console.

```java
public class ExampleHandler extends CustomResourceHandler {

	@Override
	public CustomResourceResponseCreate processCreate(String resourceType, String logicalResourceId, String stackId,
			CustomResourceRequestProperties customResourceRequestProperties) {
		// TODO: Implement your create logic here

		// Using withDataString returns value to be used in CloudFormation stack
		// Using withNoEcho prevents sensitive value from being displayed in console
		return CustomResourceResponseCreate.Builder.createSuccess("example").withDataString("foo", "bar").withNoEcho()
				.build();
	}

	@Override
	public CustomResourceResponseUpdate processUpdate(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties,
			CustomResourceRequestProperties customResourceRequestOldProperties) {
		// TODO: Implement your update logic here

		// Using withDataString returns value to be used in CloudFormation stack
		// Using withNoEcho prevents sensitive value from being displayed in console
		return CustomResourceResponseUpdate.Builder.createSuccess().withDataString("bar", "foo").withNoEcho().build();
	}

	@Override
	public CustomResourceResponseDelete processDelete(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties) {
		// TODO: Implement your delete logic here

		return CustomResourceResponseDelete.Builder.createSuccess().build();
	}
}
```

In the case where the custom resources fails to be created, updated, or deleted, an error must be returned to CloudFormation.
Instead of allowing an unhandled exception to cause the processing to fail, a properly formatted error message should be returned as shown in the following example.
When an error is returned the withDataString and withNoEcho cannot be used.

```java
public class ExampleHandler extends CustomResourceHandler {

	@Override
	public CustomResourceResponseCreate processCreate(String resourceType, String logicalResourceId, String stackId,
			CustomResourceRequestProperties customResourceRequestProperties) {

		// Example error
		return CustomResourceResponseCreate.Builder
				.createError("example", "reason why it failed returned to CloudFormation").build();
	}

	@Override
	public CustomResourceResponseUpdate processUpdate(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties,
			CustomResourceRequestProperties customResourceRequestOldProperties) {

		// Example error
		return CustomResourceResponseUpdate.Builder.createError("reason why it failed returned to CloudFormation")
				.build();
	}

	@Override
	public CustomResourceResponseDelete processDelete(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties) {

		// Example error
		return CustomResourceResponseDelete.Builder.createError("reason why it failed returned to CloudFormation")
				.build();
	}
}
```

Dependencies
------------

This library is intended to be light weight, but it uses some libraries itself to accomplish the task.

 - [com.amazonaws.aws-lambda-java-core](https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-core) for the Lambda handler
 - [org.json.json](https://mvnrepository.com/artifact/org.json/json) for parsing and constructing JSON
