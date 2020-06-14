package com.unitvectory.cfncustomresource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CustomResourceResponseDeleteTest {

	@Test
	public void successTest() {
		CustomResourceResponseDelete response = CustomResourceResponseDelete.Builder.createSuccess().build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertNull(response.getResponseReason());
	}

	@Test
	public void errorTest() {
		CustomResourceResponseDelete response = CustomResourceResponseDelete.Builder.createError("failure").build();
		assertEquals(ResponseStatus.FAILED, response.getResponseStatus());
		assertEquals("failure", response.getResponseReason());
	}
}
