/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.common;

import java.util.HashMap;

/**
 * A two-way association from one type to another. Internally maintains two hashmaps for lookup in both directions.
 * @author hinton
 *
 * @param <A>
 * @param <B>
 */
public class Association<A, B> {
	protected HashMap<A, B> forwards;
	protected HashMap<B, A>reverse;
	public Association() {
		forwards = new HashMap<A, B>();
		reverse = new HashMap<B, A>();
	}
	
	public void add(A a, B b) {
		forwards.put(a, b);
		reverse.put(b, a);
	}
	
	public B lookup(A a) {
		return forwards.get(a);
	}
	
	public A reverseLookup(B b) {
		return reverse.get(b);
	}
}
