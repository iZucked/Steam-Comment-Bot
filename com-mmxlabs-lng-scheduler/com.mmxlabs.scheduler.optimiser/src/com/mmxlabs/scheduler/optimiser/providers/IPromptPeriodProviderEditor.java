/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

public interface IPromptPeriodProviderEditor extends IPromptPeriodProvider {

	void setStartOfPromptPeriod(int time);

	void setEndOfPromptPeriod(int time);
	
	void setEndOfSchedulingPeriod(int time);
}
