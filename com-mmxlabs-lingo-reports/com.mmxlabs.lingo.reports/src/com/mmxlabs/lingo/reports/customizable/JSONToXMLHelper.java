/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import org.json.JSONObject;
import org.json.XML;

public class JSONToXMLHelper {

	private JSONToXMLHelper() {

	}

	public static String convertToXml(final String jsonStr) {
		final JSONObject json = new JSONObject(jsonStr);
		return XML.toString(json);
	}

	public static String convertToJson(final String xmlStr) {
		return XML.toJSONObject(xmlStr).toString();
	}
}
