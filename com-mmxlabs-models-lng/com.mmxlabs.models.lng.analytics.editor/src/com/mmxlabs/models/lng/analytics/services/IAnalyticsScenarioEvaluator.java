/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.services;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator.IMapperClass;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IAnalyticsScenarioEvaluator {
	enum BreakEvenMode {
		POINT_TO_POINT, PORTFOLIO
	}

	void evaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, boolean fork, String forkName);

	void breakEvenEvaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, long targetProfitAndLoss,
			BreakEvenMode breakEvenMode);

	void multiEvaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, long targetProfitAndLoss, BreakEvenMode breakEvenMode,
			List<BaseCase> baseCase, IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap, BiConsumer<BaseCase, Schedule> resultHandler);

}
