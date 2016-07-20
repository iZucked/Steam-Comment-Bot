/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

import java.util.Objects;

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
		this.start = start;
	}

	@Override
	public int getExclusiveEnd() {
		return end;
	}

	public void setExclusiveEnd(final int end) {
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

}
