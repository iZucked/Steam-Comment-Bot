/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.List;
import java.util.Random;

/**
 * A class which adds some convenience methods to a Random
 * 
 * @author hinton
 * 
 */
public final class RandomHelper {

	private RandomHelper() {

	}

	/**
	 * Return an integer between from 0 to n-1 which is not equal to d, drawn from a uniform distribution.
	 * 
	 * @param n
	 * @param d
	 * @return
	 */
	public static final int nextDifferentInt(final Random random, final int n, final int d) {
		final int k = random.nextInt(n - 1);
		if (k >= d) {
			return k + 1;
		} else {
			return k;
		}
	}

	/**
	 * Uniformly randomly pick an element from a list
	 * 
	 * @param <T>
	 * @param collection
	 * @return
	 */
	public static final <T> T chooseElementFrom(final Random random, final List<T> collection) {
		return collection.get(random.nextInt(collection.size()));
	}

	/**
	 * Return a random integer uniformly distributed from min to max (inclusive).
	 * 
	 * @param random
	 * @param min
	 * @param max
	 * @return
	 */
	public static final int nextIntBetween(final Random random, final int min, final int max) {
		final int diff = max - min;
		if (diff == 0) {
			return min;
		}
		return min + random.nextInt(diff + 1);
	}
}
