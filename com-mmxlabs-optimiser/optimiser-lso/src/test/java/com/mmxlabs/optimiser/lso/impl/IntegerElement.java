package com.mmxlabs.optimiser.lso.impl;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class IntegerElement implements ISequenceElement, Comparable<IntegerElement> {

	private int idx;

	public IntegerElement(final int idx) {
		this.idx = idx;
	}

	@Override
	public int getIndex() {
		return idx;
	}

	@Override
	public String getName() {
		return "" + idx;
	}

	@Override
	public int compareTo(IntegerElement o) {
		return idx - o.idx;
	}

	@Override
	public boolean equals(Object obj) {

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
