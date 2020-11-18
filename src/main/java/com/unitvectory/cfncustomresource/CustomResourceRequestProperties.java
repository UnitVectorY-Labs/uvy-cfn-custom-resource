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

import java.util.Collections;
import java.util.Set;

import org.json.JSONObject;

/**
 * Provides access to the custom resource properties.
 *
 */
public class CustomResourceRequestProperties {

	private JSONObject resourceProperties;

	CustomResourceRequestProperties() {
		this.resourceProperties = null;
	}

	CustomResourceRequestProperties(JSONObject resourceProperties) {
		this.resourceProperties = resourceProperties;
	}

	final Set<String> getKeys() {
		return Collections.unmodifiableSet(this.resourceProperties.keySet());
	}

	/**
	 * Get a string property by name.
	 * 
	 * @param key the property key
	 * @return the property value; null if not set
	 */
	public final String getStringProperty(String key) {
		if (this.resourceProperties == null) {
			return null;
		}

		if (!this.resourceProperties.has(key)) {
			return null;
		}

		return this.resourceProperties.getString(key);
	}
}
