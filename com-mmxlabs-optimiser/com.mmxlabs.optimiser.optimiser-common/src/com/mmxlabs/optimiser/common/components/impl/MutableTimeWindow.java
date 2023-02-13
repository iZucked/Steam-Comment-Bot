/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * A {@link ITimeWindow} implementation that can alter it's start and end values after creation.
 * 
 * @author Simon Goodall
 *
 */
public final class MutableTimeWindow implements ITimeWindow {

	private int start;
	private int end;
	private int endFlex;

	public MutableTimeWindow() {

	}

	public MutableTimeWindow(final int start, final int end, final int endFlex) {
		assert start >= 0;
		assert end > 0;
		this.start = start;
		this.end = end;
		this.endFlex = endFlex;
	}

	public MutableTimeWindow(final int start, final int end) {
		this(start, end, 0);
	}

	@Override
	public int getInclusiveStart() {
		return start;
	}

	public void setInclusiveStart(final int start) {
		assert start >= 0;

		this.start = start;
	}

	@Override
	public int getExclusiveEnd() {
		return end;
	}

	public void setExclusiveEnd(final int end) {
		assert end > 0;
		this.end = end;
	}

	@Override
	public int getExclusiveEndFlex() {
		return endFlex;
	}

	public void setExclusiveEndFlex(final int endFlex) {
		this.endFlex = endFlex;
	}

	@Override
	public int getExclusiveEndWithoutFlex() {
		return end - endFlex;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof MutableTimeWindow) {
			final MutableTimeWindow tw = (MutableTimeWindow) obj;
			if (start != tw.start) {
				return false;
			}
			if (end != tw.end) {
				return false;
			}
			if (endFlex != tw.endFlex) {
				return false;
			}
			return true;
		}

		return false;
	}

	@Override
	public final int hashCode() {
		// Based on Arrays.hashCode(int[])
		return Objects.hash(start, end, endFlex);
	}

	public void update(int inclusiveStart, int exclusiveEnd) {
		this.start = inclusiveStart;
		this.end = exclusiveEnd;
	}

	@Override
	public String toString() {
		return String.format("[%d, %d)", start, end);
	}

	@Override
	public boolean overlaps(@NonNull ITimeWindow other) {
		int a = getInclusiveStart();
		int b = getExclusiveEnd() - 1;
		int c = other.getInclusiveStart();
		int d = other.getExclusiveEnd() - 1;

		if (b - c >= 0 && d - a >= 0) {
			return true;
		}
		return false;
	}
}
