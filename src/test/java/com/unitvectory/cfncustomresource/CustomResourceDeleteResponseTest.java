package com.unitvectory.cfncustomresource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CustomResourceDeleteResponseTest {

	@Test
	public void successTest() {
		CustomResourceDeleteResponse response = CustomResourceDeleteResponse.Builder.createSuccess().build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertNull(response.getResponseReason());
	}

	@Test
	public void errorTest() {
		CustomResourceDeleteResponse response = CustomResourceDeleteResponse.Builder.createError("failure").build();
		assertEquals(ResponseStatus.FAILED, response.getResponseStatus());
		assertEquals("failure", response.getResponseReason());
	}
}
