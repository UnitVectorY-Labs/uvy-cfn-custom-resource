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
