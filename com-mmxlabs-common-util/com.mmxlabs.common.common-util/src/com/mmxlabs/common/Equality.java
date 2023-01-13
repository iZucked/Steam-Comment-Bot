/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.Arrays;

/**
 * Utility class to help with equality checks.
 * 
 * @author Simon Goodall
 * 
 */
public final class Equality {

	private Equality() {

	}

	/**
	 * Shallow equality test for object arrays; checks whether two object arrays
	 * contain identical references (it is true iff (a) both arrays have the same
	 * length and (b) a[i] == b[i] for all valid i). This differs to
	 * {@link Arrays#equals(Object[], Object[])} in that is uses the == operator
	 * rather than {@link Object#equals(Object)} to compare objects.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static final boolean shallowEquals(final Object[] a, final Object[] b) {
		if (a == b) {
			return true;
		}
		if ((a == null) || (b == null) || (a.length != b.length)) {
			return false;
		}
		for (int x = 0; x < a.length; x++) {
			if (a[x] != b[x]) {
				return false;
			}
		}
		return true;
	}
}
