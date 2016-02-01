/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;

public class LatenessAnnotation implements ILatenessAnnotation {
	int lateness;
	long weightedLateness;
	int latenessWithoutFlex;
	Interval interval;
	Interval intervalWithoutFlex;
	
	public LatenessAnnotation(int lateness, long weightedLateness, Interval interval, int latenessWithoutFlex, Interval intervalWithoutFlex) {
		this.lateness = lateness;
		this.weightedLateness = weightedLateness;
		this.interval = interval;
		this.latenessWithoutFlex = latenessWithoutFlex;
		this.intervalWithoutFlex = intervalWithoutFlex;
	}
	
	@Override
	public int getLateness() {
		return lateness;
	}

	@Override
	public long getWeightedLateness() {
		return weightedLateness;
	}

	@Override
	public int getlatenessWithoutFlex() {
		return latenessWithoutFlex;
	}

	@Override
	public Interval getInterval() {
		return interval;
	}

	@Override
	public Interval getIntervalWithoutFlex() {
		return intervalWithoutFlex;
	}

}
