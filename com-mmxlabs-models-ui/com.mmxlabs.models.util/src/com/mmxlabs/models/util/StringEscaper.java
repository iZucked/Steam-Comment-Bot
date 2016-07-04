/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util;

/**
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Simon Goodall
 * 
 */
public final class StringEscaper {

	private StringEscaper() {
	}

	/**
	 * Escape control characters for display in the UI. Currently this is just ampersand.
	 * 
	 * @param str
	 * @return
	 */
	public static final String escapeUIString(final String input) {
		// Escape ampersands
		String str = input;
		if (str != null) {
			str = str.replaceAll("&", "&&");
		}

		return str;
	}
}
