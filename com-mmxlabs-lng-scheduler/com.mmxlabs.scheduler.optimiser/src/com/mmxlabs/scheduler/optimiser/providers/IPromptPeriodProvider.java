/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

public interface IPromptPeriodProvider extends IDataComponentProvider {

	int getStartOfPromptPeriod();

	int getEndOfPromptPeriod();

	int getEndOfSchedulingPeriod();

	boolean isPeriodOptimisation();

	int getStartOfOptimisationPeriod();

	int getEndOfOptimisationPeriod();

}
