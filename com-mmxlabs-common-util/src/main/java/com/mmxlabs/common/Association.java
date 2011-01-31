/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.HashMap;

/**
 * A two-way association from one type to another. Internally maintains two
 * hashmaps for lookup in both directions.
 * 
 * @author hinton
 * 
 * @param <A>
 * @param <B>
 */
public class Association<A, B> {
	
	protected final HashMap<A, B> forwards;
	protected final HashMap<B, A> reverse;

	public Association() {
		forwards = new HashMap<A, B>();
		reverse = new HashMap<B, A>();
	}

	public void add(final A a, final B b) {
		forwards.put(a, b);
		reverse.put(b, a);
	}

	public B lookup(final A a) {
		return forwards.get(a);
	}

	public A reverseLookup(final B b) {
		return reverse.get(b);
	}
}
