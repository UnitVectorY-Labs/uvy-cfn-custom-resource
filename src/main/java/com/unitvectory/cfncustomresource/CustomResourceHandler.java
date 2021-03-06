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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public abstract class CustomResourceHandler implements RequestStreamHandler {

	private final CustomResourceConfig customResourceConfig;

	/**
	 * Creates a new instance of the CustomResourceHandler
	 */
	public CustomResourceHandler() {
		this.customResourceConfig = CustomResourceConfig.Builder.create().build();
	}

	/**
	 * Creates a new instance of the CustomResourceHandler
	 * 
	 * This is intended to be overridden by an implementing constructor that takes
	 * no parameters as the super construct to allow behaviors to be configured.
	 * 
	 * @param customResourceConfig the CustomResourceConfig; must not be null
	 */
	public CustomResourceHandler(CustomResourceConfig customResourceConfig) {
		if (customResourceConfig == null) {
			throw new IllegalArgumentException("customResourceConfig must not be null");
		}

		this.customResourceConfig = customResourceConfig;
	}

	public final void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
			throws IOException {

		// Read InputStream into the JSONObject
		JSONObject inputNode = new JSONObject(new JSONTokener(inputStream));

		if (this.customResourceConfig.isPrintRequest()) {
			System.out.println(inputNode.toString());
		}

		// The request type is set by the AWS CloudFormation stack operation
		// (create-stack, update-stack, or delete-stack) that was initiated by the
		// template developer for the stack that contains the custom resource.
		final RequestType requestType = RequestType.valueOf(inputNode.optString("RequestType", null));

		// The response URL identifies a presigned S3 bucket that receives responses
		// from the custom resource provider to AWS CloudFormation.
		final String responseURL = inputNode.optString("ResponseURL", null);

		// The Amazon Resource Name (ARN) that identifies the stack that contains the
		// custom resource.
		// Combining the StackId with the RequestId forms a value that you can use to
		// uniquely identify a request on a particular custom resource.
		final String stackId = inputNode.optString("StackId", null);

		// A unique ID for the request.
		// Combining the StackId with the RequestId forms a value that you can use to
		// uniquely identify a request on a particular custom resource.
		final String requestId = inputNode.optString("RequestId", null);

		// The template developer-chosen resource type of the custom resource in the AWS
		// CloudFormation template. Custom resource type names can be up to 60
		// characters long and can include alphanumeric and the following characters:
		// _@-.
		final String resourceType = inputNode.optString("ResourceType", null);

		// The template developer-chosen name (logical ID) of the custom resource in the
		// AWS CloudFormation template. This is provided to facilitate communication
		// between the custom resource provider and the template developer.
		final String logicalResourceId = inputNode.optString("LogicalResourceId", null);

		// A required custom resource provider-defined physical ID that is unique for
		// that provider.
		// Required: Always sent with Update and Delete requests; never sent with
		// Create.
		final String physicalResourceId = inputNode.optString("PhysicalResourceId", null);

		// This field contains the contents of the Properties object sent by the
		// template developer. Its contents are defined by the custom resource provider.
		// ResourceProperties (object)
		final CustomResourceRequestProperties customResourceRequestProperties;
		if (inputNode.has("ResourceProperties")) {
			JSONObject resourceProperties = inputNode.getJSONObject("ResourceProperties");
			customResourceRequestProperties = new CustomResourceRequestProperties(resourceProperties);
		} else {
			customResourceRequestProperties = new CustomResourceRequestProperties();
		}

		// Used only for Update requests. Contains the resource properties that were
		// declared previous to the update request.
		// OldResourceProperties (object)
		final CustomResourceRequestProperties customResourceRequestOldProperties;
		if (inputNode.has("OldResourceProperties")) {
			JSONObject oldResourceProperties = inputNode.getJSONObject("OldResourceProperties");
			customResourceRequestOldProperties = new CustomResourceRequestProperties(oldResourceProperties);
		} else {
			customResourceRequestOldProperties = new CustomResourceRequestProperties();
		}

		// Process

		final ResponseStatus responseStatus;
		final String responseReason;
		final String responsePhysicalResourceId;
		final Boolean responseNoEcho;
		final Map<String, String> responseDataString;

		Exception unexpectedException = null;

		if (RequestType.Create.equals(requestType)) {
			// Perform the actual request

			CustomResourceResponseCreate response = null;
			boolean exception = false;
			try {
				response = this.processCreate(resourceType, logicalResourceId, stackId,
						customResourceRequestProperties);
			} catch (Exception e) {
				unexpectedException = e;
				response = null;
				exception = true;
			}

			if (response != null) {
				responseStatus = response.getResponseStatus();
				responseReason = response.getResponseReason();
				responsePhysicalResourceId = response.getResponsePhysicalResourceId();
				responseNoEcho = response.getResponseNoEcho();
				responseDataString = response.getDataString();
			} else {
				// Failed hard on creation
				responseStatus = ResponseStatus.FAILED;
				if (exception) {
					responseReason = "Failed with unexpected exception";
				} else {
					responseReason = "Failed with no reason";
				}

				responsePhysicalResourceId = UUID.randomUUID().toString();
				responseNoEcho = false;
				responseDataString = null;
			}

		} else if (RequestType.Update.equals(requestType)) {

			CustomResourceResponseUpdate response = null;
			boolean exception = false;
			try {
				response = processUpdate(physicalResourceId, resourceType, logicalResourceId, stackId,
						customResourceRequestProperties, customResourceRequestOldProperties);
			} catch (Exception e) {
				unexpectedException = e;
				response = null;
				exception = true;
			}

			if (response != null) {
				responseStatus = response.getResponseStatus();
				responseReason = response.getResponseReason();
				responsePhysicalResourceId = physicalResourceId;
				responseNoEcho = response.getResponseNoEcho();
				responseDataString = response.getDataString();
			} else {
				responseStatus = ResponseStatus.FAILED;
				if (exception) {
					responseReason = "Failed with unexpected exception";
				} else {
					responseReason = "Failed with no reason";
				}

				responsePhysicalResourceId = physicalResourceId;
				responseNoEcho = false;
				responseDataString = null;
			}

		} else if (RequestType.Delete.equals(requestType)) {

			CustomResourceResponseDelete response = null;
			boolean exception = false;
			try {
				response = processDelete(physicalResourceId, resourceType, logicalResourceId, stackId,
						customResourceRequestProperties);
			} catch (Exception e) {
				unexpectedException = e;
				response = null;
				exception = true;
			}

			if (response != null) {
				responseStatus = response.getResponseStatus();
				responseReason = response.getResponseReason();
				responsePhysicalResourceId = physicalResourceId;
				responseNoEcho = null;
				responseDataString = null;
			} else {
				responseStatus = ResponseStatus.FAILED;
				if (exception) {
					responseReason = "Failed with unexpected exception";
				} else {
					responseReason = "Failed with no reason";
				}

				responsePhysicalResourceId = physicalResourceId;
				responseNoEcho = false;
				responseDataString = null;
			}
		} else {

			responseStatus = ResponseStatus.FAILED;
			responseReason = "Unknown request type";
			responsePhysicalResourceId = physicalResourceId;
			responseNoEcho = null;
			responseDataString = null;
		}

		// Build the response

		JSONObject response = new JSONObject();

		// The status value sent by the custom resource provider in response to an AWS
		// CloudFormation-generated request.
		// Must be either SUCCESS or FAILED.
		// Required: Yes
		// Type: String
		response.put("Status", responseStatus.name());

		// Describes the reason for a failure response.
		// Required: Required if Status is FAILED. It's optional otherwise.
		// Type: String
		if (ResponseStatus.FAILED.equals(responseStatus)) {
			if (responseReason == null || responseReason.trim().length() == 0) {
				response.put("Reason", "Unknown reason");
			} else {
				response.put("Reason", responseReason);
			}
		}

		// This value should be an identifier unique to the custom resource vendor, and
		// can be up to 1 Kb in size. The value must be a non-empty string and must be
		// identical for all responses for the same resource.
		// Required: Yes
		// Type: String
		if (RequestType.Create.equals(requestType)) {
			response.put("PhysicalResourceId", responsePhysicalResourceId);
		} else if (RequestType.Update.equals(requestType) || RequestType.Delete.equals(requestType)) {
			response.put("PhysicalResourceId", physicalResourceId);
		}

		// The Amazon Resource Name (ARN) that identifies the stack that contains the
		// custom resource. This response value should be copied verbatim from the
		// request.
		// Required: Yes
		// Type: String
		response.put("StackId", stackId);

		// A unique ID for the request. This response value should be copied verbatim
		// from the request.
		// Required: Yes
		// Type: String
		response.put("RequestId", requestId);

		// The template developer-chosen name (logical ID) of the custom resource in the
		// AWS CloudFormation template. This response value should be copied verbatim
		// from the request.
		// Required: Yes
		// Type: String
		response.put("LogicalResourceId", logicalResourceId);

		// Optional. Indicates whether to mask the output of the custom resource when
		// retrieved by using the Fn::GetAtt function. If set to true, all returned
		// values are masked with asterisks (*****). The default value is false.
		// For more information about using NoEcho to mask sensitive information, see
		// the Do Not Embed Credentials in Your Templates best practice.
		// Required: No
		// Type: Boolean
		if (responseNoEcho != null) {
			response.put("NoEcho", responseNoEcho.booleanValue());
		}

		// Optional. The custom resource provider-defined name-value pairs to send with
		// the response. You can access the values provided here by name in the template
		// with Fn::GetAtt.
		// Important: If the name-value pairs contain sensitive information, you should
		// use the NoEcho field to mask the output of the custom resource. Otherwise,
		// the values are visible through APIs that surface property values (such as
		// DescribeStackEvents).
		// Required: No
		// Type: JSON object
		if (responseDataString != null && responseDataString.size() > 0) {
			JSONObject data = new JSONObject();

			for (Entry<String, String> entry : responseDataString.entrySet()) {
				data.put(entry.getKey(), entry.getValue());
			}

			response.put("Data", data);
		}

		// There was an unexpected exception, log it
		if (unexpectedException != null) {

			// TODO: Log the error
		}

		// Write the response JSON to S3
		String json = response.toString();

		if (this.customResourceConfig.isPrintResponse()) {
			System.out.println(json);
		}

		this.customResourceConfig.getCustomResourceOutcome().putFile(responseURL, json);
	}

	/**
	 * Process the Create signal from CloudFormation.
	 * 
	 * @param resourceType
	 * @param logicalResourceId
	 * @param stackId
	 * @param customResourceRequestProperties
	 * @return the response
	 */
	public abstract CustomResourceResponseCreate processCreate(String resourceType, String logicalResourceId,
			String stackId, CustomResourceRequestProperties customResourceRequestProperties);

	/**
	 * Process the Update signal from CloudFormation.
	 * 
	 * @param physicalResourceId
	 * @param resourceType
	 * @param logicalResourceId
	 * @param stackId
	 * @param customResourceRequestProperties
	 * @param customResourceRequestOldProperties
	 * @return the response
	 */
	public abstract CustomResourceResponseUpdate processUpdate(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties,
			CustomResourceRequestProperties customResourceRequestOldProperties);

	/**
	 * Process the Delete signal from CloudFormation.
	 * 
	 * @param physicalResourceId
	 * @param resourceType
	 * @param logicalResourceId
	 * @param stackId
	 * @param customResourceRequestProperties
	 * @return the response
	 */
	public abstract CustomResourceResponseDelete processDelete(String physicalResourceId, String resourceType,
			String logicalResourceId, String stackId, CustomResourceRequestProperties customResourceRequestProperties);
}