package com.mmxlabs.scheduler.optimiser.providers;

public interface IPromptPeriodProviderEditor extends IPromptPeriodProvider {

	void setStartOfPromptPeriod(int time);

	void setEndOfPromptPeriod(int time);
}
