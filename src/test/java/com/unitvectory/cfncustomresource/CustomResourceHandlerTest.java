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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;

@RunWith(Parameterized.class)
public class CustomResourceHandlerTest {

	private final CustomResourceOutcomeTestClient customResourceOutcomeTestClient;

	private final CustomResourceHandlerMock customResourceHandler;

	private final String filePath;

	public CustomResourceHandlerTest(String filePath) {
		this.customResourceOutcomeTestClient = new CustomResourceOutcomeTestClient();
		this.customResourceHandler = new CustomResourceHandlerMock(this.customResourceOutcomeTestClient);
		this.filePath = filePath;
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> data() {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("tests");
		String path = url.getPath();

		List<Object[]> data = new ArrayList<Object[]>();
		for (File file : new File(path).listFiles()) {
			if (file.isDirectory()) {
				continue;
			}

			data.add(new Object[] { file.getAbsolutePath() });
		}

		return data;
	}

	@Test
	public void testProcess() throws Exception {

		File testFile = new File(this.filePath);
		assertTrue("test file does not exist", testFile.exists());

		String testJsonString = FileUtils.readFileToString(testFile, StandardCharsets.UTF_8);
		assertNotNull("test file was null", testJsonString);

		JSONObject testJson = new JSONObject(testJsonString);

		if (testJson.optBoolean("failed", false)) {
			this.customResourceHandler.setFail();
		}

		if (testJson.optBoolean("noEcho", false)) {
			this.customResourceHandler.setNoEcho();
		}

		assertTrue("test file must have \"input\" object", testJson.has("input"));
		JSONObject input = testJson.getJSONObject("input");

		String inputResponseURL = input.optString("ResponseURL", null);

		assertTrue("test file must have \"expectedOutput\" object", testJson.has("expectedOutput"));
		JSONObject expectedOutput = testJson.getJSONObject("expectedOutput");

		InputStream inputStream = new ByteArrayInputStream(input.toString().getBytes(StandardCharsets.UTF_8));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.customResourceHandler.handleRequest(inputStream, outputStream, null);

		String outputString = customResourceOutcomeTestClient.getResponseJson();

		String outputResponseURL = customResourceOutcomeTestClient.getResponseURL();

		JSONAssert.assertEquals(expectedOutput.toString(), outputString, true);
		assertEquals(inputResponseURL, outputResponseURL);
	}
}
