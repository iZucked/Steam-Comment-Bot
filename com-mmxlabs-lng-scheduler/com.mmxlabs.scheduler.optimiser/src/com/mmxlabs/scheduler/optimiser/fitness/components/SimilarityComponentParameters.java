/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

public class SimilarityComponentParameters implements ISimilarityComponentParameters {

	private final int[] threshold = new int[ISimilarityComponentParameters.Interval.values().length];
	private final int[] weight = new int[ISimilarityComponentParameters.Interval.values().length];

	@Override
	public int getThreshold(@NonNull final Interval interval) {
		return threshold[interval.ordinal()];
	}

	public void setThreshold(@NonNull final Interval interval, final int threshold) {
		this.threshold[interval.ordinal()] = threshold;
	}

	@Override
	public int getWeight(@NonNull final Interval interval) {
		return this.weight[interval.ordinal()];
	}

	public void setWeight(@NonNull final Interval interval, final int weight) {
		this.weight[interval.ordinal()] = weight;
	}

}
