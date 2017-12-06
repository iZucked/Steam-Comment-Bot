/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.services;

import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public interface IAnalyticsScenarioEvaluator {
	enum BreakEvenMode {
		POINT_TO_POINT, PORTFOLIO
	}

	void evaluateBaseCase(@NonNull IScenarioDataProvider scenarioDataProvider, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, boolean fork, String forkName);

	void multiEvaluate(@Nullable ScenarioInstance scenarioInstance, EditingDomain editingDomain, @NonNull IScenarioDataProvider scenarioDataProvider, @NonNull UserSettings userSettings,
			long targetProfitAndLoss, BreakEvenMode breakEvenMode, List<Pair<BaseCase, ScheduleSpecification>> baseCases, IMapperClass mapper, BiConsumer<BaseCase, Schedule> resultHandler);

	ScenarioInstance exportResult(ScenarioResult result, String name);
}
