uvy-cfn-custom-resource
=======================

Java library to make AWS Lambda function for CloudFormation custom resources

[![](https://jitci.com/gh/UnitVectorY-Labs/uvy-cfn-custom-resource/svg)](https://jitci.com/gh/UnitVectorY-Labs/uvy-cfn-custom-resource)

AWS CloudFormation provides for the creation of [custom resources](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources.html) to extend and enhance the capabilities of CloudFormation.
These resources can be implemented using AWS Lambda as [Lambda-backed custom resources](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources-lambda.html).
AWS provides an official [cfn-response module](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-lambda-function-code-cfnresponsemodule.html) for Node.js implementations.
This library, uvy-cfn-custom-resource, provides an alternative for Java based Lambda functions through a streamlined interface. 

A custom resource can be used to provide dynamic values to a CloudFormation template that can be looked up from external resources or programmatically calculated.
A custom resource can also provision or manage resources outside of AWS from third parties or your own applications.
Alternatively, the custom resource can call AWS APIs to provision AWS resources that do not support CloudFormation or do not support certain actions with CloudFormation.

To implement a custom resource, the `CustomResourceHandler` is extend and implemented so it can be used as the handler for a Lambda function.
The class provides three abstract methods for the custom resource the opportunity to handle the `processCreate`, `processUpdate`, and `processDelete` life cycle signals provided by CloudFormation.
Builders are provided to construct responses simplifying the implementation.
The library takes care of parsing the request, forming the response, and sending the outcome to S3 for processing by CloudFormation.

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

Dependencies
------------

This library is intended to be light weight, but it uses some libraries itself to accomplish the task.

 - [com.amazonaws.aws-lambda-java-core](https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-core) for the Lambda handler
 - [org.json.json](https://mvnrepository.com/artifact/org.json/json) for parsing and constructing JSON

