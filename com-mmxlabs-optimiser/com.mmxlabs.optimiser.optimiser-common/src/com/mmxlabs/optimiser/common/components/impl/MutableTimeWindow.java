/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

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
	public int getStart() {
		return start;
	}

	public void setStart(final int start) {
		this.start = start;
	}

	@Override
	public int getEnd() {
		return end;
	}

	public void setEnd(final int end) {
		this.end = end;
	}

	@Override
	public int getEndFlex() {
		return endFlex;
	}
	
	public void setEndFlex(final int endFlex) {
		this.endFlex = endFlex;
	}

	@Override
	public int getEndWithoutFlex() {
		return end - endFlex;
	}
	
	
}
