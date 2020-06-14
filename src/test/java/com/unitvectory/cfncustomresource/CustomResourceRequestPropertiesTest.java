package com.unitvectory.cfncustomresource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.json.JSONObject;
import org.junit.Test;

public class CustomResourceRequestPropertiesTest {

	@Test
	public void testParseString() throws Exception {
		String jsonString = "{\"foo\": \"bar\"}";
		JSONObject json = new JSONObject(jsonString);

		CustomResourceRequestProperties prop = new CustomResourceRequestProperties(json);

		assertEquals("bar", prop.getStringProperty("foo"));
		assertNull(prop.getStringProperty("null"));
	}
}
