/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import com.mmxlabs.optimiser.lso.IThresholder;

/**
 * Simple {@link IThresholder} implementation which reduces an initial threshold by a fixed amount each step.
 * 
 * @author Simon Goodall
 * 
 */
public final class StepThresholder implements IThresholder {

	private int stepSize;

	private long initialThreshold;

	private long currentThreshold;

	public StepThresholder() {

	}

	public StepThresholder(final int stepSize, final long initialThreshold) {
		this.stepSize = stepSize;
		this.initialThreshold = initialThreshold;
		this.currentThreshold = initialThreshold;
	}

	public final int getStepSize() {
		return stepSize;
	}

	public final void setStepSize(final int stepSize) {
		this.stepSize = stepSize;
	}

	public final long getInitialThreshold() {
		return initialThreshold;
	}

	public final void setInitialThreshold(final long initialThreshold) {
		this.initialThreshold = initialThreshold;
	}

	public final long getCurrentThreshold() {
		return currentThreshold;
	}

	@Override
	public boolean accept(final long delta) {

		return (delta < currentThreshold);
	}

	@Override
	public void init() {
		currentThreshold = initialThreshold;
	}

	@Override
	public void step() {
		currentThreshold -= stepSize;
	}

	@Override
	public void reset() {
		assert false : "Not implemented";
	}

}
