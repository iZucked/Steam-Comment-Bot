package com.mmxlabs.common.util;
@FunctionalInterface
	public interface CheckedFunction<T, R, E extends Exception> {

		/**
		 * Applies this function to the given argument.
		 *
		 * @param t
		 *            the function argument
		 * @return the function result
		 */
		R apply(T t) throws E;
	}
