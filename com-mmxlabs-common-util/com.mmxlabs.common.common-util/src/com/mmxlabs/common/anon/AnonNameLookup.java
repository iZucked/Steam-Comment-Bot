/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.anon;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AnonNameLookup {
	private static final String HTML_AT = "&#64;";
	private static final String MARKER = "@@";

	private AnonNameLookup() {

	}

	public static String generateFor(final JSONObject obj) {
		return generateFor(obj.toJSONString());
	}

	public static String generateFor(final String str) {
		return addMarkers(encodeStr(str));
	}

	public static String generateFor(final String type, final String name) {
		final JSONObject obj = new JSONObject();
		obj.put("type", type);
		obj.put("name", name);

		return generateFor(obj);
	}

	public static String addMarkers(String str) {
		return MARKER + str + MARKER;
	}

	public static String encodeStr(String str) {
		return str.replace("@", HTML_AT);
	}

	public static String decodeStr(String str) {
		return str.replace(HTML_AT, "@");
	}

	public static boolean containsMarker(String source) {
		return source.contains(MARKER);
	}

	public static String decode(String source, Function<String, String> decoder) {

		Matcher m = Pattern.compile("@@([^@.]*)@@").matcher(source);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, decoder.apply(m.group(1)));
		}
		m.appendTail(sb);

		return sb.toString();

	}

	public static void main(String[] args) {
		String s = "hello my name is @@{\"name\":\"loopy &#64;k\",\"type\":\"bouncer\"}@@ and  @@ kkkkkk@@@";

		String decoded = decode(s, (a) -> {
			try {
				JSONObject obj;
				String abc = decodeStr(a);
				obj = (JSONObject) new JSONParser().parse(abc);
				return obj.get("name").toString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				return a;
			}
		});
		System.out.println(decoded);
	}
}
