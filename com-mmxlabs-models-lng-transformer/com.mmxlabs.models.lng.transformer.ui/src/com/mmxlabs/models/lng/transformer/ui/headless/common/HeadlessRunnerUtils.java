/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HeadlessRunnerUtils {

	/**
	 * Checks if a field name (in the JSON output structure) is actually a valid
	 * BSON fieldname.
	 * 
	 * @param name
	 * @return
	 */
	private static boolean isValidBsonFieldname(final String name) {
		return name.matches("^[_0-9a-zA-Z\\-]+$");
	}

	/**
	 * Converts a field name into a valid BSON fieldname, if necessary.
	 * 
	 * @param name
	 * @return
	 */
	private static String makeValidBsonFieldname(final String name) {
		// force only alphanumeric characters, underscore & hyphen
		String result = name.replaceAll("[^_a-zA-Z0-9\\-]", "_");

		// don't allow an empty string
		if (result.equals("")) {
			result = "_";
		}

		return result;

	}

	/**
	 * Renames fields that have BSON-invalid names (e.g. containing whitespace)
	 * 
	 * @param object A JSONObject
	 */
	private static void renameInvalidBsonFields(final JSONObject object) {
		if (object == null) {
			return;
		}

		final Set keys = object.keySet();

		final List<Object> badNames = new LinkedList<>();

		for (final Object key : keys) {
			final String name = key.toString();
			final Object value = object.get(key);

			if (!isValidBsonFieldname(name)) {
				badNames.add(key);
			}

			if (value instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) value);
			} else if (value instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) value);
			}
		}

		for (final Object key : badNames) {
			final String name = key.toString();
			final String newName = makeValidBsonFieldname(name);
			if (object.containsKey(newName)) {
				throw new RuntimeException("Tried to ".format("Tried to rename invalid BSON name '%s' to '%s' but encountered a name collision.", name, newName));
			} else {
				object.put(newName, object.get(key));
				object.remove(key);
			}

		}

	}

	/**
	 * Renames fields that have BSON-invalid names (e.g. containing whitespace)
	 * 
	 * @param object A JSONArray of objects
	 */
	public static void renameInvalidBsonFields(final JSONArray object) {
		for (final Object child : object) {
			if (child instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) child);
			} else if (child instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) child);
			}
		}

	}
}
