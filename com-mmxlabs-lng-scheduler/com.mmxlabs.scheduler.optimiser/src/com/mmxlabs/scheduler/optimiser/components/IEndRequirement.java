package com.mmxlabs.scheduler.optimiser.components;

public interface IEndRequirement extends IStartEndRequirement {

	boolean isEndCold();

	long getTargetHeelInM3();
}
