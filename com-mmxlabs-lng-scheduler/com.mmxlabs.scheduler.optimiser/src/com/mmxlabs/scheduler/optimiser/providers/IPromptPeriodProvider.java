package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

public interface IPromptPeriodProvider extends IDataComponentProvider {

	int getStartOfPromptPeriod();

	int getEndOfPromptPeriod();
}
