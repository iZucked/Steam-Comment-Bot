/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONToXMLHelperTest {

	@Test
	public void testJsonToXML() {

		String jsonStr = "{\"name\":\"JSON\",\"integer\":1,\"double\":2.0,\"boolean\":true,\"nested\":{\"id\":42},\"array\":[1,2,3]}";

		String xmlStr = JSONToXMLHelper.convertToXml(jsonStr);

		System.out.println(xmlStr);

		Assertions.assertNotNull(xmlStr);
	}

	@Test
	public void testXMLToJson() {

		String originaljsonStr = "{\"name\":\"JSON\",\"integer\":1,\"double\":2.0,\"boolean\":true,\"nested\":{\"id\":42},\"array\":[1,2,3]}";

		String xmlStr = JSONToXMLHelper.convertToXml(originaljsonStr);
		
		String jsonStr = JSONToXMLHelper.convertToJson(xmlStr);

		System.out.println(jsonStr);

		Assertions.assertNotNull(jsonStr);
		
		// TODO: Maybe compare the original JSON to the new JSON ? (Note attribute order can change!)
	}
}
