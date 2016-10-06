package com.mmxlabs.models.lng.analytics.services;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IAnalyticsScenarioEvaluator {
	enum BreakEvenMode {
		POINT_TO_POINT,
		PORTFOLIO
	}

	void evaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork);
	
	void breakEvenEvaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, long targetProfitAndLoss, BreakEvenMode breakEvenMode);

}

