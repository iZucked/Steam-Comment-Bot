package com.mmxlabs.common;


/**
 * Utility class to help with equality checks.
 * 
 * @author Simon Goodall
 * 
 */
public final class Equality {

	/**
	 * Compare two object using their {@link #equals(Object)} method. However
	 * check for <code>null</code>. Returns true if both object are null or
	 * {@link #equals(Object)} return true.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual(final Object a, final Object b) {

		if (a == b) {
			return true;
		}

		if (a == null || b == null) {
			return false;
		}

		// Null check over, fall back to proper equals method
		return a.equals(b);
	}
}
