/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A class for holding a quad of heterogenous items
 * 
 * @author Tom Hinton, Simon Goodall
 * 
 * 
 * @param <A>
 *            the type of the first item
 * @param <B>
 *            the type of the second item
 * @param <C>
 *            the type of the third item
 * @param <D>
 *            the type of the fourth item
 */
public class Quad<A, B, C, D> {
	private A first;
	private B second;
	private C third;
	private D fourth;

	@SuppressWarnings("null")
	public Quad() {
		this((A) null, (B) null, (C) null, (D) null);
	}

	public Quad(final A first, final B second, final C third, final D fourth) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public Quad(final @NonNull Quad<A, B, C, D> copy) {
		this.first = copy.getFirst();
		this.second = copy.getSecond();
		this.third = copy.getThird();
		this.fourth = copy.getFourth();
	}

	public static <A, B, C, D> @NonNull Quad<A, B, C, D> of(A first, B second, C third, D fourth) {
		return new Quad<>(first, second, third, fourth);
	}

	public final A getFirst() {
		return first;
	}

	public final void setFirst(final A first) {
		this.first = first;
	}

	public final B getSecond() {
		return second;
	}

	public final void setSecond(final B second) {
		this.second = second;
	}

	public final C getThird() {
		return third;
	}

	public final void setThird(final C third) {
		this.third = third;
	}

	public final D getFourth() {
		return fourth;
	}

	public final void setFourth(final D fourth) {
		this.fourth = fourth;
	}

	/**
	 * Represents the pair as a string in the form (first, second, thrid, fourth).
	 */
	@Override
	public final String toString() {
		return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		final A pFirst = first;
		final B pSecond = second;
		final C pThird = third;
		final D pFourth = fourth;

		result = (prime * result) + ((pFirst == null) ? 0 : pFirst.hashCode());
		result = (prime * result) + ((pSecond == null) ? 0 : pSecond.hashCode());
		result = (prime * result) + ((pThird == null) ? 0 : pThird.hashCode());
		result = (prime * result) + ((pFourth == null) ? 0 : pFourth.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof final Quad<?, ?, ?, ?> other) {
			return Objects.equals(this.first, other.first) //
					&& Objects.equals(this.second, other.second) //
					&& Objects.equals(this.third, other.third) //
					&& Objects.equals(this.fourth, other.fourth);
		}

		return false;
	}

}
