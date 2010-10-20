/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.common;

/**
 * A class for holding a pair of heterogenous items
 * @author hinton
 *
 * @param <A> the type of the first item
 * @param <B> the type of the second item
 */
public class Pair<A, B> {
	private A first;
	private B second;
	
	public Pair() {
		this(null, null);
	}
	
	public Pair(A first, B second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	public Pair(Pair<A, B> copy) {
		setBoth(copy.getFirst(), copy.getSecond());
	}

	public A getFirst() {
		return first;
	}
	public void setFirst(A first) {
		this.first = first;
	}
	public B getSecond() {
		return second;
	}
	public void setSecond(B second) {
		this.second = second;
	}

	public void setBoth(A i, B j) {
		setFirst(i);
		setSecond(j);
	}
	
	public String toString() {
		return "(" + first.toString() + ", " + second.toString() + ")";
	}
}
