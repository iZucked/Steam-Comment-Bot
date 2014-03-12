/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.HashMap;

/**
 * A two-way association from one type to another. Internally maintains two {@link HashMap}s for lookup in both directions. Null values are not permitted.
 * 
 * @author hinton
 * 
 * @param <A>
 * @param <B>
 */
public final class Association<A, B> {

	protected final HashMap<A, B> forwards;
	protected final HashMap<B, A> reverse;

	public Association() {
		forwards = new HashMap<A, B>();
		reverse = new HashMap<B, A>();
	}

	/**
	 * Add an association between A and B
	 * 
	 * @param a
	 * @param b
	 */
	public void add(final A a, final B b) {
		forwards.put(a, b);
		reverse.put(b, a);
	}

	/**
	 * Lookup the association for A. Returns null is none is found.
	 * 
	 * @param a
	 * @return
	 */
	public B lookup(final A a) {
		return forwards.get(a);
	}

	/**
	 * Lookup the reverse association for B. Returns null is none is found.
	 * 
	 * @param a
	 * @return
	 */
	public A reverseLookup(final B b) {
		return reverse.get(b);
	}

	/**
	 * Clear any existing associations
	 */
	public void clear() {
		forwards.clear();
		reverse.clear();
	}
}
