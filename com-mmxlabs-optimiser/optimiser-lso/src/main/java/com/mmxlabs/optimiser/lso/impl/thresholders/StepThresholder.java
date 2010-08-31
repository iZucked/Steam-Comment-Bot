package com.mmxlabs.optimiser.lso.impl.thresholders;

import com.mmxlabs.optimiser.lso.IThresholder;

/**
 * Simple {@link IThresholder} implementation which reduces an initial threshold
 * by a fixed amount each step.
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

	public StepThresholder(int stepSize, long initialThreshold) {
		this.stepSize = stepSize;
		this.initialThreshold = initialThreshold;
		this.currentThreshold = initialThreshold;
	}

	public final int getStepSize() {
		return stepSize;
	}

	public final void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	public final long getInitialThreshold() {
		return initialThreshold;
	}

	public final void setInitialThreshold(long initialThreshold) {
		this.initialThreshold = initialThreshold;
	}

	public final long getCurrentThreshold() {
		return currentThreshold;
	}

	@Override
	public boolean accept(long delta) {

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

}
