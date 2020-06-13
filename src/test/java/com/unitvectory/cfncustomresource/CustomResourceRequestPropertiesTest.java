package com.unitvectory.cfncustomresource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class CustomResourceRequestPropertiesTest {

	@Test
	public void testParseString() throws Exception {
		String jsonString = "{\"foo\": \"bar\"}";
		JsonNode json = UvyJacksonHelper.MAPPER.readTree(jsonString);

		CustomResourceRequestProperties prop = new CustomResourceRequestProperties(json);

		assertEquals("bar", prop.getStringProperty("foo"));
		assertNull(prop.getStringProperty("null"));
	}
}
