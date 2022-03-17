/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONConverter {

	private JSONConverter() {

	}

	public static Map<String, Object> jsonToMap(final JSONObject json) {
		Map<String, Object> retMap = new HashMap<>();

		if (json.isEmpty()) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static Map<String, Object> toMap(final JSONObject object) {
		final Map<String, Object> map = new HashMap<>();

		final Iterator<?> keysItr = object.keySet().iterator();
		while (keysItr.hasNext()) {
			final String key = (String) keysItr.next();
			Object value = object.get(key);

			if (value instanceof final JSONObject jObj) {
				value = toMap(jObj);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(final JSONArray array) {
		final List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			Object value = array.get(i);
			if (value instanceof final JSONArray jArr) {
				value = toList(jArr);
			} else if (value instanceof final JSONObject jObj) {
				value = toMap(jObj);
			}
			list.add(value);
		}
		return list;
	}
}
