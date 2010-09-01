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
}
