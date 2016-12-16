/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

public interface IPromptPeriodProvider extends IDataComponentProvider {

	int getStartOfPromptPeriod();

	int getEndOfPromptPeriod();
	
	int getEndOfSchedulingPeriod();
}
