/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class IntegerElement implements ISequenceElement, Comparable<IntegerElement> {

	private final int idx;

	public IntegerElement(final int idx) {
		this.idx = idx;
	}

	@Override
	public int getIndex() {
		return idx;
	}

	@Override
	@NonNull
	public String getName() {
		return "" + idx;
	}

	@Override
	public int compareTo(final IntegerElement o) {
		return idx - o.idx;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof IntegerElement) {
			return idx == ((IntegerElement) obj).idx;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return idx;
	}
}
