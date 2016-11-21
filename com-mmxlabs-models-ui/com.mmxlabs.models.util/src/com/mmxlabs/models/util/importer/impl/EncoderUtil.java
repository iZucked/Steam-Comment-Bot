/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.NamedObject;

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

	public static String getTypedName(@NonNull final NamedObject namedObject) {

		final String name = namedObject.getName();
		if (name != null && !name.isEmpty()) {
			final EClass eClass = namedObject.eClass();
			final EAnnotation annotation = eClass.getEAnnotation("http://www.mmxlabs.com/models/csv");
			if (annotation != null) {
				final String namePrefix = annotation.getDetails().get("namePrefix");
				if (namePrefix != null) {
					return String.format("%s:%s", namePrefix, name);
				}
			}
		}
		return name;
	}
}
