package com.mmxlabs.lingo.reports.customizable;

import org.json.JSONObject;
import org.json.XML;

public class JSONToXMLHelper {

	public static String convertToXml(final String jsonStr) {
		JSONObject json = new JSONObject(jsonStr);
		String xml = XML.toString(json);
		return xml;
	}

	public static String convertToJson(final String xmlStr) {
		return XML.toJSONObject(xmlStr).toString();
	}
}
