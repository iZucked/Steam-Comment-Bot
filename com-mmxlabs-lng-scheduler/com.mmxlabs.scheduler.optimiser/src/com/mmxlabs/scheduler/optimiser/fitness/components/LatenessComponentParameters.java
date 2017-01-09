/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

public class LatenessComponentParameters implements ILatenessComponentParameters {

	private final int[] threshold = new int[ILatenessComponentParameters.Interval.values().length];
	private final int[] lowWeight = new int[ILatenessComponentParameters.Interval.values().length];
	private final int[] highWeight = new int[ILatenessComponentParameters.Interval.values().length];

	@Override
	public int getThreshold(@NonNull final Interval interval) {
		return threshold[interval.ordinal()];
	}

	public void setThreshold(@NonNull final Interval interval, final int threshold) {
		this.threshold[interval.ordinal()] = threshold;
	}

	@Override
	public int getLowWeight(@NonNull final Interval interval) {
		return lowWeight[interval.ordinal()];
	}

	public void setLowWeight(@NonNull final Interval interval, final int weight) {
		this.lowWeight[interval.ordinal()] = weight;
	}

	@Override
	public int getHighWeight(@NonNull final Interval interval) {
		return highWeight[interval.ordinal()];
	}

	public void setHighWeight(@NonNull final Interval interval, final int weight) {
		this.highWeight[interval.ordinal()] = weight;
	}

}
