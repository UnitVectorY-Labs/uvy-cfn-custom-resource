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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

class UvyJacksonHelper {

	public static final ObjectMapper MAPPER = new ObjectMapper();

	public static final ObjectWriter PRETTY_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

	public static String toJson(ObjectNode json) {
		try {
			return MAPPER.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	public static String toPrettyJson(ObjectNode json) {
		try {
			return PRETTY_WRITER.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
