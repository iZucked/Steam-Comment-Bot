/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

public interface IEndRequirement extends IStartEndRequirement {

	boolean isEndCold();

	long getTargetHeelInM3();
}
