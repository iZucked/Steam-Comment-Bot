package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;

public interface ILatenessAnnotation extends IElementAnnotation {
	int getLateness();
	long getWeightedLateness();
	Interval  getInterval();
	int getlatenessWithoutFlex();
	Interval getIntervalWithoutFlex();
}
