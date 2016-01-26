/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

/**
 * Handy methods for editor parts
 * 
 * @author Tom Hinton
 * 
 */
public class EditorUtils {
	public static String unmangle(final String name) {
		final StringBuilder sb = new StringBuilder();
		boolean lastWasLower = false;
		boolean lastLastWasLower = false;
		boolean firstChar = true;
		for (final char c : name.toCharArray()) {
			if (firstChar) {
				sb.append(Character.toUpperCase(c));
				lastWasLower = Character.isLowerCase(c);
				firstChar = false;
			} else {
				if (lastWasLower && Character.isUpperCase(c)) {
					sb.append(" ");
				} else if (!lastLastWasLower && !lastWasLower && !Character.isUpperCase(c)) {
					sb.insert(sb.length() - 1, " ");
				}
				lastLastWasLower = lastWasLower;
				lastWasLower = Character.isLowerCase(c);
				sb.append(c);
			}
		}
		return sb.toString().trim();
	}
}
