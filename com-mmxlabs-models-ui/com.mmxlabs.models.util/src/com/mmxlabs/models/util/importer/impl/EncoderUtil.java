/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import org.eclipse.jdt.annotation.NonNull;

public final class EncoderUtil {

	public static final String HTML_AMPERSAND = "&#38;";
	public static final String HTML_COMMA = "&#44;";

	public static @NonNull String encode(@NonNull String input) {
		return input.trim() //
				// Do first as control character
				.replaceAll("&", HTML_AMPERSAND) //
				.replaceAll(",", HTML_COMMA) //
		;
	}

	public static @NonNull String decode(@NonNull String input) {
		return input.trim() //
				.replaceAll(HTML_COMMA, ",") //
				// Do last as control character
				.replaceAll(HTML_AMPERSAND, "&") //
		;
	}
}
