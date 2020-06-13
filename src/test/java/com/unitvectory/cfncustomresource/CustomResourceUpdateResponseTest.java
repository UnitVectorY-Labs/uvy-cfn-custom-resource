package com.unitvectory.cfncustomresource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CustomResourceUpdateResponseTest {

	@Test
	public void successTest() {
		CustomResourceUpdateResponse response = CustomResourceUpdateResponse.Builder.createSuccess().build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertNull(response.getResponseReason());
		assertNull(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

	@Test
	public void successDataTest() {
		CustomResourceUpdateResponse response = CustomResourceUpdateResponse.Builder.createSuccess()
				.withDataString("a", "1").withDataString("b", "2").build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertNull(response.getResponseReason());
		assertNull(response.getResponseNoEcho());
		assertEquals(2, response.getDataString().size());
		assertNotNull(response.getDataString());
		assertEquals("1", response.getDataString().get("a"));
		assertEquals("2", response.getDataString().get("b"));
		assertNull(response.getDataString().get("c"));
	}

	@Test
	public void successNoEchoTest() {
		CustomResourceUpdateResponse response = CustomResourceUpdateResponse.Builder.createSuccess().withNoEcho()
				.build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertNull(response.getResponseReason());
		assertTrue(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

	@Test
	public void errorTest() {
		CustomResourceUpdateResponse response = CustomResourceUpdateResponse.Builder.createError("failure").build();
		assertEquals(ResponseStatus.FAILED, response.getResponseStatus());
		assertEquals("failure", response.getResponseReason());
		assertNull(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

}
