package com.unitvectory.cfncustomresource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CustomResourceResponseCreateTest {

	@Test
	public void successTest() {
		CustomResourceResponseCreate response = CustomResourceResponseCreate.Builder.createSuccess("foo").build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertEquals("foo", response.getResponsePhysicalResourceId());
		assertNull(response.getResponseReason());
		assertNull(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

	@Test
	public void successDataTest() {
		CustomResourceResponseCreate response = CustomResourceResponseCreate.Builder.createSuccess("foo")
				.withDataString("a", "1").withDataString("b", "2").build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertEquals("foo", response.getResponsePhysicalResourceId());
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
		CustomResourceResponseCreate response = CustomResourceResponseCreate.Builder.createSuccess("foo").withNoEcho()
				.build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertEquals("foo", response.getResponsePhysicalResourceId());
		assertNull(response.getResponseReason());
		assertTrue(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

	@Test
	public void errorTest() {
		CustomResourceResponseCreate response = CustomResourceResponseCreate.Builder.createError("foo", "failure")
				.build();
		assertEquals(ResponseStatus.FAILED, response.getResponseStatus());
		assertEquals("foo", response.getResponsePhysicalResourceId());
		assertEquals("failure", response.getResponseReason());
		assertNull(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}
}
