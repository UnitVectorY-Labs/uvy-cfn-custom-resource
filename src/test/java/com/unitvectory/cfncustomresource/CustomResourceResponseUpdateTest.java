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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CustomResourceResponseUpdateTest {

	@Test
	public void successTest() {
		CustomResourceResponseUpdate response = CustomResourceResponseUpdate.Builder.createSuccess().build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertNull(response.getResponseReason());
		assertNull(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

	@Test
	public void successDataTest() {
		CustomResourceResponseUpdate response = CustomResourceResponseUpdate.Builder.createSuccess()
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
		CustomResourceResponseUpdate response = CustomResourceResponseUpdate.Builder.createSuccess().withNoEcho()
				.build();
		assertEquals(ResponseStatus.SUCCESS, response.getResponseStatus());
		assertNull(response.getResponseReason());
		assertTrue(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

	@Test
	public void errorTest() {
		CustomResourceResponseUpdate response = CustomResourceResponseUpdate.Builder.createError("failure").build();
		assertEquals(ResponseStatus.FAILED, response.getResponseStatus());
		assertEquals("failure", response.getResponseReason());
		assertNull(response.getResponseNoEcho());
		assertNotNull(response.getDataString());
		assertEquals(0, response.getDataString().size());
	}

}
