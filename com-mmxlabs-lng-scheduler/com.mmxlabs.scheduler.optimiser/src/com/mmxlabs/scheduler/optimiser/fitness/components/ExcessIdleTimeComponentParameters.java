/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

public class ExcessIdleTimeComponentParameters implements IExcessIdleTimeComponentParameters {

	private final int[] threshold = new int[IExcessIdleTimeComponentParameters.Interval.values().length];
	private final int[] weights = new int[IExcessIdleTimeComponentParameters.Interval.values().length];
	private int endWeight;

	@Override
	public int getThreshold(@NonNull final Interval interval) {
		return threshold[interval.ordinal()];
	}

	public void setThreshold(@NonNull final Interval interval, final int threshold) {
		this.threshold[interval.ordinal()] = threshold;
	}

	@Override
	public int getWeight(@NonNull final Interval interval) {
		return weights[interval.ordinal()];
	}

	public void setWeight(@NonNull final Interval interval, final int weight) {
		this.weights[interval.ordinal()] = weight;
	}

	@Override
	public int getEndWeight() {
		return endWeight;
	}

	public void setEndWeight(final int weight) {
		this.endWeight = weight;
	}

}
